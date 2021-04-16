package event;

import config.Settings;
import data.Data;
import model.UserAccount;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TextEvent extends ListenerAdapter {

    /*
        Nyaa commands:
        !nyan as nyaa show reg [limit x] - show all participants for all days without closing

     */

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().startsWith(Settings.botCommand.getPrefix())) {
            return;
        }

        if (event.getMessage().getContentRaw().startsWith(Settings.botCommand.getPrefix() + "as nyaa")) {
            runCreatorEvent(event);
        }
    }

    private void runCreatorEvent(GuildMessageReceivedEvent event) {
        if (event.getAuthor().getIdLong() != Settings.botCommand.getOwnerIdLong()) {
            event.getMessage().reply("Hmmmm ... Who are you?").queue();
            return;
        }

        /* Command => !nyan as nyaa show reg [limit] */
        if (event.getMessage().getContentRaw().contains("show reg")) {
            int limit = Integer.MAX_VALUE;

            if (event.getMessage().getContentRaw().contains("limit")) {
                try {
                    limit = Integer.parseInt(event.getMessage().getContentRaw().split(" ")[6]);
                } catch (Exception e) { }
            }

            String[] creationMsg = Data.currentGPQList.getByGuildKey(event.getGuild().getIdLong()).split("/");

            //TODO: check if existingReg is manually deleted.
            Message actualEb = event.getGuild().getTextChannelById(creationMsg[5]).getHistoryAround(creationMsg[6], 1).complete().getRetrievedHistory().get(0);

            List<MessageReaction> ebReact = actualEb.getReactions();
            String reply = "";

            for (int i = 0; i < 1; i++) { //hard code to yes (0) and no (1)
                List<User> usrList = ebReact.get(i).retrieveUsers().stream().filter(x -> !x.isBot()).collect(Collectors.toList());

                if (i == 0) {
                    reply += String.format("For Participating = %d \n", usrList.size());
                } else if (i == 1) {
                    reply += String.format("For NOT Participating = %d \n", usrList.size());
                }

                List<UserAccount> usrForDay = usrList.stream()
                        .map(x -> {
                            UserAccount ua = Data.currentUserList.getByUserKey(x.getIdLong(), event.getGuild().getMember(x).getEffectiveName());
                            if (ua == null) {
                                ua = new UserAccount(x.getIdLong());
                                ua.setIgn(event.getGuild().getMember(x).getEffectiveName());
                            }
                            return ua;
                        })//set null so no need to check effective name too many times
                        .sorted(Comparator.comparingInt(UserAccount::getFloor).reversed())
                        .collect(Collectors.toList());

                int thisLimit = Math.min(limit, usrForDay.size());

                for (int j = 0; j < thisLimit; j++) {
                    reply += String.format("%d. %s\n", j+1, usrForDay.get(j).printString());
                }

                if (thisLimit < usrForDay.size()) {
                    reply += "...\n";
                }

                reply += "\n";

            }

            event.getChannel().sendMessage(reply).complete().delete().queueAfter(30, TimeUnit.SECONDS);;
            event.getMessage().delete().queue();
            return;
        }

        /* Command => !nyan as nyaa for @MooJieJie floor 10 */
        if (event.getMessage().getContentRaw().contains("for")) {
            Member member = event.getMessage().getMentionedMembers().get(0); //TODO: ensure it has at least 1 member

            if (event.getMessage().getContentRaw().contains("floor")) {
                UserAccount ua = Data.currentUserList.getByUserKey(member.getIdLong(), member.getEffectiveName());
                if (ua == null) {
                    event.getChannel().sendMessage(member.getAsMention() + " is not registered.").queue();
                } else {
                    ua.setFloor(Integer.parseInt(event.getMessage().getContentRaw().split(" ")[6]));
                    event.getChannel().sendMessage("Updated. -> " + ua.toString()).queue();
                }
                return;
            }
            return;
        }
    }
}
