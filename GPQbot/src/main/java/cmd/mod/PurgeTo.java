package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PurgeTo extends Command {

    public PurgeTo() {
        super.name = "purgeto";
        super.help = "purge messages (max:100) from the current channel until the referenced message (only mods)";
        super.requiredRole = Settings.BOT_MOD_NAME;
    }

    @Override
    protected void execute(CommandEvent event) {
        Thread t = new Thread(() -> {
            Message msgFrom = event.getMessage().getReferencedMessage();
            Message msgCommand = event.getMessage();
            List<Message> msgHistory = event.getChannel().getHistoryAfter(msgFrom, 100).complete().getRetrievedHistory(); //max limit = 100

            String size = String.valueOf(msgHistory.size());

            event.replyWarning(size + " pending delete. " +
                    "Deletion will start in 5 seconds. If you had made a mistake, delete your `!nyan purgeto` command. ");

            try {
                TimeUnit.SECONDS.sleep(5);
                event.getChannel().retrieveMessageById(msgCommand.getIdLong()).complete();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ErrorResponseException e) {
                event.reply("purge cancelled");
                return;
            }

            for (int i = 0; i < msgHistory.size(); i++) {
                msgHistory.get(i).delete().queue();
            }

            msgFrom.delete().queue(x -> {
                event.replySuccess("Purge completed. " + msgHistory.size() + " messages deleted.");
            });

        });
        t.start();

    }

}
