package scheduler;

import config.Settings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
    This class supports the discord alert on MapleStory's flag timing (12pm, 7pm, 9pm).
 */
public class FlagAlert {

    /**
     *
     * @param jda discord bot
     * @param time (0 = 12pm, 1 = 7pm, 2 = 9pm)
     */
    static Runnable alarmForFlag(JDA jda, int time) {
        if (time < 0 || time > 2)
            return null;
        Runnable alarm = () -> {
            try {
                Message source = jda.getGuildById("705486807441211442").getTextChannelById("845254323738902548").getHistoryAround("845254430617370684", 1).complete().getRetrievedHistory().get(0);

                MessageReaction sourceReaction = source.getReactions().get(time);
                List<String> toTagUser = sourceReaction.retrieveUsers()
                        .stream().filter(x -> !x.isBot()).map(x -> x.getId()).collect(Collectors.toList());

                StringBuilder sb = new StringBuilder("5mins to Flag (");
                switch (time) {
                    case 0: sb.append("12pm");
                    break;
                    case 1: sb.append("7pm");
                    break;
                    case 2: sb.append("9pm");
                    break;
                }
                sb.append(")\n\n");

                for (String s : toTagUser) {
                    sb.append("<@" + s + "> ");
                }

                source.getChannel().sendMessage(sb.toString()).queue();

            } catch (NullPointerException npe) {
                System.err.println("Unable to find source message");
            }
        };
        return alarm;
    }
}
