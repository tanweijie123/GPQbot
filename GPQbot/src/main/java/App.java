import cmd.Hello;
import data.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class App {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args) {

        FileHandler fh;

        try {
            File file = new File("log/" + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss")) + ".log");
            file.createNewFile();
            fh = new FileHandler(file.getPath());
            LOGGER.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {

            Data.init();
            LOGGER.log(Level.INFO, "Data init complete");

            Scanner sc = new Scanner(new File("token"));
            String token = sc.nextLine();
            JDA jda = JDABuilder.createDefault(token).build();
            jda.addEventListener(new Hello());

            LOGGER.log(Level.INFO, "Bot is ready and awaiting. . .");
            jda.awaitReady();

        } catch (Exception e) {
            System.out.println("Bot Token Failed!");
            e.printStackTrace();
            return;
        }
    }
}
