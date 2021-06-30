package scheduler;

import logic.ReminderMethod;
import model.Reminder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import util.Utility;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public class SendReminder {

    /**
     * Reminder messages are sent once every minute.
     */
    static Runnable sendReminder(JDA jda) {
        Runnable clear = () -> {
            List<Reminder> reminderList = ReminderMethod.getReminders();

            for (Reminder r : reminderList) {
                try {
                    //TODO: add reaction so more people can share reminder.

                    Message msg = Utility.convertStringToDiscordMessage(jda, r.sourceLink);

                    String toSend = String.format("<@%s> You have requested for a reminder on %s for this message.",
                            r.uid, Utility.toDateTimeString(r.createdDate));
                    msg.reply(toSend).queue();

                } catch (NullPointerException e) {
                    System.err.println("Message link returned from database is null\n  -> " + r.toString());
                }
            }

        };
        return clear;
    }
}
