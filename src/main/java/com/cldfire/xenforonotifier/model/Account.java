package com.cldfire.xenforonotifier.model;

import com.cldfire.xenforonotifier.util.ForumsStore;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Account { // TODO: Make sure all this becomes thread safe at some point or another
    private final StringProperty name;
    private final StringProperty alertCountProperty;
    private final StringProperty messageCountProperty;
    private int alertCount;
    private int messageCount;
    private Set<Cookie> cookies;
    //private final ObjectProperty<Image> favicon;

    public Account(Map<String, Object> accountData) {
        this.cookies = (Set<Cookie>) accountData.get("cookies");
        this.name = new SimpleStringProperty((String) accountData.get("name"));
        alertCount = 0;
        messageCount = 0;

        alertCountProperty = new SimpleStringProperty("Alerts: 0");
        messageCountProperty = new SimpleStringProperty("Messages: 0");
        //favicon = new SimpleObjectProperty<>(image);
    }

    public Forum getForum() {
        for (Forum forum : ForumsStore.forums) {
            for (Account account : forum.getAccounts()) {
                if (account == this) {
                    return forum;
                }
            }
        }
        return null; // wat
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

    public int getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(Integer alertCount) {
        this.alertCount = alertCount;
        this.alertCountProperty.set("Alerts: " + alertCount.toString());
    }

    public StringProperty getAlertProperty() {
        return alertCountProperty;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
        this.messageCountProperty.set("Messages: " + messageCount.toString());
    }

    public StringProperty getMessageProperty() {
        return messageCountProperty;
    }

    //public Image getFavicon() {
    //    return favicon.get();
    //}

    // public void setFavicon(Image favicon) {
    //    this.favicon.set(favicon);
    //}
}
