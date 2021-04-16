package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import data.Data;
import model.GPQParticipation;
import model.UserAccount;

import java.util.*;
import java.util.stream.Collectors;

public class GPQRanking extends Command {
    private static final int LIMIT = 20;

    public GPQRanking() {
        super.name = "gpqrank";
        super.help = "Get the top 20 ranking attendance for GPQ";
    }

    @Override
    protected void execute(CommandEvent event) {
        LinkedList<GPQParticipation> lst = Data.pastGPQList.getList(event.getGuild().getIdLong());
        if (lst == null) {
            event.reply("This guild had not went to gpq before.");
        } else {
            HashMap<Long, Integer> tallyCount = new HashMap<>();
            lst.stream().forEach(
                    x -> x.getParticipantList().stream().map(y -> y.getUserId()).forEach(y -> {
                if (tallyCount.containsKey(y)) {
                    tallyCount.put(y, tallyCount.get(y) + 1);
                } else {
                    tallyCount.put(y, 1);
                }
            }));

            List<Map.Entry<Long, Integer>> top20 = tallyCount.entrySet().stream()
                    .sorted((x, y) -> Integer.compare(y.getValue(), x.getValue()))
                    .collect(Collectors.toList());
            int max = Math.min(LIMIT, top20.size());

            int rank = 1;
            int myRank = -1;

            String reply = "Top 20 Attendance for GPQ:\n";
            for(int i = 0; i < max; i++) {
                if (i != 0 && (top20.get(i).getValue() != top20.get(i-1).getValue())) {
                    rank = i + 1;
                }
                if (top20.get(i).getKey() == event.getAuthor().getIdLong()) {
                    myRank = rank;
                }
                UserAccount ua = Data.currentUserList.getByUserKey(top20.get(i).getKey(), null);
                reply += String.format("%d. %s -> %d time(s) \n", rank, ua.getIgn(), top20.get(i).getValue());
            }

            if (myRank == -1) { // check if user is below top 20
                for (int i = max; i < top20.size(); i++) {
                    if (i != 0 && (top20.get(i).getValue() != top20.get(i-1).getValue())) {
                        rank = i + 1;
                    }

                    if (top20.get(i).getKey() == event.getAuthor().getIdLong()) {
                        myRank = rank;
                    }
                }
            }

            if (myRank > 0) {
                reply += "\nYou rank " + myRank + " in the guild.";
            }
            event.reply(reply);
        }

    }
}
