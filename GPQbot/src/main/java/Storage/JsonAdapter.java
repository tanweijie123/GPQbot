package Storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.CurrentGPQList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class JsonAdapter {
    public static boolean saveCurrentGPQList(CurrentGPQList content) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        String exportString = gson.toJson(content);
        return writeToFile(new File("gpq_current.json"), exportString);
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

    public static Optional<CurrentGPQList> loadCurrentGPQList() {
        File file = new File("gpq_current.json");
        if (!file.exists()) {
            CurrentGPQList list = new CurrentGPQList();
            saveCurrentGPQList(list);
            return Optional.of(list);
        }

        try {
            Scanner readFile = new Scanner(file);
            String importString = readFile.useDelimiter("\\Z").next();
            readFile.close();

            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();

            return Optional.of(gson.fromJson(importString, CurrentGPQList.class));
        } catch (IOException | NoSuchElementException e) {
            return Optional.empty(); //this should not happen.
        }

    }

}
