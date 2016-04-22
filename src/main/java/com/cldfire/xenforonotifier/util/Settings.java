package com.cldfire.xenforonotifier.util;

import com.cldfire.xenforonotifier.XenForoNotifier;

import java.io.*;
import java.util.Properties;

public class Settings { // TODO: Clean this up, rushed it to get repo online

    private static Properties properties;
    public static double version;
    public static double innerVersion;

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
            File file = new File(XenForoNotifier.APP_DIR, "settings.properties");
            if (file.exists()) {
                properties.load(new FileInputStream(file));
            } else {
                System.out.println("Settings File not found, creating one!");
                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                // This
                InputStream inputStream = Settings.class.getResourceAsStream("/" + file.getName());

                int read;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                outputStream.close();
                inputStream.close();
                // Or which is nice but I don't know how heavy either are so ¯\_(ツ)_/¯
                /*
                InputStream inputStream = Settings.class.getResourceAsStream("/" + file.getName());
                Properties tmp = new Properties();
                tmp.load(inputStream);
                FileOutputStream fileOutputStream = new FileOutputStream(new File(XenForoNotifier.APP_DIR, "settings.properties"));
                tmp.store(fileOutputStream, "XenForo Notifier");
                fileOutputStream.close();
                 */
                load();
            }
            InputStream inputStream = Settings.class.getResourceAsStream("/" + file.getName());
            version = Double.parseDouble(get("settings.version"));
            Properties tmp = new Properties();
            tmp.load(inputStream);
            innerVersion = Double.parseDouble(tmp.getProperty("settings.version"));
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(XenForoNotifier.APP_DIR, "settings.properties"));
            properties.store(fileOutputStream, "XenForo Notifier");
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
