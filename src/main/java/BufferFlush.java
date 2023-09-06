import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class BufferFlush {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mariadb://localhost:3306/dong_demo_test";
        String username = "root";
        String password = "1234";
        int record_num = 5000; // 변경할 레코드 수

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            String sql = "INSERT INTO BF VALUES(?);";
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < record_num; i++) {
                preparedStatement.setString(1, generateRandomString(16384));
                preparedStatement.addBatch(); // 배치 작업에 추가
                if ((i + 1) % 1000 == 0) { // 1000개의 배치로 나눔
                    preparedStatement.executeBatch(); // 배치 실행
                    preparedStatement.clearBatch(); // 배치 초기화
                }
            }

            // 마지막에 남은 배치 실행
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
