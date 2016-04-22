package com.cldfire.xenforonotifier.util;

import com.cldfire.xenforonotifier.XenForoNotifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class LangUtils { // TODO: Clean this up, rushed it to get repo online

    private static Properties properties;

    public enum Locale {
        EN_TA("en_TA"),
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
        properties = new Properties();

        try {
            File file = new File(XenForoNotifier.APP_DIR + "/lang", locale.getTag() + ".lang");
            if (file.exists()) {
                properties.load(new FileInputStream(file));
            } else {
                properties.load(new InputStreamReader(LangUtils.class.getResourceAsStream("/lang/" + file.getName())));
            }
            System.out.println("Loaded language " + locale.getTag());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
