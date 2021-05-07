package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import logic.GuildMethod;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.stream.Collectors;

public class AddMembers extends Command {

    public AddMembers() {
        super.name = "gpqaddmem";
        super.help = "Add members to current gpq (only mods)"; //warning, only registered members allowed
        super.requiredRole = Settings.BOT_MOD_NAME;
        super.arguments = "[user]...";
    }

    @Override
    protected void execute(CommandEvent event) {
        List<String> memList = event.getMessage().getMentionedMembers(event.getGuild())
                .stream().map(x -> x.getId()).collect(Collectors.toList());

        GuildMethod.appendMemberToCurrent(event.getGuild().getId(), memList);

        String reply = "Note that this function does not add unregistered members. \n";
        reply += "Currently appended members to current gpq: \n";

        List<String> appendedList = GuildMethod.getAppendedMembersToCurrent(event.getGuild().getId())
                .stream().map(x -> event.getGuild().getMemberById(x).getEffectiveName()).collect(Collectors.toList());
        for(String s : appendedList) {
            reply += s + "\n";
        }

        event.reply(reply);
    }


}
