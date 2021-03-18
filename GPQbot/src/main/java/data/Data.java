package data;

import storage.Storage;

public class Data {
    public static CurrentGPQList currentGPQList;
    public static CurrentUserList currentUserList;

    public static void init() {
        currentGPQList = Storage.loadCurrentGPQList().orElse(new CurrentGPQList());
        currentUserList = Storage.loadCurrentUserList().orElse(new CurrentUserList());
    }
}
