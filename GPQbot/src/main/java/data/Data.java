package data;

import net.dv8tion.jda.internal.utils.tuple.MutableTriple;
import storage.Storage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Data {
    public static CurrentGPQList currentGPQList;
    public static CurrentUserList currentUserList;
    public static HashMap<String, MutableTriple<String, Integer, Integer>> migrateData;

    public static void init() {
        currentGPQList = Storage.loadCurrentGPQList().orElse(new CurrentGPQList());
        currentUserList = Storage.loadCurrentUserList().orElse(new CurrentUserList());

        loadMigrateData();
    }

    private static void loadMigrateData() {
        migrateData = new HashMap<>();
        try {
            Scanner sc = new Scanner(new File("migrate.txt"));
            sc.nextLine(); // remove first line header.

            while(sc.hasNext()) {

                String[] split = sc.nextLine().split("/");
                MutableTriple<String, Integer, Integer> triple = MutableTriple.of(
                        split[0],
                        JobList.getIndexByJob_OneBased(split[1]),
                        Integer.parseInt(split[2])
                );

                migrateData.put(split[0], triple);

            }
            System.out.println("Migration Data loaded");
        } catch (IOException e) {
            System.err.println("Unable to load migration data");
        }
    }
}
