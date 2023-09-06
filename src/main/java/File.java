import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

public class File {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mariadb://localhost:3306/dong_demo_test";
        String username = "root";
        String password = "1234";
        int file_per_folder = 5; // 변경할 레코드 수

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Get random folderCP values from Folder table
            for (int i = 0; i < file_per_folder; i++) {
                String folderCPQuery = "SELECT folderCP FROM Folder ORDER BY RAND()";
                try (PreparedStatement folderCPStmt = conn.prepareStatement(folderCPQuery)) {
                    try (ResultSet folderCPRS = folderCPStmt.executeQuery()) {
                        String insertQuery = "INSERT INTO File (folderCP, fileId, subheadEWS, lastChangedDate, contentsEWS) VALUES (?, UNHEX(REPLACE(?, '-', '')), ?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            Random random = new Random();
                            while (folderCPRS.next()) {
                                String folderCP = folderCPRS.getString("folderCP");
                                String fileId = UUID.randomUUID().toString();
                                String subheadEWS = generateRandomString(300);
                                Timestamp lastChangedDate = new Timestamp(System.currentTimeMillis());
                                String contentsEWS = generateRandomString(300);

                                insertStmt.setString(1, folderCP);
                                insertStmt.setString(2, fileId);
                                insertStmt.setString(3, subheadEWS);
                                insertStmt.setTimestamp(4, lastChangedDate);
                                insertStmt.setString(5, contentsEWS);

                                insertStmt.addBatch();
                            }

                            insertStmt.executeBatch();
                            System.out.println("Data insertion completed.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
