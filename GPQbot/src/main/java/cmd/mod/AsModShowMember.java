package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import logic.GuildMethod;
import model.UserAccount;

import java.util.List;

public class AsModShowMember extends Command {
    public AsModShowMember() {
        super.name = "guildlist";
        super.help = "Shows everyone that is registered with NyanBot in this guild.";
        super.requiredRole = Settings.BOT_MOD_NAME;
        super.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String reply = "Current Guild Members registered with NyanBot: \n";

        List<UserAccount> uaList = GuildMethod.getGuildMembers(event.getGuild().getId());

        for (UserAccount ua : uaList) {
            reply += ua.replyString(event.getGuild().getMemberById(ua.getUserId()).getEffectiveName()) + "\n";
        }

        event.reply(reply);
    }
}
