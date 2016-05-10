package com.cldfire.forumnotifier.util;

import com.cldfire.forumnotifier.ForumNotifier;

import java.io.*;
import java.util.Properties;

public class Settings { // TODO: Clean this up
    public static double version;
    public static double lVersion;
    private static Properties properties;
    private static Properties lProperties;

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public static boolean contains(String key) {
        return properties.contains(key);
    }

    public static void load() {
        properties = new Properties();
        try {
            File file = new File(ForumNotifier.APP_DIR, "settings.properties");
            if (file.exists()) {
                properties.load(new FileInputStream(file));
            } else {
                System.out.println("Settings File not found, creating one!");
                InputStream inputStream = Settings.class.getResourceAsStream("/" + file.getName());
                Properties tmpProperties = new Properties();
                tmpProperties.load(inputStream);
                FileOutputStream fileOutputStream = new FileOutputStream(new File(ForumNotifier.APP_DIR, "settings.properties"));
                tmpProperties.store(fileOutputStream, "Forum Notifier");
                fileOutputStream.close();
                load();
            }
            InputStream inputStream = Settings.class.getResourceAsStream("/" + file.getName());
            version = Double.parseDouble(get("settings.version"));
            lProperties = new Properties();
            lProperties.load(inputStream);
            lVersion = Double.parseDouble(lProperties.getProperty("settings.version"));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() { // TODO: Use this
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(ForumNotifier.APP_DIR, "settings.properties"));
            properties.store(fileOutputStream, "Forum Notifier");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void versionCheck() {
        if (lVersion > version) {
            lProperties.keySet().stream().filter(object -> !properties.containsKey(object)).forEach(object -> {
                properties.setProperty((String) object, lProperties.getProperty((String) object));
            });
            properties.setProperty("settings.version", String.valueOf(lVersion));
            save();
        }
    }
}