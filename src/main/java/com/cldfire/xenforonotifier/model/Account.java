package com.cldfire.xenforonotifier.model;

import com.cldfire.xenforonotifier.util.ForumsStore;
import com.cldfire.xenforonotifier.view.LoginViewController;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.beans.property.*;
import javafx.scene.image.Image;

import java.util.Set;

public class Account { // TODO: Make sure all this becomes thread safe at some point or another
    private Set<Cookie> cookies;
    private final StringProperty name;
    private String connProtocol;

    private final IntegerProperty alertCount;
    private final IntegerProperty messageCount;
    //private final ObjectProperty<Image> favicon;

    public Account(Set<Cookie> cookies, String name, String connProtocol) {
        this.cookies = cookies;
        this.name = new SimpleStringProperty(name);
        this.connProtocol = connProtocol;

        alertCount = new SimpleIntegerProperty(0);
        messageCount = new SimpleIntegerProperty(0);
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

    public String getConnProtocol() {
        return connProtocol;
    }

    public void setConnProtocol(String connProtocol) {
        this.connProtocol = connProtocol;
    }

    public Integer getAlertCount() {
        return alertCount.get();
    }

    public void setAlertCount(int alertCount) {
        this.alertCount.set(alertCount);
    }

    public Integer getMessageCount() {
        return messageCount.get();
    }

    public void setMessageCount(int messageCount) {
        this.messageCount.set(messageCount);
    }

    //public Image getFavicon() {
    //    return favicon.get();
    //}

   // public void setFavicon(Image favicon) {
    //    this.favicon.set(favicon);
    //}
}
