package eval_tx_response.folder;

import eval_tx_response.TxTestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SearchFromSlave extends TxTestTemplate {
    @Override
    public void query(Connection connection) throws SQLException {
        String keyword = "test";
        findOne(keyword, connection);
        List<FolderSearch> search_list = findAll(connection);

        Map<String, Integer> simHash = new HashMap<>();
        simHash.put("fds", 1);

        for (FolderSearch folderSearch : search_list) {
            if (!simHash.containsKey(folderSearch.getTitle())) {
                simHash.put(
                        folderSearch.getTitle(),
                        (int)similarity(keyword, folderSearch.getTitle())
                );
            }
        }

        search_list.sort(new Comparator<FolderSearch>() {
            @Override
            public int compare(FolderSearch o1, FolderSearch o2) {
                return -Integer.compare(
                        simHash.get(o1.getTitle()),
                        simHash.get(o2.getTitle())
                );
            }
        });
    }

    private void findOne(String keyword, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Folder WHERE folderCP = ?;"
        );

        preparedStatement.setString(1, keyword);
        preparedStatement.execute();
    }

    private List<FolderSearch> findAll(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Folder_SLAVE_ISAM;"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        List<FolderSearch> list = new ArrayList<>();

        while (resultSet.next()) {
            list.add(new FolderSearch(
                resultSet.getString(1), resultSet.getString(2)
            ));
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

    public static class FolderSearch {
        private final String folderCP;
        private final String title;

        public FolderSearch(String folderCP, String title) {
            this.folderCP = folderCP;
            this.title = title;
        }

        public String getFolderCP() {
            return folderCP;
        }

        public String getTitle() {
            return title;
        }
    }
}
