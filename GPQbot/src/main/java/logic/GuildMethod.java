package logic;

import db.SQLFunctions;
import model.UserAccount;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GuildMethod {

    public static String getCurrentGPQLink(String guildId) {
        try {
            PreparedStatement stmt = SQLFunctions.getCurrentGPQLink();
            stmt.setString(1, guildId);
            ResultSet resultSet = stmt.executeQuery();
            boolean found = resultSet.next();

            String link = null;
            if (found) {
                link = resultSet.getString("link");
            }
            stmt.close();
            return link;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getCurrentGPQLink()");
            System.err.println("SQLException: " + ex.getMessage());
            return null;
        }
    }

    public static boolean setCurrentGPQLink(String guildId, String link) {
        try {
            PreparedStatement stmt = SQLFunctions.setCurrentGPQLink();
            stmt.setString(1, guildId);
            stmt.setString(2, link);
            int update = stmt.executeUpdate();
            stmt.close();

            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function setCurrentGPQLink()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }

    }

    public static boolean deleteCurrentGPQLink(String guildId) {
        try {
            PreparedStatement stmt = SQLFunctions.deleteCurrentGPQLink();
            stmt.setString(1, guildId);
            int update = stmt.executeUpdate();
            stmt.close();

            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function deleteCurrentGPQLink()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }

    }

    public static boolean insertGpqConfirmation(String guildId, List<UserAccount> uaList) {
        String timestamp = ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
        try {
            PreparedStatement stmt = SQLFunctions.insertGpqConfirmed();
            stmt.setString(1, guildId);
            stmt.setString(2, timestamp);
            stmt.executeUpdate();
            stmt.close();

            PreparedStatement stmt3 = SQLFunctions.insertGpqParticipants();
            for (UserAccount ua : uaList) {
                stmt3.setString(1, guildId);
                stmt3.setString(2, timestamp);
                stmt3.setString(3, ua.getUserId());
                stmt3.addBatch();
            }

            stmt3.executeBatch();
            stmt3.close();
            return true;

        } catch (SQLException ex) {
            System.err.println("Error in performing function insertGpqConfirmation()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }
}
