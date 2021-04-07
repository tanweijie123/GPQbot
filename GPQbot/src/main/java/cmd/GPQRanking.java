package cmd;

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
            HashMap<UserAccount, Integer> tallyCount = new HashMap<>();
            lst.stream().forEach(
                    x -> x.getParticipantList().stream().forEach(y -> {
                if (tallyCount.containsKey(y)) {
                    tallyCount.put(y, tallyCount.get(y) + 1);
                } else {
                    tallyCount.put(y, 1);
                }
            }));

            List<Map.Entry<UserAccount, Integer>> top20 = tallyCount.entrySet().stream()
                    .sorted((x, y) -> {
                        int compare = Integer.compare(y.getValue(), x.getValue());
                        if (compare == 0) {
                            compare = Integer.compare(y.getKey().getFloor(), x.getKey().getFloor());
                        }
                        return compare;
                    }
                    ).limit(LIMIT).collect(Collectors.toList());
            int max = Math.min(LIMIT, top20.size());

            String reply = "Top 20 Attendance for GPQ:\n";
            for(int i = 0; i < max; i++) {
                reply += String.format("%d. %s -> %d times", i+1, top20.get(i).getKey().getIgn(), top20.get(i).getValue());
            }

            event.reply(reply);
        }

    }
}
