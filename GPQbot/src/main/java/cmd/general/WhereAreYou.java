package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

import java.time.temporal.ChronoUnit;

public class WhereAreYou extends Command {

    public WhereAreYou() {
        super.name = "whereareyou";
        super.help = "Check if bot is online";
        super.aliases = new String[]{"wru"};
    }

    @Override
    protected void execute(CommandEvent event) {
        Message msg = event.getEvent().getChannel().sendMessage("nyaaa~").complete();
        long ping = ChronoUnit.MILLIS.between( event.getMessage().getTimeCreated(), msg.getTimeCreated() );
        event.reply("ping: " + ping + "ms");
    }
}

