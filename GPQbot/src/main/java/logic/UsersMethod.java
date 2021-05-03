package logic;

import db.SQLFunctions;
import model.UserAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersMethod {

    public static UserAccount getOrCreateUser(String guildId, String userId) {
        try {
            PreparedStatement stmt = SQLFunctions.getUserInfo();
            stmt.setString(1, guildId);
            stmt.setString(2, userId);
            ResultSet resultSet = stmt.executeQuery();

            boolean found = resultSet.next();
            if (!found) {
                createUser(guildId, userId);
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

    public static UserAccount getUser(String guildId, String userId) {
        try {
            PreparedStatement stmt = SQLFunctions.getUserInfo();
            stmt.setString(1, guildId);
            stmt.setString(2, userId);
            ResultSet resultSet = stmt.executeQuery();

            boolean found = resultSet.next();
            if (!found) {
                return null;
            }
            UserAccount ua = new UserAccount(guildId, userId, resultSet.getInt("job"), resultSet.getInt("floor") );

            stmt.close();
            return ua;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getUserInfo()");
            System.err.println("SQLException: " + ex.getMessage());
            return null;
        }
    }

    public static List<UserAccount> getUsers(String guildId, List<String> userId) {
        List<UserAccount> uaList = new ArrayList<>();

        try {
            PreparedStatement stmt = SQLFunctions.getUserInfo();
            stmt.setString(1, guildId);

            for (String s : userId) {
                stmt.setString(2, s);
                ResultSet resultSet = stmt.executeQuery();

                boolean found = resultSet.next();
                UserAccount ua = null;
                if (!found) {
                    ua = new UserAccount(guildId, s, 0, 0);
                } else {
                    ua = new UserAccount(guildId, s, resultSet.getInt("job"), resultSet.getInt("floor"));
                }
                uaList.add(ua);
            }

            stmt.close();
            return uaList;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getUserInfo()");
            System.err.println("SQLException: " + ex.getMessage());
            return null;
        }
    }

    public static boolean createUser(String guildId, String userId) {
        try {
            PreparedStatement stmt = SQLFunctions.createNewUser();
            stmt.setString(1, guildId);
            stmt.setString(2, userId);
            int update = stmt.executeUpdate(); //assume success where update == 1
            stmt.close();

            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function createNewUser()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
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
