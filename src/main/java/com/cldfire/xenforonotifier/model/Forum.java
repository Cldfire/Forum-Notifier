package com.cldfire.xenforonotifier.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Forum {
    private final StringProperty url;
    private final ObjectProperty<ForumType> type;
    private String protocol;
    private List<Account> accounts = new ArrayList<>();

    public Forum(String url, ForumType type, String protocol, Account account) {
        this.url = new SimpleStringProperty(url);
        this.type = new SimpleObjectProperty<>(type);
        this.protocol = protocol;
        accounts.add(account);
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

}
