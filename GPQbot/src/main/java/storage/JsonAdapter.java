package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.CurrentGPQList;
import data.CurrentUserList;
import data.PastGPQList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

class JsonAdapter {
    static boolean saveCurrentGPQList(CurrentGPQList content) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        String exportString = gson.toJson(content);
        return writeToFile(new File("gpq_current.json"), exportString);
    }

    static boolean savePastGPQList(PastGPQList content) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        String exportString = gson.toJson(content);
        return writeToFile(new File("gpq_past.json"), exportString);
    }

    static boolean saveCurrentUserList(CurrentUserList content) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        String exportString = gson.toJson(content);
        return writeToFile(new File("user_account.json"), exportString);
    }

    private static boolean writeToFile(File file, String exportString) {
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(exportString);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("Unable to write to " + file + "; content=>" + exportString);
            return false;
        }
    }

    static Optional<CurrentGPQList> loadCurrentGPQList() {
        File file = new File("gpq_current.json");
        Optional<CurrentGPQList> gpqList = loadFromFile(file, CurrentGPQList.class);
        return gpqList.or( () -> Optional.of(new CurrentGPQList()) );

    }

    static Optional<PastGPQList> loadPastGPQList() {
        File file = new File("gpq_past.json");
        Optional<PastGPQList> gpqList = loadFromFile(file, PastGPQList.class);
        return gpqList.or( () -> Optional.of(new PastGPQList()) );

    }

    static Optional<CurrentUserList> loadCurrentUserList() {
        File file = new File("user_account.json");
        Optional<CurrentUserList> userList = loadFromFile(file, CurrentUserList.class);
        return userList.or( () -> Optional.of(new CurrentUserList()) );
    }

    private static <E> Optional<E> loadFromFile(File file, Class<E> classFile) {
        try {
            Scanner readFile = new Scanner(file);
            String importString = readFile.useDelimiter("\\Z").next();
            readFile.close();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();

            return Optional.of(gson.fromJson(importString, classFile));
        } catch (IOException | NoSuchElementException e) {
            return Optional.empty(); //this should not happen.
        }
    }

}
