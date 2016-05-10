package com.cldfire.forumnotifier.util;

import com.cldfire.forumnotifier.ForumNotifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

public class LangUtils { // TODO: Clean this up, rushed it to get repo online

    private static Properties properties;

    /**
     * Cheers for Thinkofname for making this 5 times faster than my version
     *
     * @param property key per se
     * @param replacey (Optional) if the provided {@code property} contains "{0}" this method will replace them with
     *                 strings that are relative to the array {@code replacey}
     * @return Translated String
     */
    public static String translate(String property, String... replacey) {
        String translate = properties.getProperty(property, property);
        if (translate.contains("{0}")) {
            int cap = property.length() + Arrays.stream(replacey).mapToInt(String::length).sum();
            StringBuilder builder = new StringBuilder(cap);
            int index = 0;
            int offset = 0;
            int pos = 0;
            while (index < replacey.length && (pos = translate.indexOf("{0}", offset)) != -1) {
                builder.append(translate, offset, pos);
                builder.append(replacey[index++]);
                offset = pos + 3;
            }
            builder.append(translate, offset, translate.length());
            return builder.toString();

        }
        return translate;
    }

    /**
     * Loads the locale
     *
     * @param locale {@link Locale}
     */
    public static void loadLocale(Locale locale) {
        properties = new Properties();

        try {
            File file = new File(ForumNotifier.APP_DIR + "/lang", locale.getTag() + ".lang");
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

    public enum Locale {
        EN_US("English (US)"),
        EN_TA("Taco"); // Add more

        private final String name;

        Locale(String name) {
            this.name = name;
        }

        public String getTag() {
            String[] nameSplit = this.name().split("_");
            return nameSplit[0].toLowerCase() + "_" + nameSplit[1];
        }

        public String getName() {
            return this.name;
        }
    }

}
