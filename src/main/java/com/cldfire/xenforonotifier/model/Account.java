package com.cldfire.xenforonotifier.model;

import com.cldfire.xenforonotifier.util.ForumsStore;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

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
    private final ObjectProperty<Image> accountPic;
    private String picFilePath;

    public Account(Map<String, Object> accountData) {
        this.cookies = (Set<Cookie>) accountData.get("cookies");
        this.name = new SimpleStringProperty((String) accountData.get("name"));
        this.accountPic = new SimpleObjectProperty<>(new Image("file:" + accountData.get("picFilePath")));
        picFilePath = (String) accountData.get("picFilePath");
        alertCount = 0;
        messageCount = 0;

        alertCountProperty = new SimpleStringProperty("Alerts: 0");
        messageCountProperty = new SimpleStringProperty("Messages: 0");
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

    public String getPicFilePath() {
        return picFilePath;
    }

    public void setPicFilePath(String picFilePath) {
        this.picFilePath = picFilePath;
    }

    public Image getAccountPic() {
        return accountPic.get();
    }

    public void setAccountPic(Image accountPic) {
        this.accountPic.set(accountPic);
    }
}
