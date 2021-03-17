import cmd.Hello;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class App {
    public static void main(String[] args) {
        try {
            JDA jda = JDABuilder.createDefault("ODIxNjMzNzc5NDI4ODg0NTAx.YFGkFg.SH8eJW3oPtmlEjwgj3DN4F3ZPpk").build();
            jda.addEventListener(new Hello());
        } catch (Exception e) {
            System.out.println("Bot Token Failed!");
            e.printStackTrace();
            return;
        }
    }
}
