package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import data.Data;
import model.GPQParticipation;
import model.UserAccount;
import net.dv8tion.jda.internal.utils.tuple.MutablePair;
import net.dv8tion.jda.internal.utils.tuple.MutableTriple;

import java.util.LinkedList;

public class WhoAmI extends Command {

    public WhoAmI() {
        super.name = "whoami";
        super.help = "Gets what NyanBot knows about you";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMember().getEffectiveName().contains(" ")) {
            event.reply("I do not accept nickname / IGN with a space.");
            return;
        }

        long userID = event.getAuthor().getIdLong();
        UserAccount ua = Data.currentUserList.getByUserKey(userID, event.getMember().getEffectiveName());

        if (ua == null) {
            ua = new UserAccount(userID);
            Data.currentUserList.add(new MutablePair<>(userID, ua));
            ua.setIgn(event.getMember().getEffectiveName());

            /* if user have migration data */
            MutableTriple<String, Integer, Integer> triple = Data.migrateData.get(event.getMember().getEffectiveName().toUpperCase());
            if (triple != null) {
                ua.setJob(triple.getMiddle());
                ua.setFloor(triple.getRight());
            }
        }

        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.toString());
    }
}
