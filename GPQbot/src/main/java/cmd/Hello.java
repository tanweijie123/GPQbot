package cmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Hello extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot())
            return;
        String msg = e.getMessage().getContentRaw();
        System.out.printf("%s(%s) => %s\n", e.getGuild().getMember(e.getAuthor()).getNickname(), e.getAuthor().getAsTag() , msg);
        if (msg.startsWith("!nyan")) {
            e.getChannel().sendMessage("nyaaa~").queue();
            processRequest(e);
        }
    }

    public void processRequest(MessageReceivedEvent e) {
        String[] recv = e.getMessage().getContentRaw().split(" ");
        if (recv.length > 1) {
            if (recv[1].equalsIgnoreCase("new")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Register for GPQ Day!");
                eb.setColor(java.awt.Color.GREEN);
                eb.setFooter("This request is made by " + e.getGuild().getMember(e.getAuthor()).getNickname() + " on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                Message msg = e.getChannel().sendMessage(eb.build()).complete();
                msg.addReaction("\uD83C\uDDF2").queue();
                msg.addReaction("\uD83C\uDDF9").queue();
                msg.addReaction("\uD83C\uDDFC").queue();
                msg.addReaction("\uD83C\uDDED").queue();
                msg.addReaction("\uD83C\uDDEB").queue();
                msg.addReaction("\uD83C\uDDF8").queue();
                msg.addReaction("\uD83D\uDE34").queue();
                msg.addReaction("U+2705").queue();
                msg.pin().queue();

            } else if (recv[1].equalsIgnoreCase("update")) {
                e.getChannel().sendMessage("Update the form here~ \n https://forms.gle/3Z4hXitJRWi7hmwW9").queue();
            }
        }
    }

    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (event.getUser().isBot())
            return;
        byte[] check = new byte[] {-30, -100, -123};
        byte[] cross = new byte[] {-30, -99, -116};

        Message reactedMsg = event.getChannel().getHistoryAround(event.getMessageId(),1).complete().getRetrievedHistory().get(0);

        if (reactedMsg.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) { //user reacted to message sent by this bot

            if (reactedMsg.getEmbeds().size() > 0 && reactedMsg.getEmbeds().get(0).getTitle().equals("Register for GPQ Day!")) {

                if (Arrays.equals(event.getReactionEmote().getAsReactionCode().getBytes(StandardCharsets.UTF_8), check)) {
                   //reacted to Register message
                    Message msg = event.getChannel().sendMessage("Finalising? " + event.getMessageId()).complete();
                    msg.addReaction("U+2705").queue();
                    msg.addReaction("U+274C").queue();
                }
            } else if (reactedMsg.getContentRaw().startsWith("Finalising?")) {

                if (Arrays.equals(event.getReactionEmote().getAsReactionCode().getBytes(StandardCharsets.UTF_8), check)) {
                    event.getChannel().sendMessage("Confirmed GPQ at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss (EEE)"))).queue();

                    Message actualEb = event.getChannel().getHistoryAround(reactedMsg.getContentRaw().split(" ")[1],1).complete().getRetrievedHistory().get(0);
                    if (actualEb.isPinned())
                        actualEb.unpin().queue();

                    List<MessageReaction> ebReact = actualEb.getReactions();

                    int choice = LocalDateTime.now().getDayOfWeek().getValue() - 1;
                    List<User> usrList = ebReact.get(choice).retrieveUsers().stream().collect(Collectors.toList());

                    String allName = "Participants (" + (usrList.size() - 1) + "): \n";
                    for (User u : usrList) {
                        if (!u.getName().equals(event.getJDA().getSelfUser().getName()))
                            allName += event.getGuild().getMember(u).getNickname() + "\n";
                    }
                    event.getChannel().sendMessage(allName).queue();

                }

                reactedMsg.delete().queue();
            }

        }
    }

}
