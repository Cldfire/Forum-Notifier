package com.cldfire.xenforonotifier.util;

import com.cldfire.xenforonotifier.XenForoNotifier;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyUtils { // TODO: Clean this up, rushed it to get repo online

    private static Properties properties;

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public static void loadSettings() {
        properties = new Properties();

        try {
            File file = new File(XenForoNotifier.APP_DIR, "settings.ini");
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
