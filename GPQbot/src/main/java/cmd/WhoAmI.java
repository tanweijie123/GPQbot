package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.Data;
import model.UserAccount;
import net.dv8tion.jda.internal.utils.tuple.MutablePair;

public class WhoAmI extends Command {

    public WhoAmI() {
        super.name = "whoami";
        super.help = "Gets what NyanBot knows about you";
    }

    @Override
    protected void execute(CommandEvent event) {
        long userID = event.getAuthor().getIdLong();
        UserAccount ua = Data.currentUserList.getByUserKey(userID, event.getMember().getEffectiveName());

        if (ua == null) {
            ua = new UserAccount(userID);
            Data.currentUserList.add(new MutablePair<>(userID, ua));
            ua.setIgn(event.getMember().getEffectiveName());
        }

        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.toString());
    }
}