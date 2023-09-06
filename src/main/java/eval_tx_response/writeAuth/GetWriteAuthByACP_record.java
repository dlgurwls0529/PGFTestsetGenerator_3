package eval_tx_response.writeAuth;

import eval_tx_response.TxTestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetWriteAuthByACP_record extends TxTestTemplate {
    @Override
    public void query(Connection connection) throws SQLException {
        String readAuthQuery = """
                    SELECT * FROM WriteAuthority WHERE accountCP = ?;
                """;
        PreparedStatement rPstmt = connection.prepareStatement(readAuthQuery);
        rPstmt.setString(1, "FHk2fU3IuC3KkGszMbQ2qsPElX79hPB1BF9Oj7RqKWeKyZnHVyfEeB4OW66G");
        ResultSet rResultSet = rPstmt.executeQuery();

        while (rResultSet.next()) {
            PreparedStatement fPstmt = connection.prepareStatement(
                    "SELECT * FROM Folder WHERE folderCP = ?;"
            );
            fPstmt.setString(1, rResultSet.getString("folderCP"));
            fPstmt.execute();
        }
    }
}
