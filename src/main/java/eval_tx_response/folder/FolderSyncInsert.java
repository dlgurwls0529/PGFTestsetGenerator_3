package eval_tx_response.folder;

import eval_tx_response.TxTestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class FolderSyncInsert extends TxTestTemplate {

    @Override
    public void query(Connection connection) throws SQLException {
        String folderCP = generateRandomString(60);
        String title = generateRandomString(20);

        PreparedStatement p1 = connection.prepareStatement("""
                                INSERT INTO
                                    Folder (folderCP, isTitleOpen, title, symmetricKeyEWF, lastChangedDate)
                                    VALUES (?, TRUE, ?, 'sym_TEST', NOW());
                            """);
        p1.setString(1, folderCP);
        p1.setString(2, title);
        p1.execute();

        PreparedStatement p2 = connection.prepareStatement("""
                                INSERT INTO
                                    Folder_SLAVE_ISAM (folderCP, title)
                                    VALUES (?, ?);
                            """);
        p2.setString(1, folderCP);
        p2.setString(2, title);

        p2.execute();
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }
}
