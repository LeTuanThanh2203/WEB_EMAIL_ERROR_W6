package murach.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import murach.util.SQLUtil;

public class SQLGatewayDB {

    public static String executeSQL(String sqlStatement) {
        String sqlResult = "";

        ConnectionPool pool = ConnectionPool.getInstance();

        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {

            sqlStatement = sqlStatement.trim();

            if (sqlStatement.length() >= 6) {

                String sqlType = sqlStatement.substring(0, 6);

                if (sqlType.equalsIgnoreCase("select")) {

                    ResultSet resultSet = statement.executeQuery(sqlStatement);

                    sqlResult = SQLUtil.getHtmlTable(resultSet);

                    resultSet.close();

                } else {

                    int i = statement.executeUpdate(sqlStatement);

                    if (i == 0) {

                        sqlResult = "<p>The statement executed successfully.</p>";

                    } else {

                        sqlResult = "<p>The statement executed successfully.<br>"
                                + i
                                + " row(s) affected.</p>";
                    }
                }

            } else {

                sqlResult = "<p>Please enter a valid SQL statement.</p>";
            }

        } catch (SQLException e) {

            sqlResult = "<p>Error executing the SQL statement:<br>"
                    + e.getMessage()
                    + "</p>";

            e.printStackTrace();
        }

        return sqlResult;
    }
}
