package com.cldfire.xenforonotifier.util;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.model.Account;
import com.cldfire.xenforonotifier.model.Forum;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;

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
                forums = Arrays.asList(fora);
            }
        }
    }

    public static void saveForums() {
        Gson gson = new Gson();
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
    }

    public static Forum getForum(String forumurl) {
        return forums.stream().filter(forum -> forumurl.equalsIgnoreCase(forum.getForum().toLowerCase())).findFirst().orElse(null);
    }

    public static void lazy(String forumurl, String protocol, String name, Set<Cookie> cookies) {
        Account account = new Account();
        account.setName(name);
        account.setCookies(cookies);
        Forum forum = new Forum(forumurl);
        forum.setProtocol(protocol);
        forum.addAccount(account);

        forums.add(forum);
    }
}