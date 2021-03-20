package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.Data;
import net.dv8tion.jda.api.entities.Message;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CloseRegistration extends Command {

    public CloseRegistration() {
        super.name = "close";
        super.help = "Close the current GPQ registration (only mods)";
        super.requiredRole = Settings.BOT_MOD_NAME;
    }

    @Override
    protected void execute(CommandEvent event) {
        String anyExistingReg = Data.currentGPQList.getByGuildKey(event.getGuild().getIdLong());

        if (anyExistingReg == null) {
            event.reply("No opened registration to close");
            return;
        }

        Message msg = event.getChannel().sendMessage("Closing registration for " + anyExistingReg + " and getting participants for today (" + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("EEEE")) + ")?").complete();
        msg.addReaction("U+2705").queue();
        msg.addReaction("U+274C").queue();
    }
}
