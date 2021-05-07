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

    public static PreparedStatement getCurrentGPQLink() throws SQLException {
        String query = "SELECT link FROM GpqCurrent WHERE gid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement setCurrentGPQLink() throws SQLException {
        String query = "INSERT INTO GpqCurrent(gid, link) VALUES (?,?)";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement deleteCurrentGPQLink() throws SQLException {
        String query = "DELETE FROM GpqCurrent WHERE gid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement insertGpqConfirmed() throws SQLException {
        String query = "INSERT INTO GpqConfirmed(gid, confirmedTime) VALUES (?,?)";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement getLatestGpqConfirmed() throws SQLException {
        String query = "SELECT * FROM GpqConfirmed WHERE gid = ? ORDER BY confirmedTime DESC LIMIT 1";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement insertGpqParticipants() throws SQLException {
        String query = "INSERT INTO GpqParticipants VALUES (?,?,?)";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement gpqRanking() throws SQLException {
        String query = "SELECT uid, COUNT(*) FROM GpqParticipants WHERE gid = ? GROUP BY uid ORDER BY COUNT(*) DESC";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement getGuildMembers() throws SQLException {
        String query = "SELECT uid, job, floor FROM Users WHERE gid = ? ORDER BY floor DESC";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement appendMemberToCurrent() throws SQLException {
        String query = "INSERT INTO GpqCurrentAddMem " +
                "SELECT * FROM (SELECT ?, ?) AS temp WHERE NOT EXISTS (" +
                "SELECT 1 FROM GpqCurrentAddMem WHERE gid = ? AND uid = ?" +
                ")";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement getAppendedMembersToCurrent() throws SQLException {
        String query = "SELECT uid FROM GpqCurrentAddMem WHERE gid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }
}
