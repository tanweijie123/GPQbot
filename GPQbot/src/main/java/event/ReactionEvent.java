package event;

import config.Settings;
import excelgen.Generator;
import logic.GuildMethod;
import logic.RulesMethod;
import logic.UsersMethod;
import model.UserAccount;
import model.UserAccountExport;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.Utility;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

                    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+8"));
                    event.getChannel().sendMessage("Confirmed GPQ at " + Utility.toDateTimeString(now)).queue();

                    Message actualEb = Utility.convertStringToDiscordMessage(event.getJDA(), GuildMethod.getCurrentGPQLink(event.getGuild().getId()));
                    if (actualEb == null) {
                        //Message may have been deleted, or not found from database.
                        return;
                    }

                    if (actualEb.isPinned())
                        actualEb.unpin().queue();

                    List<MessageReaction> ebReact = actualEb.getReactions();

                    List<String> usrAttending = ebReact.get(0).retrieveUsers().stream().filter(x -> event.getGuild().getMember(x) != null && !x.isBot())
                            .map(x -> x.getId())
                            .collect(Collectors.toList());

                    //TODO: remove isBot
                    GuildMethod.getAppendedMembersToCurrent(event.getGuild().getId())
                            .stream().forEach(x -> {
                                if (!usrAttending.contains(x)) {
                                    usrAttending.add(x);
                                }
                    } );

                    List<UserAccount> uaList = UsersMethod.getUsers(event.getGuild().getId(), usrAttending);
                    uaList.sort( (x,y) -> Integer.compare(y.getFloor(), x.getFloor()) );


                    List<UserAccountExport> uaeList = new ArrayList<>();
                    StringBuilder sbReply = new StringBuilder("Participants (" + uaList.size()+ "): \n");
                    for (int i = 0; i < uaList.size(); i++) {
                        UserAccount ua = uaList.get(i);
                        String ign = event.getGuild().getMemberById(uaList.get(i).getUserId()).getEffectiveName();

                        sbReply.append(String.format("%d. %s\n", i+1, ua.gpqString(ign)));
                        uaeList.add(new UserAccountExport(ua.getGuildId(), ua.getUserId(), ua.getJob(), ua.getFloor(), ua.isRegistered(), ign));
                    }

                    //create a new thread to generate the excel file;
                    Thread t = new Thread(() -> {
                        File generatedExcel = new File("excelgen/" + event.getGuild().getId() + "_" + Utility.toDateTimeStringFileUsage(now) + ".xlsx");
                        try {
                            generatedExcel.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Generator gen = new Generator(uaeList, RulesMethod.getRules(event.getGuild().getId()), generatedExcel);
                        gen.generate();

                        //send the generated excel file to user's dm.
                        event.getUser().openPrivateChannel().flatMap(hi ->
                                hi.sendMessage("This is the generated excel file for your gpq.\n")
                                        .addFile(generatedExcel)).queue();

                    });
                    t.start();

                    //while generating new file, send the list of participants, insert into db, and delete reaction.
                    String reply = sbReply.toString();
                    event.getChannel().sendMessage(reply).queue();
                    GuildMethod.insertGpqConfirmation(event.getGuild().getId(), uaList, now);

                    //clean up -> delete GpqCurrent record from db
                    GuildMethod.deleteCurrentGPQLink(event.getGuild().getId());
                }

                reactedMsg.delete().queue();
            }
        }
    }

}
