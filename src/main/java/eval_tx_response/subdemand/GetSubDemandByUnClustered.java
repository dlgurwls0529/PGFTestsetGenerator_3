package eval_tx_response.subdemand;

import eval_tx_response.TxTestTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetSubDemandByUnClustered extends TxTestTemplate {
    @Override
    public void query(Connection connection) throws SQLException {
        String folderCP = "smJoRHr3JYzMzhjOCMCZHNy5sjTzIdOPTkkR6W2y6LXc4Mp5DaU12jgKyGke";
        connection.prepareStatement(
                "SELECT * FROM SubscribeDemand_UNCLUST WHERE folderCP = '" + folderCP + "';"
        ).execute();
    }
}
