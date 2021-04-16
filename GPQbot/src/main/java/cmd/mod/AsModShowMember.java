package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.Data;
import model.UserAccount;
import net.dv8tion.jda.api.entities.Member;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        List<Member> memList = event.getGuild().getMembers();

        List<UserAccount> uaList =
                memList.stream()
                .map(x -> Data.currentUserList.getByUserKey(x.getIdLong(), x.getEffectiveName()))
                .filter(x -> x != null)
                .sorted( (x,y) -> x.getIgn().compareToIgnoreCase(y.getIgn()) )
                .collect(Collectors.toList());

        for (UserAccount ua : uaList) {
            reply += ua.toString() + "\n";
        }

        event.reply(reply);
    }
}
