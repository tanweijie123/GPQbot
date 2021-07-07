package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import event.SlashCommand;
import logic.UsersMethod;
import model.UserAccount;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

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

    public void execute(SlashCommandEvent event) {
        if (event.getMember().getUser().isBot())
            return;

        UserAccount ua = UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getUser().getId());
        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.replyString(event.getMember().getEffectiveName())).setEphemeral(true).queue();
    }
}
