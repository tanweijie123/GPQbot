package data;

import model.UserAccount;
import net.dv8tion.jda.internal.utils.tuple.MutablePair;
import storage.Storage;

import java.net.URL;
import java.util.HashMap;

public class CurrentUserList {
    private HashMap<Long, UserAccount> currentList;

    public CurrentUserList() {
        currentList = new HashMap();
    }

    public boolean add(MutablePair<Long, UserAccount> toAdd) {
        currentList.put(toAdd.getLeft(), toAdd.getRight());
        Storage.saveCurrentUserList(this);
        return true;
    }

    /**
     * Gets the UserAccount using the UserID key
     */
    public UserAccount getByUserKey(Long key) {
        return currentList.get(key);
    }

    public boolean update(MutablePair<Long, UserAccount> toUpdate) {
        return add(toUpdate);
    }

    public boolean save() {
        return Storage.saveCurrentUserList(this);
    }

}
