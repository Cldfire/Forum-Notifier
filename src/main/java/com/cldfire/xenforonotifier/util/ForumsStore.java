package com.cldfire.xenforonotifier.util;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.model.Forum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForumsStore { // TODO: Add things, idk what to add
    private static List<Forum> forums = new ArrayList<Forum>();

    public static void loadForums() {
        Gson gson = new Gson();
        File file = new File(XenForoNotifier.APP_DIR, "forums.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileReader != null) {
            Forum[] fora = gson.fromJson(fileReader, Forum[].class);
            if (fora != null) {
                forums = new ArrayList<>(Arrays.asList(fora));
            }
        }
    }

    public static void saveForums() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (Boolean.valueOf(Settings.get("json.prettyprint"))) {
            gsonBuilder.setPrettyPrinting().serializeNulls();
        }
        Gson gson = gsonBuilder.create();
        File file = new File(XenForoNotifier.APP_DIR, "forums.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(forums));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addForum(Forum forum) {
        forums.add(forum);
        saveForums();
    }

    public static void removeForum(Forum forum) {
        forums.remove(forum);
        saveForums();
    }

    public static Forum getForum(String forumurl) {
        return forums.stream().filter(forum -> forumurl.equalsIgnoreCase(forum.getForum().toLowerCase())).findFirst().orElse(null);
    }
}