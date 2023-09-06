import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RFJoin4Test {
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

        String accountCP = "a".repeat(60);
        List<YourResultType> resultList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.prepareStatement("SELECT * FROM BUFFERFLUSH").execute();
            connection.prepareStatement("SELECT * FROM BUFFERFLUSH").execute();
            connection.prepareStatement("SELECT * FROM BUFFERFLUSH").execute();

            String selectQuery = "SELECT * FROM ReadAuthority AS R INNER JOIN Folder AS F ON R.folderCP = F.folderCP WHERE accountCP = ?;";

            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, accountCP);

                long startTime = System.currentTimeMillis();

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        // Fetch the values from ResultSet and create your result object
                        YourResultType result = new YourResultType();
                        result.setAccountCP(resultSet.getString("accountCP"));
                        result.setFolderCP(resultSet.getString("folderCP"));
                        result.setSymmetricKeyEWA(resultSet.getString("symmetricKeyEWA"));

                        resultList.add(result);
                    }
                }

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                return executionTime;
                // System.out.println("Query execution time: " + executionTime + " milliseconds");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Use the resultList as per your requirement
        // ...
        return 0;
    }

    private static String buildInClause(int size) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < size; i++) {
            sb.append("?");
            if (i < size - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    // Define your result object type
    private static class YourResultType {
        private String accountCP;
        private String folderCP;
        private String symmetricKeyEWA;

        public String getAccountCP() {
            return accountCP;
        }

        public void setAccountCP(String accountCP) {
            this.accountCP = accountCP;
        }

        public String getFolderCP() {
            return folderCP;
        }

        public void setFolderCP(String folderCP) {
            this.folderCP = folderCP;
        }

        public String getSymmetricKeyEWA() {
            return symmetricKeyEWA;
        }

        public void setSymmetricKeyEWA(String symmetricKeyEWA) {
            this.symmetricKeyEWA = symmetricKeyEWA;
        }
    }
}

