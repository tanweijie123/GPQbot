package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class WhereAreYou extends Command {

    public WhereAreYou() {
        super.name = "wru";
        super.help = "Check if bot is online";
        super.aliases = new String[]{"whereareyou"};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("nyaaa~");
    }
}
