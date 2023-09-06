package eval_tx_response;

import org.checkerframework.checker.units.qual.C;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class TxTestTemplate {

    protected final String url = "jdbc:mariadb://localhost:3306/dong_demo_test";
    protected final String username = "root";
    protected final String password = "1234";

    public float getAvgExecutionTime(int n_iteration) {
        Long acc_execution_time = 0L;
        for (int i = 0; i < n_iteration; i++) {
            buffer_clear();
            Long beg = System.currentTimeMillis();
            queryTemplate();
            Long end = System.currentTimeMillis();
            acc_execution_time += (end - beg);
            System.out.println("cur_iter : " + (i + 1));
            System.out.println("cur_avg : " + ((float)acc_execution_time) / ((float)(i+1)) + " ms");
        }

        return ((float)acc_execution_time) / ((float)n_iteration);
    }

    private void buffer_clear() {
        try(Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.prepareStatement("SELECT * FROM BF;").execute();
            connection.prepareStatement("SELECT * FROM BF;").execute();
            connection.prepareStatement("SELECT * FROM BF;").execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void queryTemplate() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            query(connection);
            connection.commit();
            connection.setAutoCommit(true);
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

    public abstract void query(Connection connection) throws SQLException;
}
