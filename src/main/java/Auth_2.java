import java.sql.*;
import java.util.Random;

public class Auth_2 {
    public static void main(String[] args) {
        // JDBC connection parameters
        String url = "jdbc:mariadb://localhost:3306/dong_demo_test";
        String username = "root";
        String password = "1234";

        // Number of records to be inserted
        int numRecords = 100000;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            ResultSet resultSet = connection.prepareStatement(
                    "SELECT * FROM TMP_MATCH;"
            ).executeQuery();

            PreparedStatement preparedStatement = connection.prepareStatement("""
                        INSERT INTO SubscribeDemand_UNCLUST(accountCP, folderCP, accountPublicKey) VALUES(?, ?, ?);
            """);

            connection.setAutoCommit(false);

            int count = 0;
            while(resultSet.next()) {
                preparedStatement.setString(1, resultSet.getString(2));
                preparedStatement.setString(2, resultSet.getString(1));
                preparedStatement.setString(3, generateRandomString(300));
                preparedStatement.addBatch();
                if (++count % 1000 == 0) {
                    System.out.println("insert " + count + " completed !!");
                }
            }

            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
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
