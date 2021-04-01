package storage;

import data.CurrentGPQList;
import data.CurrentUserList;
import data.PastGPQList;

import java.util.Optional;

/* Facade Pattern for Storage package. */
public class Storage {
    public static boolean saveCurrentGPQList(CurrentGPQList content) {
        return JsonAdapter.saveCurrentGPQList(content);
    }

    public static Optional<CurrentGPQList> loadCurrentGPQList() {
        return JsonAdapter.loadCurrentGPQList();
    }

    public static boolean savePastGPQList(PastGPQList content) {
        return JsonAdapter.savePastGPQList(content);
    }

    public static Optional<PastGPQList> loadPastGPQList() {
        return JsonAdapter.loadPastGPQList();
    }

    public static boolean saveCurrentUserList(CurrentUserList content) {
        return JsonAdapter.saveCurrentUserList(content);
    }

    public static Optional<CurrentUserList> loadCurrentUserList() {
        return JsonAdapter.loadCurrentUserList();
    }
}
