import java.sql.*;
import java.util.Random;

public class Folder {
    public static void main(String[] args) {

        String url = "jdbc:mariadb://localhost:3306/dong_demo_test";
        String username = "root";
        String password = "1234";
        int numRecords = 100000;

        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, username, password);

            String insertQuery = "INSERT INTO Folder (folderCP, isTitleOpen, title, symmetricKeyEWF, lastChangedDate) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                Random random = new Random();

                for (int i = 0; i < numRecords; i++) {
                    pstmt.setString(1, generateRandomString(60)); // folderCP
                    pstmt.setBoolean(2, random.nextDouble() < 0.50); // isTitleOpen
                    pstmt.setString(3, generateRandomString(100)); // title
                    pstmt.setString(4, generateRandomString(300)); // symmetricKeyEWF
                    pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // lastChangedDate

                    pstmt.addBatch();

                    // Execute batch every 1000 records
                    if (i % 1000 == 0) {
                        pstmt.executeBatch();
                    }
                }

                // Execute any remaining batched records
                pstmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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
