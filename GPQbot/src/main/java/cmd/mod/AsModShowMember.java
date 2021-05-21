package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import logic.GuildMethod;
import model.UserAccountExport;

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

        List<UserAccountExport> uaeList = GuildMethod.getGuildMembers(event.getGuild().getId())
                .stream()
                .map(x -> new UserAccountExport(x.getGuildId(), x.getUserId(), x.getJob(), x.getFloor(), x.isRegistered(),
                        event.getGuild().getMemberById(x.getUserId()).getEffectiveName()))
                .sorted(Comparator.comparing(UserAccountExport::getIgn))
                .collect(Collectors.toList());

        for (UserAccountExport uae : uaeList) {
            reply += uae.toString() + "\n";
        }

        event.reply(reply);
    }
}
