package scheduler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

/**
 * Clears all message from POV.
 */
public class ClearMessage {

    /**
     * Clears Message After the FlagAlert message --> hardcoded
     * Assumption: all messages are less than 2 weeks old, and total messages are <= 100.
     */
    static Runnable clearAfterMessage(JDA jda) {
        final String GUILDID = "705486807441211442";
        final String CHANNELID = "845254323738902548";
        final String MESSAGEID = "845254430617370684";
        Runnable clear = () -> {
            try {
                TextChannel channel = jda.getGuildById(GUILDID).getTextChannelById(CHANNELID);

                List<Message> msgHistory;
                do {
                    msgHistory = channel.getHistoryAfter(MESSAGEID, 100).complete().getRetrievedHistory();

                    if (msgHistory.size() > 1) {
                        channel.deleteMessages(msgHistory).queue();
                    } else if (msgHistory.size() == 1) {
                        msgHistory.get(0).delete().queue();
                    }
                } while (msgHistory.size() > 0);

            } catch (NullPointerException npe) {
                System.err.println("Unable to find text channel for scheduled deletion");
            }
        };
        return clear;
    }
}
