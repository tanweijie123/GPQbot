package util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {
    /**
     * Converts a String into Discord Message
     * @return JDA.Message if a valid discord link is given, and bot is part of the guild, null otherwise.
     */
    @Nullable
    public static Message convertStringToDiscordMessage(JDA jda, String link) {
        if (link == null || link.isEmpty())
            return null;

        try { //try to check if link is valid.
            new URL(link);
        } catch (MalformedURLException e) {
            System.err.println("Unable to parse link -> " + link);
            return null;
        }

        String[] linkSplit = link.split("/");

        try {
            if (linkSplit[2].contains("discord")) {
                Guild guild = jda.getGuildById(linkSplit[4]);
                TextChannel tc = guild.getTextChannelById(linkSplit[5]);
                Message msg = tc.getHistoryAround(linkSplit[6], 1).complete().getMessageById(linkSplit[6]);
                return msg;
            }
        } catch (NullPointerException npe) {
            System.err.println("Unable to retrieve Message from link -> " + link);
            return null;
        }
        return null;
    }

    public static String toDateTimeString(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public static String toDateTimeStringFileUsage(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ofPattern("ddMMyy_HHmmss"));
    }
}
