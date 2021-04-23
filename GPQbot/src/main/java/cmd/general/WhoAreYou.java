package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;

public class WhoAreYou extends Command {

    public WhoAreYou() {
        super.name = "whoareyou";
        super.aliases = new String[]{"whoru", "whoareu"};
    }

    @Override
    protected void execute(CommandEvent event) {
        String reply = String.format("Hellonyaa ~ \nI am %s, currently running on version %s\nMy mama is %s. If I am acting weird, feel free to find her :smile:",
                event.getGuild().getMember(event.getJDA().getSelfUser()).getEffectiveName(),
                Settings.VERSION,
                "NyanBreath"
                );

        event.getMessage().reply(reply).queue();
    }
}
