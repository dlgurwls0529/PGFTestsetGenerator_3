import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class RandomCP {
    public static void main(String[] args) {
        final String url = "jdbc:mariadb://localhost:3306/dong_demo_test";
        final String username = "root";
        final String password = "1234";
        final int numRecords = 447;
        final String table = "TMP_AccountCP";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);

            for (int i = 0; i < numRecords; i++) {
                String sql = "INSERT INTO " + table + " VALUES(?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, generateRandomString(60));
                preparedStatement.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
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

