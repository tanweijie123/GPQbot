package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import logic.GuildMethod;
import model.UserAccountExport;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

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

        String reply = "Current Guild Members registered with NyanBot: \n" +
                GuildMethod.getGuildMembers(event.getGuild().getId())
                .stream()
                .filter(x -> event.getGuild().getMemberById(x.getUserId()) != null)
                .map(x -> new UserAccountExport(x.getGuildId(), x.getUserId(), x.getJob(), x.getFloor(), x.isRegistered(),
                        event.getGuild().getMemberById(x.getUserId()).getEffectiveName()))
                .sorted(Comparator.comparing(UserAccountExport::getIgn))
                .map(x -> x.toString())
                .collect(Collectors.joining("\n"));

        event.reply(reply);
    }
}
