package data;

import model.GPQParticipation;
import net.dv8tion.jda.internal.utils.tuple.MutablePair;
import storage.Storage;

import java.util.HashMap;
import java.util.LinkedList;

public class PastGPQList {
    private HashMap<Long, LinkedList<GPQParticipation>> historyMap;

    public PastGPQList() {
        this.historyMap = new HashMap<>();
    }

    public boolean add(GPQParticipation toAdd) {
        LinkedList<GPQParticipation> lst = historyMap.get(toAdd.getGuildId());
        if (lst == null) {
            lst = new LinkedList<>();
            historyMap.put(toAdd.getGuildId(), lst);
        }
        lst.add(toAdd);
        Storage.savePastGPQList(this);
        return true;
    }

    //TODO: return a clone list.
    public LinkedList<GPQParticipation> getList(long guildId) {
        return historyMap.get(guildId);
    }

}
