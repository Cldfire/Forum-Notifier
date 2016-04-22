package com.cldfire.spigotnotifier2.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class LangUtils { // TODO: Clean this up, rushed it to get repo online

    private static File APP_DIR;
    private static Properties properties;

    public enum Locale {
        EN_UK("en_UK"),
        EN_US("en_US"); // Add more

        private final String tag;
        Locale(String tag) {
            this.tag = tag;
        }
        public String getTag() {
            return this.tag;
        }
    }

    // Returns localised text
    public static String translate(String property) {
        return properties.getProperty(property, property);
    }

    public static void loadLocale(Locale locale) {
        APP_DIR = new File(System.getProperty("user.home"), ".spigotnotifier");
        if (!APP_DIR.exists()) {
            APP_DIR.mkdir();
        }
        properties = new Properties();

        try {
            File file = new File(APP_DIR + "/lang", locale.getTag() + ".lang");
            if (file.exists()) {
                properties.load(new FileInputStream(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
