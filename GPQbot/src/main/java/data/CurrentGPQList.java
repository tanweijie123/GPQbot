package data;

import net.dv8tion.jda.internal.utils.tuple.MutablePair;
import storage.Storage;

import java.net.URL;
import java.util.HashMap;

public class CurrentGPQList {
    private HashMap<Long, URL> currentList;

    public CurrentGPQList() {
        currentList = new HashMap();
    }

    public boolean add(MutablePair<Long, URL> toAdd) {
        currentList.put(toAdd.getLeft(), toAdd.getRight());
        Storage.saveCurrentGPQList(this);
        return true;
    }

    public String getByGuildKey(Long key) {
        URL content = currentList.get(key);
        return (content != null) ? content.toString() : null;
    }

    public boolean remove(Long key) {
        currentList.remove(key);
        Storage.saveCurrentGPQList(this);
        return true;
    }
}
