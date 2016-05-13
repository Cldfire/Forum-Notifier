package com.cldfire.forumnotifier.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Forum {
    private final StringProperty url;
    private final ObjectProperty<ForumType> type;
    private String protocol;
    private List<Account> accounts = new ArrayList<>();

    public Forum(Map<String, Object> forumData) {
        this.url = new SimpleStringProperty((String) forumData.get("url"));
        this.type = new SimpleObjectProperty<>((ForumType) forumData.get("type"));
        this.protocol = (String) forumData.get("protocol");
    }

    public enum ForumType {
        XENFORO("XenForo");

        private final String name;

        ForumType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public ForumType fromName(String name) {
            for (ForumType forumType : values()) {
                if (name.equalsIgnoreCase(forumType.getName())) {
                    return forumType;
                }
            }
            return null;
        }
    }

    public Map<String, Object> getForumData() {
        Map<String, Object> forumData = new HashMap<>();
        List<Map<String, Object>> accountData = new ArrayList<>();

        forumData.put("url", url.get());
        forumData.put("type", type.get());
        forumData.put("protocol", protocol);

        accounts.forEach(a -> {
            Map<String, Object> ad = new HashMap<>();
            List<Map<String, Object>> cd = new ArrayList<>();

            a.getCookies().forEach(c -> {
                Map<String, Object> cda = new HashMap<>();

                cda.put("name", c.getName());
                cda.put("value", c.getValue());
                cda.put("domain", c.getDomain());
                cda.put("expires", c.getExpires());
                cda.put("path", c.getPath());
                cda.put("isSecure", c.isSecure());
                cda.put("isHttpOnly", c.isHttpOnly());

                cd.add(cda);
            });

            ad.put("name", a.getName());
            ad.put("cookies", cd);
            ad.put("profileUrl", a.getProfileUrl());
            ad.put("picFilePath", a.getPicFilePath());
            ad.put("xpathMaps", a.getAccountXpathsMap());

            accountData.add(ad);
        });

        forumData.put("accounts", accountData);

        return forumData;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public ForumType getType() {
        return type.get();
    }

    public void setType(ForumType type) {
        this.type.set(type);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

}
