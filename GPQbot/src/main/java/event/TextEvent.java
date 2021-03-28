package event;

import config.Settings;
import data.Data;
import model.UserAccount;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TextEvent extends ListenerAdapter {

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

        /* Command => !nyan as mama show regalldays [limit] */
        if (event.getMessage().getContentRaw().contains("show regalldays")) {
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

            for (int i = 0; i < 7; i++) { //hardcode at sunday (6)
                List<User> usrList = ebReact.get(i).retrieveUsers().stream().filter(x -> !x.isBot()).collect(Collectors.toList());

                reply += String.format("For %s, Total Participants = %d \n", DayOfWeek.of(i + 1).name(), usrList.size());

                List<UserAccount> usrForDay = usrList.stream()
                        .map(x -> Data.currentUserList.getByUserKey(x.getIdLong(), null)) //set null so no need to check effective name too many times
                        .sorted(Comparator.comparingInt(UserAccount::getFloor))
                        .collect(Collectors.toList());

                int thisLimit = Math.min(limit, usrForDay.size());

                for (int j = 0; j < thisLimit; j++) {
                    reply += String.format("%d. %s\n", j+1, usrForDay.get(j).toString());
                }

                if (thisLimit < usrForDay.size()) {
                    reply += "...\n";
                }

                reply += "\n\n";

            }

            event.getMessage().reply(reply).queue();
        }
    }
}
