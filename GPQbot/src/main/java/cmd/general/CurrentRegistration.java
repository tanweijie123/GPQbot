package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import logic.GuildMethod;
import net.dv8tion.jda.api.entities.Message;

public class CurrentRegistration extends Command {

    public CurrentRegistration() {
        super.name = "reg";
        super.help = "Get the link to the post of the current registration";
    }

    @Override
    protected void execute(CommandEvent event) {

        String url = GuildMethod.getCurrentGPQLink(event.getGuild().getId());
        if (url == null) {
            event.reply("There isn't a GPQ available to register!");
        } else {
            String[] retSplit = url.split("/");
            if (event.getChannel().getIdLong() == Long.parseLong(retSplit[5])) {
                Message msg = event.getChannel().getHistoryAround(retSplit[6], 1).complete().getRetrievedHistory().get(0);
                msg.reply("Here is the latest GPQ registration post!").queue();
            } else
                event.reply("The latest GPQ registration post is here! \n" + url);
        }
    }
}
