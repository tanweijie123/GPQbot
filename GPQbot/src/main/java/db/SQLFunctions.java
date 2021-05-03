package db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLFunctions {
    public static PreparedStatement getUserInfo() throws SQLException, NullPointerException {
        String query = "SELECT job, floor FROM Users WHERE gid = ? AND uid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement createNewUser() throws SQLException {
        String query = "INSERT INTO Users(gid, uid) VALUES (?,?)";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement updateFloor() throws SQLException {
        String query = "UPDATE Users SET floor = ? WHERE gid = ? AND uid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement updateJob() throws SQLException {
        String query = "UPDATE Users SET job = ? WHERE gid = ? AND uid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }
}
