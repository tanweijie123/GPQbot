package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

            event.replyWarning(msgHistory.size() + " pending delete. " +
                    "Deletion will start in 10 seconds. If you had made a mistake, delete your `!nyan purgeto` command. ");

            try {
                TimeUnit.SECONDS.sleep(10);
                event.getChannel().retrieveMessageById(msgCommand.getIdLong()).complete();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ErrorResponseException e) {
                event.reply("purge cancelled");
                return;
            }

            //bulk delete msg < 2 weeks old, 2 <= size <= 100, manual delete otherwise.
            OffsetDateTime twoWeeksOld = OffsetDateTime.now().minusWeeks(2);
            Map<Boolean, List<Message>> resultMap = msgHistory.stream().collect(Collectors.partitioningBy(x -> x.getTimeCreated().isAfter(twoWeeksOld)));

            if (resultMap.get(true).size() > 1) {
                event.getTextChannel().deleteMessages(resultMap.get(true)).queue();
            } else if (resultMap.get(true).size() == 1) {
                resultMap.get(true).get(0).delete().queue();
            }

            List<Message> falseList = resultMap.get(false);

            for (int i = 0; i < falseList.size(); i++) {
                falseList.get(i).delete().queue();
            }

            msgFrom.delete().queue(x -> {
                event.replySuccess("Purge completed. " + msgHistory.size() + " messages deleted.");
            });

        });
        t.start();

    }

}
