import cmd.general.*;
import cmd.mod.AsMod;
import cmd.mod.CloseRegistration;
import cmd.mod.NewRegistration;
import com.jagrosh.jdautilities.command.*;
import config.Settings;
import event.ReactionEvent;
import event.TextEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.requests.GatewayIntent;

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
            File file = new File("log/" + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss")) + ".log");
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
            String token = sc.nextLine();
            JDA jda = JDABuilder.create(token,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS).build();

            CommandClientBuilder builder = new CommandClientBuilder();
            builder.setOwnerId("419674934072180736");
            builder.setPrefix("!nyan ");
            builder.setActivity(Activity.playing("with Moo"));
            builder.setHelpWord("help");

            addCommands(builder);

            builder.setListener(new CommandListener() {
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

            jda.addEventListener(new ReactionEvent());
            jda.addEventListener(new TextEvent());

            CommandClient commandClient = builder.build();
            Settings.botCommand = commandClient;
            jda.addEventListener(commandClient);

            Settings.LOGGER.log(Level.INFO, "Bot is ready and awaiting. . .");
            jda.awaitReady();

            //exportDataToSql();

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

        builder.addCommand(new CurrentRegistration());
        builder.addCommand(new GetJobList());
        builder.addCommands(new GPQRanking());

        builder.addCommand(new NewRegistration());
        builder.addCommands(new CloseRegistration());

        builder.addCommands(new AsMod());
    }

    /*
    private static void exportDataToSql() {
        System.out.println("INSERT INTO Guilds\nVALUES(705486807441211442);");

        System.out.println("INSERT INTO Users\nVALUES");
        Data.currentUserList.currentList.entrySet().stream().forEach(x -> System.out.println("(705486807441211442," + x.getKey() + "," + x.getValue().getJob() + "," + x.getValue().getFloor() + "),"));

        System.out.println("INSERT INTO GpqCurrent(gid, link)\nVALUES");
        Data.currentGPQList.currentList.entrySet().stream().forEach(x -> System.out.println("(" + x.getKey() + ", '" + x.getValue().toString() + "'),"));

        System.out.println("INSERT INTO GpqConfirmed\nVALUES");
        Data.pastGPQList.historyMap.entrySet().stream().forEach(x ->
                x.getValue().stream().forEach(y ->

                System.out.println("(" + x.getKey() + ", '" + y.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'," + y.getScore() + "),")
                )
        );

        System.out.println("INSERT INTO GpqParticipants\nVALUES");
        Data.pastGPQList.historyMap.entrySet().stream().forEach(x ->
                x.getValue().stream().forEach(y ->
                    y.getParticipantList().stream().forEach(z ->
                        System.out.println("(" + x.getKey() + ", '" + y.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "'," + z.getUserId() + "),")
                    )
                )
        );

        System.out.println("~EOF~");
    }

     */
}
