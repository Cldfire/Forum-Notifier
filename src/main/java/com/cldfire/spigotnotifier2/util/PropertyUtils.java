package com.cldfire.spigotnotifier2.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyUtils { // TODO: Clean this up, rushed it to get repo online

    private static File APP_DIR;
    private static Properties properties;

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public static void loadSettings() {
        APP_DIR = new File(System.getProperty("user.home"), ".spigotnotifier");
        if (!APP_DIR.exists()) {
            APP_DIR.mkdir();
        }
        properties = new Properties();

        try {
            File file = new File(APP_DIR, "settings.ini");
            if (file.exists()) {
                FileInputStream fileInput = new FileInputStream(file);
                properties.load(fileInput);
                fileInput.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
