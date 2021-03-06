package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import logic.GuildMethod;
import net.dv8tion.jda.api.entities.Message;

public class CloseRegistration extends Command {

    public CloseRegistration() {
        super.name = "close";
        super.help = "Close the current GPQ registration; force will delete the registration (only mods)";
        super.arguments = "[force]";
        super.requiredRole = Settings.BOT_MOD_NAME;
    }

    @Override
    protected void execute(CommandEvent event) {
        String anyExistingReg = GuildMethod.getCurrentGPQLink(event.getGuild().getId());

        if (anyExistingReg == null) {
            event.reply("No opened registration to close");
            return;
        }

        String[] retSplit = anyExistingReg.split("/");

        if (event.getArgs().equals("force")) {
            GuildMethod.deleteCurrentGPQLink(event.getGuild().getId());
            Message actualEb = event.getGuild().getTextChannelById(retSplit[5]).getHistoryAround(retSplit[6], 1).complete().getRetrievedHistory().get(0);
            if (actualEb.isPinned())
                actualEb.unpin().queue();

            event.reply("You can now create a new GPQ registration.");
            return;
        }

        //TODO: check if existingReg is manually deleted.
        Message msg;

        if (event.getChannel().getIdLong() == Long.parseLong(retSplit[5])) {
            msg = event.getChannel().getHistoryAround(retSplit[6], 1).complete().getRetrievedHistory().get(0);
            msg = msg.reply("Closing registration for this?").complete();
        } else {
            msg = event.getChannel().sendMessage("Closing registration for " + anyExistingReg).complete();
        }
        msg.addReaction("U+2705").queue();
        msg.addReaction("U+274C").queue();
    }
}
