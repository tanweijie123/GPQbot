import Storage.JsonAdapter;
import cmd.Hello;
import logic.Data;
import model.CurrentGPQList;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        try {
            Data.currentGPQList = JsonAdapter.loadCurrentGPQList().orElse(new CurrentGPQList());

            Scanner sc = new Scanner(new File("token"));
            String token = sc.nextLine();
            JDA jda = JDABuilder.createDefault(token).build();
            jda.addEventListener(new Hello());

        } catch (Exception e) {
            System.out.println("Bot Token Failed!");
            e.printStackTrace();
            return;
        }
    }
}
