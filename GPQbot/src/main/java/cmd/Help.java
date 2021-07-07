package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Help extends Command {

    public Help() {
        super.name = "help";
        super.help = "Get the help website";
        super.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply("Do check out the user guide here! https://nyanbot.link/ug/");
    }

    public void execute(SlashCommandEvent event) {
        event.reply("Do check out the user guide here! https://nyanbot.link/ug/").setEphemeral(true).queue();
    }
}
