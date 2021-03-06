package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import logic.GuildMethod;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.util.*;

public class GPQRanking extends Command {
    private static final int LIMIT = 20;

    public GPQRanking() {
        super.name = "gpqrank";
        super.help = "Get the top 20 ranking attendance for GPQ";
    }

    @Override
    protected void execute(CommandEvent event) {

        List<Pair<String, Integer>> top20 = GuildMethod.gpqRanking(event.getGuild().getId());

        int rank = 1;
        int myRank = -1;

        String reply = "Top 20 Attendance for GPQ:\n";
        for(int i = 0; i < top20.size(); i++) {
            if (i != 0 && (top20.get(i).getRight() != top20.get(i-1).getRight())) {
                if (i >= LIMIT) break; //to stop after showing all the same ranked
                rank = i + 1;
            }
            if (top20.get(i).getLeft().equals(event.getAuthor().getId())) {
                myRank = rank;
            }
            String ign = "NOT IN SERVER - " + top20.get(i).getLeft();
            try {
                ign = event.getGuild().getMemberById(top20.get(i).getLeft()).getEffectiveName();
            } catch (NullPointerException e) {
                //ign will use default if not found.
            }
            reply += String.format("%d. %s -> %d time(s) \n", rank, ign, top20.get(i).getRight());
        }

        if (myRank > 0) {
            reply += "\nYou rank " + myRank + " in the guild.";
        }
        event.reply(reply);

    }
}
