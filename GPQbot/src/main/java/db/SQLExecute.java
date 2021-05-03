package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLExecute {
    public static void main(String[] args) throws SQLException {
        Connection conn = SQLConn.getConnection();
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM a;";
        ResultSet resultSet = stmt.executeQuery(query);

        while(resultSet.next()) {
            System.out.println(resultSet.getString(1)); //1-based table
        }

        conn.close();
    }
}
