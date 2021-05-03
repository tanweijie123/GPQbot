package event;

import config.Settings;
import data.Data;
import data.JobList;
import data.PastGPQList;
import logic.GuildMethod;
import logic.UsersMethod;
import model.GPQParticipation;
import model.UserAccount;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReactionEvent extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (event.getUser().isBot())
            return;
        byte[] check = new byte[] {-30, -100, -123};
        byte[] cross = new byte[] {-30, -99, -116};

        Message reactedMsg = event.getChannel().getHistoryAround(event.getMessageId(),1).complete().getRetrievedHistory().get(0);

        if (reactedMsg.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) { //user reacted to message sent by this bot

            if (reactedMsg.getContentRaw().startsWith("Closing registration for ")) {

                Optional permission = event.getMember().getRoles().stream().filter(x -> x.getName().equalsIgnoreCase(Settings.BOT_MOD_NAME)).findFirst();

                if (!permission.isPresent())
                    return;

                if (Arrays.equals(event.getReactionEmote().getAsReactionCode().getBytes(StandardCharsets.UTF_8), check)) {

                    event.getChannel().sendMessage("Confirmed GPQ at " + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss (EEE)"))).queue();

                    String[] creationMsg = GuildMethod.getCurrentGPQLink(event.getGuild().getId()).split("/");

                    //TODO: check if existingReg is manually deleted.
                    Message actualEb = event.getGuild().getTextChannelById(creationMsg[5]).getHistoryAround(creationMsg[6], 1).complete().getRetrievedHistory().get(0);
                    if (actualEb.isPinned())
                        actualEb.unpin().queue();

                    GuildMethod.deleteCurrentGPQLink(event.getGuild().getId());  //once finalised, it will be removed from current.

                    List<MessageReaction> ebReact = actualEb.getReactions();

                    HashMap<String, String> idToName = new HashMap<>();
                    List<String> usrAttending = ebReact.get(0).retrieveUsers().stream().filter(x -> !x.isBot())
                            .peek(x -> idToName.put(x.getId(), event.getGuild().getMemberById(x.getId()).getEffectiveName()))
                            .map(x -> x.getId())
                            .collect(Collectors.toList());

                    List<UserAccount> uaList = UsersMethod.getUsers(event.getGuild().getId(), usrAttending);
                    uaList.sort( (x,y) -> Integer.compare(y.getFloor(), x.getFloor()) );

                    String reply = "Participants (" + uaList.size()+ "): \n";
                    for (int i = 0; i < uaList.size(); i++) {
                        //TODO; optimise getEffectiveName
                        reply += String.format("%d. %s\n", i+1, uaList.get(i).gpqString(idToName.get(uaList.get(i))));
                    }
                    event.getChannel().sendMessage(reply).queue();

                    GuildMethod.insertGpqConfirmation(event.getGuild().getId(), uaList);

                    //TODO: send excel output
                    //event.getUser().openPrivateChannel().flatMap(hi -> hi.sendMessage("Hello~")).queue();
                }

                reactedMsg.delete().queue();
            }
        }
    }

}
