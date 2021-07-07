import cmd.Help;
import cmd.general.*;
import cmd.mod.*;
import com.jagrosh.jdautilities.command.*;
import config.Settings;
import event.NotesEvent;
import event.ReactionEvent;
import event.SlashCommand;
import event.TextEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import scheduler.Scheduler;
import util.Utility;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class App {

    public static void main(String[] args) {

        FileHandler fh;

        try {
            File file = new File("log/" + Utility.toDateTimeStringFileUsage(ZonedDateTime.now()) + ".log");
            file.createNewFile();
            fh = new FileHandler(file.getPath());
            Settings.LOGGER.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            Settings.LOGGER.log(Level.INFO, "System booting up . . .");

            Scanner sc = new Scanner(new File("token"));
            String token = sc.next();
            JDA jda = JDABuilder.create(token,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_EMOJIS
            ).build();

            CommandClientBuilder builder = new CommandClientBuilder();
            builder.setOwnerId("419674934072180736");
            builder.setPrefix("!nyan ");
            builder.setActivity(Activity.playing("with Moo"));
            builder.setHelpWord("help");
            builder.setHelpConsumer(x -> new Help().run(x));
            builder.setEmojis(":white_check_mark:", ":warning:", ":bangbang:");

            builder.setListener(new CommandListener() {
                @Override
                public void onNonCommandMessage(MessageReceivedEvent event) {
                    /*
                    if (event.getMessage().getContentRaw().equalsIgnoreCase("!nyan setup flagalert")) {
                        event.getChannel().sendMessage("React to the clock for opt-in for flag alerts!").queue(x -> {
                            x.addReaction("U+1F55B").queue();
                            x.addReaction("U+1F556").queue();
                            x.addReaction("U+1F558").queue();
                        });
                        event.getMessage().delete().queue();
                        return;
                    }
                     */
                    if (event.getMessage().getContentRaw().startsWith("!nyan ")) {
                        event.getMessage().reply("https://youtu.be/dQw4w9WgXcQ").queue();
                        Settings.LOGGER.info("[nonCommand] " + event.getAuthor().getAsTag() + "->" + event.getMessage().getContentRaw());
                    }
                }

                @Override
                public void onCompletedCommand(CommandEvent event, Command command) {
                    if (event.getChannelType() == ChannelType.PRIVATE) {
                        Settings.LOGGER.info("[Success][PM] " + event.getAuthor().getAsTag() + "->" + command.getName());
                    } else {
                        String toProcess = String.format("%s(%s) => %s", event.getMember().getEffectiveName(), event.getAuthor().getAsTag() , event.getMessage().getContentRaw());
                        Settings.LOGGER.info("[Success][GP] " + toProcess);
                    }
                }

                @Override
                public void onCommandException(CommandEvent event, Command command, Throwable throwable) {
                    Settings.LOGGER.severe("Exception occured!\n" + event.getAuthor().getAsTag() + "->" + command.getName() + "\n" +
                            throwable.toString() + "\n" +
                            Arrays.toString(throwable.getStackTrace()));
                }
            });


            addCommands(builder);
            CommandClient commandClient = builder.build();
            Settings.botCommand = commandClient;
            jda.addEventListener(commandClient);

            CommandListUpdateAction commands = jda.updateCommands();
            addSlashCommands(commands);

            jda.addEventListener(new ReactionEvent(), new TextEvent(), new NotesEvent(), new SlashCommand());


            Settings.LOGGER.log(Level.INFO, "Bot is ready and awaiting. . .");
            jda.awaitReady();

            //initialise scheduler
            Settings.LOGGER.log(Level.INFO, "Initialising scheduler");
            Scheduler.init(jda);
            Settings.LOGGER.log(Level.INFO, "Initialised scheduler");


        } catch (Exception e) {
            System.out.println("Bot Token Failed!");
            e.printStackTrace();
            return;
        }
    }

    private static void addCommands(CommandClientBuilder builder) {
        builder.addCommand(new WhereAreYou());
        builder.addCommands(new WhoAreYou());
        builder.addCommand(new WhoAmI());
        builder.addCommand(new WhoIs());
        builder.addCommand(new SetJob());
        builder.addCommand(new SetFloor());
        builder.addCommand(new CreateReminder());

        builder.addCommand(new CurrentRegistration());
        builder.addCommand(new GetJobList());
        builder.addCommands(new GPQRanking());

        builder.addCommand(new NewRegistration());
        builder.addCommands(new CloseRegistration());
        builder.addCommand(new AddMembers());

        builder.addCommands(new AsMod());
        builder.addCommand(new PurgeTo());
    }

    private static void addSlashCommands(CommandListUpdateAction commands) {
        commands.addCommands(
                new CommandData("help", "Gets the user guide for Nyanbot"),
                new CommandData("whoami", "Gets the job and floor currently stored in Nyanbot"),
                new CommandData("floor", "updates the floor of your character")
                    .addOption(OptionType.INTEGER, "floor_number", "your dojo floor", true),
                new CommandData("job", "updates the job of your character")
                    .addOption(OptionType.INTEGER, "job_number", "your job number as described in joblist", true),
                new CommandData("joblist", "Gets the list of jobs")
        );

        commands.queue(); //required to update all added slash commands here.
    }
}
