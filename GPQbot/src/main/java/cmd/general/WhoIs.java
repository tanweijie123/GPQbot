package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import logic.UsersMethod;
import model.UserAccount;

import java.util.List;
import java.util.stream.Collectors;

public class WhoIs extends Command {

    public WhoIs() {
        super.name = "whois";
        super.help = "Gets info about another user within the same server";
        super.arguments = "[EffectiveName - name shown in guild, ignore case]";
    }


    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs().trim();
        if (args.isEmpty()) {
            event.reply("No name found.");
        }

        List<String> searchedListId = event.getGuild().getMembersByEffectiveName(args, true)
                .stream().map(x -> x.getId()).collect(Collectors.toList());

        List<UserAccount> uaList = UsersMethod.getUsers(event.getGuild().getId(), searchedListId);

        String reply = "Members found: \n";
        for (UserAccount ua : uaList) {
            if (ua.isRegistered())
                reply += ua.replyString(event.getGuild().getMemberById(ua.getUserId()).getEffectiveName()) + "\n";
            else
                reply += event.getGuild().getMemberById(ua.getUserId()).getEffectiveName() + " is not registered.\n";
        }
        event.reply(reply);
    }
}
