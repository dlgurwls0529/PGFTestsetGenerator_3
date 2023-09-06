package eval_tx_response.writeAuth;

import eval_tx_response.TxTestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetWriteAuthByACP_block extends TxTestTemplate {
    @Override
    public void query(Connection connection) throws SQLException {
        String readAuthQuery = """
                    SELECT * FROM WriteAuthority WHERE accountCP = ?;
                """;
        PreparedStatement rPstmt = connection.prepareStatement(readAuthQuery);
        rPstmt.setString(1, "FHk2fU3IuC3KkGszMbQ2qsPElX79hPB1BF9Oj7RqKWeKyZnHVyfEeB4OW66G");
        ResultSet rResultSet = rPstmt.executeQuery();

        String folderQuery_prefix = "SELECT * FROM Folder WHERE folderCP IN (";
        StringBuilder folderQuery_content = new StringBuilder();
        String folderQuery_suffix = ");";

        while (rResultSet.next()) {
            folderQuery_content
                    .append('\'')
                    .append(rResultSet.getString("folderCP"))
                    .append('\'')
                    .append(",");
        }

        folderQuery_content.deleteCharAt(
                folderQuery_content.length()-1
        );

        connection.prepareStatement(
                folderQuery_prefix
                + folderQuery_content.toString()
                + folderQuery_suffix
        ).execute();

    }

    private String innerClauseGenerator(int n) {
        StringBuilder inClauseBuilder = new StringBuilder();
        inClauseBuilder.append("?,".repeat(Math.max(0, n)));
        inClauseBuilder.deleteCharAt(inClauseBuilder.length() - 1);

        return inClauseBuilder.toString();
    }
}
