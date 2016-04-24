package com.cldfire.xenforonotifier.model;

import com.cldfire.xenforonotifier.util.Settings;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Set;

public class ForumAccount { // TODO: Make sure all this becomes thread-safe at some point or another

    private final StringProperty forumUrl;
    private Set<Cookie> cookies;
    private final StringProperty name;
    private String connProtocol;
    private Integer alertCount = 0;
    private Integer messageCount = 0;

    public ForumAccount(String forumUrl, Set<Cookie> cookies, String name, String connProtocol) {
        this.forumUrl = new SimpleStringProperty(forumUrl);
        this.cookies = cookies;
        this.name = new SimpleStringProperty(name);
        this.connProtocol = connProtocol;

        try {
            Settings.set(forumUrl, forumUrl);
            Settings.set(forumUrl + ".profilename", name);
            Settings.set(forumUrl + ".protocol", connProtocol);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getForumUrl() {
        return forumUrl.get();
    }

    public void setForumUrl(String forumUrl) {
        this.forumUrl.set(forumUrl);
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getConnProtocol() {
        return connProtocol;
    }

    public void setConnProtocol(String connProtocol) {
        this.connProtocol = connProtocol;
    }

    public Integer getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(Integer alertCount) {
        this.alertCount = alertCount;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }
}