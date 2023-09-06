package eval_tx_response.folder;

import eval_tx_response.TxTestTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SearchFromMaster extends TxTestTemplate {
    @Override
    public void query(Connection connection) throws SQLException {
        String keyword = "test";
        List<String[]> search_list = findAllFolderCPAndTitle(connection);
        int length = search_list.size();

        /*if (length == 0 || keyword.length() == 0) {
            return new ArrayList<>();
        }*/

        int[] similarity_folderCP = new int[length];
        int[] similarity_title = new int[length];

        similarity_folderCP[0] = (int)similarity(keyword, search_list.get(0)[0]);
        similarity_title[0] = (int)similarity(keyword, search_list.get(0)[1]);

        int folderCP_sim_min = similarity_folderCP[0];
        int folderCP_sim_max = similarity_folderCP[0];

        int title_sim_min = similarity_title[0];
        int title_sim_max = similarity_title[0];

        for (int i = 1; i < length; i++) {
            similarity_folderCP[i] = (int)similarity(keyword, search_list.get(i)[0]);
            similarity_title[i] = (int)similarity(keyword, search_list.get(i)[1]);

            if (similarity_folderCP[i] < folderCP_sim_min) {
                folderCP_sim_min = similarity_folderCP[i];
            }
            if (similarity_folderCP[i] > folderCP_sim_max) {
                folderCP_sim_max = similarity_folderCP[i];
            }
            if (similarity_title[i] < title_sim_min) {
                title_sim_min = similarity_title[i];
            }
            if (similarity_title[i] > title_sim_max) {
                title_sim_max = similarity_title[i];
            }
        }

        for(int i = 0; i < length; i++) {
            float scaled_folderCP_sim = 0.5f * getMinMaxScaled(similarity_folderCP[i], folderCP_sim_max, folderCP_sim_min);
            float scaled_title_sim = 0.5f * getMinMaxScaled(similarity_title[i], title_sim_max, title_sim_min);
            float total_similarity = scaled_folderCP_sim + scaled_title_sim;
            String[] temp = {
                    search_list.get(i)[0],
                    search_list.get(i)[1],
                    String.valueOf(total_similarity)
            };
            search_list.set(i, temp);
        }

        search_list.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return -Float.compare(Float.parseFloat(o1[2]), Float.parseFloat(o2[2]));
            }
        });
    }

    private float getMinMaxScaled(int input, int max, int min) {
        // max min 같은 경우를 따져봤는데 그냥 0 리턴해도 될 듯
        if (max == min) {
            return 0f;
        }
        else {
            return ((float)input - (float)min) / ((float)max - (float)min);
        }
    }

    private List<String[]> findAllFolderCPAndTitle(Connection connection) throws SQLException {
        List<String[]> list = new ArrayList<>();
        ResultSet resultSet =
                connection.prepareStatement(
                        "SELECT * FROM Folder;"
                ).executeQuery();

        while (resultSet.next()) {
            list.add(
                    new String[]{
                            resultSet.getString(1),
                            resultSet.getString(2)
                    }
            );
        }

        return list;
    }

    private String[] ngram(String s, int num) {
        int res_len = s.length()-num+1;
        String[] ngram = new String[res_len];

        for (int i = 0; i < res_len; i++) {
            StringBuilder ngram_one = new StringBuilder();

            for (int j = 0; j < num; j++) {
                ngram_one.append(s.charAt(i + j));
            }
            ngram[i] = String.valueOf(ngram_one);
        }

        return ngram;
    }

    public float similarity(String input, String target) {
        String[] input_ngram = ngram(input, 2);
        String[] target_ngram = ngram(target, 2);

        int cnt = 0;
        for (String i : input_ngram) {
            for (String t : target_ngram) {
                if (i.equals(t)) {
                    cnt+=1;
                }
            }
        }

        // float next = Math.nextUp(target_ngram.length);

        return (float)cnt; // / next;
    }
}
