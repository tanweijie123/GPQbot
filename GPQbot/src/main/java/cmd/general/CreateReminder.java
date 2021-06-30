package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import logic.ReminderMethod;
import model.Reminder;
import util.Utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreateReminder extends Command {
    public CreateReminder() {
        super.name = "remindme";
        super.help = "reminds the user of this message at the specified time";
        super.arguments = "[date_time]";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMessage().getReferencedMessage() == null) {
            event.reply("You would need to reply to the message for me to create a reminder.");
            return;
        }

        if (event.getArgs().isEmpty()) {
            event.reply("I would need a date for this command to work (Eg. \"" + Settings.botCommand.getPrefix() +"remindme 30/06/2021 1300\")");
            return;
        }

        ZonedDateTime datetime = null;
        try {
            datetime = ZonedDateTime.of(LocalDateTime.parse(event.getArgs().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm")), ZoneId.of("GMT+8"));
        } catch (DateTimeParseException dtpe) {
            event.reply("I am unable to parse the specified date. Pattern: \"dd/MM/yyyy HHmm\" " +
                    "(Eg. \"" + Settings.botCommand.getPrefix() +"remindme 30/06/2021 1300\")");
            return;
        }

        if (datetime.isBefore(ZonedDateTime.now())) {
            event.reply("The reminder date/time is in the past...");
            return;
        }

        Reminder reminder = new Reminder(0, event.getGuild().getId(), event.getAuthor().getId(),
                ZonedDateTime.now(), datetime,
                event.getMessage().getJumpUrl(), event.getMessage().getReferencedMessage().getJumpUrl());

        if (ReminderMethod.insertReminder(reminder)) {
            event.reply("You will be reminded of this message on " + Utility.toDateTimeString(datetime) + "\n" +
                    "*Note that this service is currently under testing and may not work as planned*");
        }
    }
}
