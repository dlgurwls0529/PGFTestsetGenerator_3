import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class WriteAuth {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ACCOUNT_CP_LENGTH = 60;
    private static final int FOLDER_PUB_LENGTH = 300;
    private static final int FOLDER_PRI_LENGTH = 300;

    public static void main(String[] args) {
        // JDBC connection parameters
        String url = "jdbc:mariadb://localhost:3306/dong_demo_test";
        String username = "root";
        String password = "1234";

        // Number of records to be inserted
        int numRecords = 100000;

        // SQL statements
        String folderSql = "SELECT folderCP FROM Folder ORDER BY RAND() LIMIT ?";
        String insertSql = "INSERT INTO WriteAuthority (accountCP, folderCP, folderPublicKey, folderPrivateKeyEWA) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement folderStatement = connection.prepareStatement(folderSql);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            // Disable auto-commit to improve performance
            connection.setAutoCommit(false);

            // Set the number of folders to retrieve
            folderStatement.setInt(1, numRecords);

            // Retrieve random folderCP values from Folder table
            ResultSet folderResult = folderStatement.executeQuery();

            // Prepare the data to be inserted
            Random random = new Random();
            StringBuilder accountCPBuilder = new StringBuilder(ACCOUNT_CP_LENGTH);
            StringBuilder folderPublicKeyBuilder = new StringBuilder(FOLDER_PUB_LENGTH);
            StringBuilder folderPrivateKeyEWABuilder = new StringBuilder(FOLDER_PRI_LENGTH);

            // Insert rows
            while (folderResult.next()) {
                // Generate random accountCP and symmetricKeyEWA values
                generateRandomString(random, accountCPBuilder, ACCOUNT_CP_LENGTH);
                generateRandomString(random, folderPublicKeyBuilder, FOLDER_PUB_LENGTH);
                generateRandomString(random, folderPrivateKeyEWABuilder, FOLDER_PRI_LENGTH);

                String folderCP = folderResult.getString("folderCP");
                String accountCP = accountCPBuilder.toString();
                String folderPublicKey = folderPublicKeyBuilder.toString();
                String folderPrivateKeyEWA = folderPrivateKeyEWABuilder.toString();

                // Reset string builders for next iteration
                accountCPBuilder.setLength(0);
                folderPublicKeyBuilder.setLength(0);
                folderPrivateKeyEWABuilder.setLength(0);

                insertStatement.setString(1, accountCP);
                insertStatement.setString(2, folderCP);
                insertStatement.setString(3, folderPublicKey);
                insertStatement.setString(4, folderPrivateKeyEWA);
                insertStatement.addBatch();
            }

            // Execute batch in batches of 1000 for better performance
            insertStatement.executeBatch();
            connection.commit();

            System.out.println("Data insertion completed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateRandomString(Random random, StringBuilder stringBuilder, int length) {
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
    }
}
