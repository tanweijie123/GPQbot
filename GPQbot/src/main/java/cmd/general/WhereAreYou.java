package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class WhereAreYou extends Command {

    public WhereAreYou() {
        super.name = "whereareyou";
        super.help = "Check if bot is online";
        super.aliases = new String[]{"wru"};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("nyaaa~");
    }
}
