package event;

import config.Settings;
import data.Data;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

                if (Arrays.equals(event.getReactionEmote().getAsReactionCode().getBytes(StandardCharsets.UTF_8), check)) {

                    Optional permission = event.getMember().getRoles().stream().filter(x -> x.getName().equalsIgnoreCase(Settings.BOT_MOD_NAME)).findFirst();

                    permission.ifPresent(
                            x -> {
                                event.getChannel().sendMessage("Confirmed GPQ at " + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss (EEE)"))).queue();

                                String creationMsgId = Data.currentGPQList.getByGuildKey(event.getGuild().getIdLong());
                                creationMsgId = creationMsgId.substring(creationMsgId.lastIndexOf("/") + 1);

                                Message actualEb = event.getChannel().getHistoryAround(creationMsgId, 1).complete().getRetrievedHistory().get(0);
                                if (actualEb.isPinned())
                                    actualEb.unpin().queue();

                                Data.currentGPQList.remove(event.getGuild().getIdLong()); //once finalised, it will be removed from current.

                                List<MessageReaction> ebReact = actualEb.getReactions();

                                int choice = ZonedDateTime.now(ZoneId.of("GMT+8")).getDayOfWeek().getValue() - 1;
                                List<User> usrList = ebReact.get(choice).retrieveUsers().stream().collect(Collectors.toList());

                                String allName = "Participants (" + (usrList.size() - 1) + "): \n";
                                for (User u : usrList) {
                                    if (!u.getName().equals(event.getJDA().getSelfUser().getName())) {
                                        UserAccount ua = Data.currentUserList.getByUserKey(u.getIdLong(), event.getGuild().getMember(u).getEffectiveName());
                                        if (ua == null) {
                                            allName += event.getGuild().getMember(u).getEffectiveName() + "\n";
                                        } else {
                                            if (ua.getFloor() > 0) {
                                                allName += ua.getIgn() + "/" + ua.getFloor() + "\n";
                                            } else {
                                                allName += ua.getIgn() + "\n";
                                            }
                                        }
                                    }
                                }
                                event.getChannel().sendMessage(allName).queue();

                                //TODO: send excel output
                                //event.getUser().openPrivateChannel().flatMap(hi -> hi.sendMessage("Hello~")).queue();
                            }
                    );

                }

                reactedMsg.delete().queue();
            }
        }
    }

}
