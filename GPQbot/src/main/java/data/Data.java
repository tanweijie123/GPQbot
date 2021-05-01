package data;

import storage.Storage;

public class Data {
    public static CurrentGPQList currentGPQList;
    public static PastGPQList pastGPQList;
    public static CurrentUserList currentUserList;

    public static void init() {
        currentGPQList = Storage.loadCurrentGPQList().orElse(new CurrentGPQList());
        pastGPQList = Storage.loadPastGPQList().orElse(new PastGPQList());
        currentUserList = Storage.loadCurrentUserList().orElse(new CurrentUserList());
    }

}
