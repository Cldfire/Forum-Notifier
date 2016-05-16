package com.cldfire.forumnotifier.model;

import com.cldfire.forumnotifier.util.DefaultXpaths;
import com.cldfire.forumnotifier.util.ForumsStore;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Account { // TODO: Make sure all this becomes thread safe at some point or another
    private final StringProperty name;
    private final StringProperty alertCountProperty;
    private final StringProperty messageCountProperty;
    private final StringProperty postCountProperty;
    private final StringProperty postiveRatingCountProperty;
    private final ObjectProperty<Image> accountPic;

    private Set<Cookie> cookies;
    private String profileUrl;
    private String picFilePath;
    private Map<String, Object> xpathMaps;

    private AccountDisplayBlock displayBlock;

    public Account(Map<String, Object> accountData) {
        this.name = new SimpleStringProperty((String) accountData.get("name"));
        alertCountProperty = new SimpleStringProperty();
        messageCountProperty = new SimpleStringProperty();
        postCountProperty = new SimpleStringProperty();
        postiveRatingCountProperty = new SimpleStringProperty();
        this.accountPic = new SimpleObjectProperty<>(new Image("file:" + accountData.get("picFilePath")));

        this.cookies = (Set<Cookie>) accountData.get("cookies");
        profileUrl = (String) accountData.get("profileUrl");
        picFilePath = (String) accountData.get("picFilePath");

        try {
            xpathMaps = new HashMap<>((Map<String, Object>) accountData.get("xpathMaps"));
        } catch (NullPointerException e) {
            xpathMaps = new DefaultXpaths(Forum.ForumType.XENFORO).get();
        }

        displayBlock = new AccountDisplayBlock();
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

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        displayBlock.setAccountName(name);
    }

    public void setAlertCount(String alertCount) {
        this.alertCountProperty.set(alertCount);
        displayBlock.setAlertCount(alertCount);
    }

    public Integer getAlertCount() {
        if (alertCountProperty.get() != null) {
            return Integer.parseInt(alertCountProperty.get());
        }
        return null;
    }

    public void setMessageCount(String messageCount) {
        this.messageCountProperty.set(messageCount);
        displayBlock.setMessageCount(messageCount);
    }

    public Integer getMessageCount() {
        if (messageCountProperty.get() != null) {
            return Integer.parseInt(messageCountProperty.get());
        }
        return null;
    }

    public void setPostCount(String postCount) {
        this.postCountProperty.set(postCount);
        displayBlock.setPostCount(postCount);
    }

    public String getPostCount() {
        return postCountProperty.get();
    }

    public void setPositiveRatingCount(String positiveRatingCount) {
        this.postiveRatingCountProperty.set(positiveRatingCount);
        displayBlock.setPositiveRatingCount(positiveRatingCount);
    }

    public String getPositiveRatingCount() {
        return postiveRatingCountProperty.get();
    }

    public Image getAccountPic() {
        return accountPic.get();
    }

    public void setAccountPic(Image accountPic) {
        this.accountPic.set(accountPic);
        displayBlock.setAccountPic(accountPic);
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPicFilePath() {
        return picFilePath;
    }

    public void setPicFilePath(String picFilePath) {
        this.picFilePath = picFilePath;
    }

    public Map<String, Object> getAccountXpathsMap() {
        return xpathMaps;
    }

    public AccountDisplayBlock getDisplayBlock() {
        return displayBlock;
    }
}
