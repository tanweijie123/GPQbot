package logic;

import db.SQLFunctions;
import model.Reminder;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReminderMethod {
    public static List<Reminder> getReminders() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+8"));
        now = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0, 0, ZoneId.of("GMT+8"));
        ZonedDateTime now_plus1Min = now.plusMinutes(1);

        try {
            PreparedStatement stmt = SQLFunctions.getReminders();
            stmt.setObject(1, now.toLocalDateTime());
            stmt.setObject(2, now_plus1Min.toLocalDateTime());
            ResultSet resultSet = stmt.executeQuery();

            List<Reminder> reminderList = new ArrayList<>();
            while(resultSet.next()) {
                Reminder r = new Reminder(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getTimestamp(4).toInstant().atZone(ZoneId.of("GMT+8")),
                        resultSet.getTimestamp(5).toInstant().atZone(ZoneId.of("GMT+8")),
                        resultSet.getString(6));
                reminderList.add(r);
            }
            stmt.close();
            return reminderList;
        } catch (SQLException ex) {
            System.err.println("Error in performing function getReminders()");
            System.err.println("SQLException: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean insertReminder(Reminder reminder) {
        try {
            PreparedStatement stmt = SQLFunctions.insertReminder();
            stmt.setString(1, reminder.gid);
            stmt.setString(2, reminder.uid);
            stmt.setTimestamp(3, Timestamp.from(reminder.createdDate.toInstant()));
            stmt.setTimestamp(4, Timestamp.from(reminder.expectedDate.toInstant()));
            stmt.setString(5, reminder.reminderLink);

            int update = stmt.executeUpdate();

            stmt.close();
            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function insertReminder()\n  -> " + reminder.toString());
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }
}
