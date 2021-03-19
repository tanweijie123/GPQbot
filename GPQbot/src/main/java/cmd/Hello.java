package cmd;

import data.Data;
import model.UserAccount;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.tuple.MutablePair;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Hello extends ListenerAdapter {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String BOT_PREFIX = "!nyan";

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot())
            return;

        if (e.getMessage().getContentRaw().startsWith(BOT_PREFIX)) {
            //e.getChannel().sendMessage("Link => " + e.getMessage().getJumpUrl()).queue();
            processRequest(e);
        }
    }

    public void processRequest(MessageReceivedEvent e) {

        String toProcess = String.format("%s(%s) => %s", e.getMember().getEffectiveName(), e.getAuthor().getAsTag() , e.getMessage().getContentRaw());
        LOGGER.log(Level.INFO, toProcess);
        System.out.println(toProcess);
        String[] recv = e.getMessage().getContentRaw().split(" ");
        if (recv.length > 1) {

            if (recv[1].equalsIgnoreCase("help")) {

                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("NyanBot Help");
                eb.setColor(java.awt.Color.CYAN);
                eb.addBlankField(true);
                eb.appendDescription("These are the commands for NyanBot nyaaa~\n");
                eb.addField("new", "create a new GPQ registration (only for mods)", false);
                eb.addField("close", "close the current GPQ registration (only for mods)", false);
                eb.addField("reg", "send the link to the post of the current registration", false);
                eb.addField("setup", "register your discord with NyanBot", false);
                eb.addField("whoami", "get what NyanBot knows about you.", false);
                eb.addField("floor", "update your floor", false);
                eb.addBlankField(false);
                eb.setFooter("How do you use it? Simply send \"" + BOT_PREFIX + " <command>\" and you can talk to me :)");

                e.getChannel().sendMessage(eb.build()).queue();

            } else if (recv[1].equalsIgnoreCase("new")) {

                Optional permission = e.getMember().getRoles().stream().filter(x -> x.getName().equals("Tokyo") || x.getName().equals("Professor")).findFirst();

                permission.ifPresentOrElse(
                        x -> {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setTitle("Register for GPQ Day!");
                            eb.setColor(java.awt.Color.GREEN);
                            eb.setFooter("This request is made by " + e.getMember().getEffectiveName() + " on " + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                            Message msg = e.getChannel().sendMessage(eb.build()).complete();
                            msg.addReaction("\uD83C\uDDF2").queue();
                            msg.addReaction("\uD83C\uDDF9").queue();
                            msg.addReaction("\uD83C\uDDFC").queue();
                            msg.addReaction("\uD83C\uDDED").queue();
                            msg.addReaction("\uD83C\uDDEB").queue();
                            msg.addReaction("\uD83C\uDDF8").queue();
                            msg.addReaction("\uD83D\uDE34").queue();
                            msg.pin().queue();

                            try {
                                MutablePair<Long, URL> p = new MutablePair<>(msg.getGuild().getIdLong(), new URL(msg.getJumpUrl()));
                                Data.currentGPQList.add(p);
                            } catch (MalformedURLException malformedURLException) {
                                System.err.println("Unable to convert " + msg.getJumpUrl() + "into URL and store into GPQ List");
                            }

                        },
                        () -> {
                            e.getChannel().sendMessage("You do not have the permission to perform this...").queue();
                        }
                );

            } else if (recv[1].equalsIgnoreCase("close")) {
                String ret = Data.currentGPQList.getByGuildKey(e.getGuild().getIdLong());
                if (ret == null) {
                    e.getChannel().sendMessage("No opened registration to close").queue();
                    return;
                }

                Optional permission = e.getMember().getRoles().stream().filter(x -> x.getName().equals("Tokyo") || x.getName().equals("Professor")).findFirst();

                permission.ifPresentOrElse(
                        x -> {
                            Message msg = e.getChannel().sendMessage("Closing registration for " + ret + " and getting participants for today (" + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("EEEE")) + ")?").complete();
                            msg.addReaction("U+2705").queue();
                            msg.addReaction("U+274C").queue();
                        },
                        () -> {
                            e.getChannel().sendMessage("You do not have the permission to perform this...").queue();
                        }
                );
                return;
            } else if (recv[1].equalsIgnoreCase("reg")) {
                String ret = Data.currentGPQList.getByGuildKey(e.getGuild().getIdLong());
                if (ret == null) {
                    e.getChannel().sendMessage("There isn't a GPQ available to register!").queue();
                } else {
                    e.getChannel().sendMessage("The latest GPQ registration post is here! \n" + ret).queue();
                }
            } else if (recv[1].equalsIgnoreCase("setup")) {
                long userID = e.getAuthor().getIdLong();
                UserAccount ua = new UserAccount(userID);
                Data.currentUserList.add(new MutablePair<>(userID, ua));
                ua.setIgn(e.getMember().getEffectiveName());
                e.getChannel().sendMessage("Hellonyaa~ This is what I have of you:\n" + ua.toString()).queue();
            } else if (recv[1].equalsIgnoreCase("whoami")) {
                long userID = e.getAuthor().getIdLong();
                UserAccount ua = Data.currentUserList.getByUserKey(userID, e.getMember().getEffectiveName());

                if (ua == null) {
                    e.getChannel().sendMessage("Have I seen you before? " +
                            "Try \"" + BOT_PREFIX + " setup\" if this is the first time you're talking to me nyaa~\n").queue();
                    return;
                }

                e.getChannel().sendMessage("Hellonyaa~ This is what I have of you:\n" + ua.toString()).queue();

            } else if (recv[1].equalsIgnoreCase("floor")) {
                if (recv.length < 2) {
                    e.getChannel().sendMessage("Huhh? Where is your floor? (Eg. \"" + BOT_PREFIX +" floor 50\"").queue();
                }

                long userID = e.getAuthor().getIdLong();
                UserAccount ua = Data.currentUserList.getByUserKey(userID, e.getMember().getEffectiveName());

                if (ua == null) {
                    e.getChannel().sendMessage("Have I seen you before? " +
                            "Try \"" + BOT_PREFIX + " setup\" if this is the first time you're talking to me nyaa~\n").queue();
                } else {
                    try {
                        int floor = Integer.parseInt(recv[2]);
                        if (floor < 0) {
                            e.getChannel().sendMessage("Oof.. You can't even beat a snail?").queue();
                        } else if (floor > 70) {
                            e.getChannel().sendMessage("Woahh.. You are strong. Too strong in fact.").queue();
                        } else {
                            ua.setFloor(floor);
                            e.getChannel().sendMessage("Hellonyaa~ This is what I have of you:\n" + ua.toString()).queue();
                        }
                    } catch (NumberFormatException ev) {
                        e.getChannel().sendMessage("Huhh? Your floor is not a number? Idk how to use it nyaa (Eg. \"" + BOT_PREFIX + " floor 50\")").queue();
                    }
                }
            } else {
                e.getChannel().sendMessage("nyaa? I have never heard of this before :X").queue();
            }
        } else if (recv.length == 1) {
            e.getChannel().sendMessage("nyaaa~").queue();
        }
    }

    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        if (event.getUser().isBot())
            return;
        byte[] check = new byte[] {-30, -100, -123};
        byte[] cross = new byte[] {-30, -99, -116};

        Message reactedMsg = event.getChannel().getHistoryAround(event.getMessageId(),1).complete().getRetrievedHistory().get(0);

        if (reactedMsg.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) { //user reacted to message sent by this bot

            if (reactedMsg.getContentRaw().startsWith("Closing registration for ")) {

                if (Arrays.equals(event.getReactionEmote().getAsReactionCode().getBytes(StandardCharsets.UTF_8), check)) {

                    Optional permission = event.getMember().getRoles().stream().filter(x -> x.getName().equals("Tokyo") || x.getName().equals("Professor")).findFirst();

                    permission.ifPresent(
                            x -> {
                                event.getChannel().sendMessage("Confirmed GPQ at " + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss (EEE)"))).queue();

                                String creationMsgId = Data.currentGPQList.getByGuildKey(event.getGuild().getIdLong());
                                creationMsgId = creationMsgId.substring(creationMsgId.lastIndexOf("/") + 1);

                                //TODO: retrieve current from currentGPQ instead
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
