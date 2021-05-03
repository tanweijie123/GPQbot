package logic;

import data.JobList;
import db.SQLFunctions;
import model.UserAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersMethod {

    public static UserAccount getOrCreateUser(String guildId, String userId) {
        try {
            PreparedStatement stmt = SQLFunctions.getUserInfo();
            stmt.setString(1, guildId);
            stmt.setString(2, userId);
            ResultSet resultSet = stmt.executeQuery();

            boolean found = resultSet.next();
            if (!found) {
                PreparedStatement stmt2 = SQLFunctions.createNewUser();
                stmt2.setString(1, guildId);
                stmt2.setString(2, userId);
                int update = stmt2.executeUpdate(); //assume success where update == 1
                stmt2.close();

                resultSet = stmt.executeQuery();
                resultSet.next();
            }
            UserAccount ua = new UserAccount(guildId, userId, resultSet.getInt("job"), resultSet.getInt("floor") );

            stmt.close();
            return ua;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getUserInfo() or createNewUser()");
            System.err.println("SQLException: " + ex.getMessage());
            return null;
        }
    }

    public static boolean updateJob(String guildId, String userId, int job) {
        try {
            PreparedStatement stmt = SQLFunctions.updateJob();
            stmt.setInt(1, job);
            stmt.setString(2, guildId);
            stmt.setString(3, userId);
            int update = stmt.executeUpdate();

            stmt.close();
            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function updateJob()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }

    public static boolean updateFloor(String guildId, String userId, int floor) {
        try {
            PreparedStatement stmt = SQLFunctions.updateFloor();
            stmt.setInt(1, floor);
            stmt.setString(2, guildId);
            stmt.setString(3, userId);
            int update = stmt.executeUpdate();

            stmt.close();
            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function updateFloor()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }
}
