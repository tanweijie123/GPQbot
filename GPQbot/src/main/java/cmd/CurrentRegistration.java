package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import data.Data;

public class CurrentRegistration extends Command {

    public CurrentRegistration() {
        super.name = "reg";
        super.help = "Get the link to the post of the current registration";
    }

    @Override
    protected void execute(CommandEvent event) {
        String ret = Data.currentGPQList.getByGuildKey(event.getGuild().getIdLong());
        if (ret == null) {
            event.reply("There isn't a GPQ available to register!");
        } else {
            event.reply("The latest GPQ registration post is here! \n" + ret);
        }
    }
}
