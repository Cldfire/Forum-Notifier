package com.cldfire.forumnotifier.util;

import com.cldfire.forumnotifier.ForumNotifier;
import com.cldfire.forumnotifier.model.Account;
import com.cldfire.forumnotifier.model.AccountXpaths;
import com.cldfire.forumnotifier.model.Forum;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ForumsStore { // TODO: Add things, idk what to add
    public static List<Forum> forums = new ArrayList<>();

    public static void loadForums() {
        Gson gson = new Gson();
        File file = new File(ForumNotifier.APP_DIR, "forums.json");
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
            Set<Map> forumData = gson.fromJson(fileReader, new TypeToken<Set<Map<String, Object>>>() {}.getType());
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (forumData != null) {
                forumData.forEach(f -> {

                    Forum addForum = createForum((String) f.get("url"), Forum.ForumType.XENFORO, (String) f.get("protocol"));
                    List<Map<String, Object>> accountData = new ArrayList<>((List<Map<String, Object>>) f.get("accounts"));

                    accountData.forEach(ad -> {
                        Set<Cookie> cookies = new HashSet<>();
                        List<LinkedTreeMap<String, Object>> cookieDataMap = new ArrayList<>((List<LinkedTreeMap<String, Object>>) ad.get("cookies"));
                        Map<String, List<String>> xpathsMap = new HashMap<>((Map<String, List<String>>) ad.get("xpathsMap"));

                        cookieDataMap.forEach(c -> {
                            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
                            String expiresText = (String) c.get("expires");

                            try {
                                Date expires;
                                if (expiresText != null) {
                                    expires = formatter.parse(expiresText);
                                    cookies.add(new Cookie((String) c.get("domain"), (String) c.get("name"), (String) c.get("value"), (String) c.get("path"), expires, (boolean) c.get("isSecure"), (boolean) c.get("isHttpOnly")));
                                } else {
                                    cookies.add(new Cookie((String) c.get("domain"), (String) c.get("name"), (String) c.get("value"), (String) c.get("path"), null, (boolean) c.get("isSecure"), (boolean) c.get("isHttpOnly")));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        });

                        // TODO: This is stupid, error-prone and should not be this much typing. Rework this system
                        AccountXpaths accountXpaths = new AccountXpaths(
                                xpathsMap.get("userPassLoginForm"),
                                xpathsMap.get("twoFactorLoginForm"),
                                xpathsMap.get("passwordFieldName"),
                                xpathsMap.get("usernameFieldName"),
                                xpathsMap.get("stayLoggedInFieldName"),
                                xpathsMap.get("loginButtonValue"),
                                xpathsMap.get("twoFactorCodeFieldName"),
                                xpathsMap.get("trustTwoFactorLoginFieldName"),
                                xpathsMap.get("confirmTwoFactorButtonName"),
                                xpathsMap.get("accountUrl"),
                                xpathsMap.get("accountPic"),
                                xpathsMap.get("accountName"),
                                xpathsMap.get("messages"),
                                xpathsMap.get("alerts"),
                                xpathsMap.get("ratings"),
                                xpathsMap.get("posts"),
                                xpathsMap.get("followingList"),
                                xpathsMap.get("followerList"),
                                xpathsMap.get("followerCount")
                        );

                        addForum.addAccount(createAccount(cookies, (String) ad.get("name"), (String) ad.get("profileUrl"), (String) ad.get("picFilePath"), accountXpaths));
                    });
                    forums.add(addForum);
                });
            }
        }
    }

    public static void saveForums() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (Boolean.valueOf(Settings.get("json.prettyprint"))) {
            gsonBuilder.setPrettyPrinting().serializeNulls();
        }
        Gson gson = gsonBuilder.create();
        File file = new File(ForumNotifier.APP_DIR, "forums.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            Set<Map> saveForums = new HashSet<>();

            forums.forEach(f -> {
                saveForums.add(f.getForumData());
            });

            fileWriter.write(gson.toJson(saveForums));
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

    public static Forum getForum(String forumUrl) {
        return forums.stream().filter(f -> forumUrl.equalsIgnoreCase(f.getUrl())).findFirst().orElse(null);
    }

    public static Forum createForum(String url, Forum.ForumType type, String protocol) {
        Map<String, Object> returnForumData = new HashMap<>();

        returnForumData.put("url", url);
        returnForumData.put("type", type);
        returnForumData.put("protocol", protocol);

        return new Forum(returnForumData);
    }

    public static Account createAccount(Set<Cookie> cookies, String name, String profileUrl, String picFilePath, AccountXpaths xpathsMap) {
        Map<String, Object> returnAccountData = new HashMap<>();

        returnAccountData.put("cookies", cookies);
        returnAccountData.put("name", name);
        returnAccountData.put("profileUrl", profileUrl);
        returnAccountData.put("picFilePath", picFilePath);
        returnAccountData.put("xpathsMap", xpathsMap);

        return new Account(returnAccountData);
    }
}