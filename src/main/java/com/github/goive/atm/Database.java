package com.github.goive.atm;

import com.github.goive.atm.dto.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database {

    private static final String FILE_NAME = "anime-offline-database.json";
    private static final String FILE_URL = "https://github.com/manami-project/anime-offline-database/raw/master/anime-offline-database.json";

    private List<Item> items = new ArrayList<>();

    public Database() {
        this(FILE_NAME);
    }

    @SuppressWarnings("unchecked")
    public Database(String dbFile) {
        try {
            String homeDir = System.getProperty("user.home") + "/.atm";
            if (!Files.exists(Paths.get(homeDir))) {
                Files.createDirectory(Paths.get(homeDir));
            }

            String absoluteFilePath = homeDir + "/" + dbFile;
            File file = new File(absoluteFilePath);

            if (!file.exists()) {
                BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(absoluteFilePath);
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                fileOutputStream.close();
                in.close();
            }

            JsonReader jsonReader = new JsonReader(new FileReader(file));
            Gson gson = new GsonBuilder().create();
            Map map = gson.fromJson(jsonReader, Map.class);
            List<Map> data = (List<Map>) map.get("data");
            parseItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseItems(List<Map> data) {
        for (Map rawItem : data) {
            items.add(new Item(
                    (String) rawItem.get("type"),
                    (String) rawItem.get("title"),
                    (String) rawItem.get("picture"),
                    (String) rawItem.get("thumbnail"),
                    ((Double) rawItem.get("episodes")).intValue(),
                    (List<String>) rawItem.get("sources"),
                    (List<String>) rawItem.get("relations"),
                    (List<String>) rawItem.get("synonyms")
            ));
        }
    }

    public List<Item> getItems() {
        return items;
    }
}
