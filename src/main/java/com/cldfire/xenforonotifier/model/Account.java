package com.cldfire.xenforonotifier.model;

import com.gargoylesoftware.htmlunit.util.Cookie;

import java.util.Set;

public class Account {
    private String name;
    private Set<Cookie> cookies;
    private Integer alertCount = 0;
    private Integer messageCount = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
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
