package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import logic.UsersMethod;
import model.UserAccount;

public class WhoAmI extends Command {

    public WhoAmI() {
        super.name = "whoami";
        super.help = "Gets what NyanBot knows about you";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMember().getUser().isBot())
            return;

        UserAccount ua = UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getAuthor().getId());
        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.replyString(event.getMember().getEffectiveName()));

    }
}
