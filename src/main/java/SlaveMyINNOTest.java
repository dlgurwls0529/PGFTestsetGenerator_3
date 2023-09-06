import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SlaveMyINNOTest {
    public static void main(String[] args) {
        long sum = 0L;
        int epoch = 5;
        for (int i = 0; i < epoch; i++) {
            sum += test();
        }
        System.out.println("Query execution time: " + (float) sum / (float)epoch + " milliseconds");
    }
    public static long test() {
        String url = "jdbc:mariadb://localhost:3306/dong_demo_test";
        String username = "root";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.prepareStatement("SELECT * FROM BUFFERFLUSH").execute();
            connection.prepareStatement("SELECT * FROM BUFFERFLUSH").execute();
            connection.prepareStatement("SELECT * FROM BUFFERFLUSH").execute();

            String selectQuery = "SELECT * FROM Folder_S_50";

            long startTime = System.currentTimeMillis();
            connection.prepareStatement(selectQuery).execute();
            long endTime = System.currentTimeMillis();
            return endTime - startTime;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Use the resultList as per your requirement
        // ...
        return 0;
    }
}

