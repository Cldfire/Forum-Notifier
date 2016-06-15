package com.cldfire.forumnotifier.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountXpaths {
    private List<String> userPassLoginForm;
    private List<String> twoFactorLoginForm;
    private List<String> passwordFieldName;
    private List<String> usernameFieldName;
    private List<String> stayLoggedInFieldName;
    private List<String> loginButtonValue;
    private List<String> twoFactorCodeFieldName;
    private List<String> trustTwoFactorLoginFieldName;
    private List<String> confirmTwoFactorButtonName;
    private List<String> accountUrl;
    private List<String> accountPic;
    private List<String> accountName;
    private List<String> messages;
    private List<String> alerts;
    private List<String> ratings;
    private List<String> posts;
    private List<String> followingList;
    private List<String> followerList;
    private List<String> followerCount;

    public AccountXpaths(Forum.ForumType forumType) {
        userPassLoginForm = new ArrayList<>();
        twoFactorLoginForm = new ArrayList<>();
        passwordFieldName = new ArrayList<>();
        usernameFieldName = new ArrayList<>();
        stayLoggedInFieldName = new ArrayList<>();
        loginButtonValue = new ArrayList<>();
        twoFactorCodeFieldName = new ArrayList<>();
        trustTwoFactorLoginFieldName = new ArrayList<>();
        confirmTwoFactorButtonName = new ArrayList<>();
        accountUrl = new ArrayList<>();
        accountPic = new ArrayList<>();
        accountName = new ArrayList<>();
        messages = new ArrayList<>();
        alerts = new ArrayList<>();
        posts = new ArrayList<>();
        ratings = new ArrayList<>();
        followingList = new ArrayList<>();
        followerList = new ArrayList<>();
        followerCount = new ArrayList<>();

        switch (forumType) {
            case XENFORO: {
                userPassLoginForm.add("//*[@id='pageLogin']"); // Every XenForo site in existence, hopefully

                twoFactorLoginForm.add("//*[@id='content']/div/div/div[2]/form"); // Spigot

                /*
                NOT XPATHS, BASED OFF OF ABOVE FORM(S)
                 */
                passwordFieldName.add("name='password'"); // Spigot

                usernameFieldName.add("name='login'"); // Spigot

                stayLoggedInFieldName.add("name='remember'"); // Spigot

                loginButtonValue.add("value='Log in'"); // Spigot

                twoFactorCodeFieldName.add("name='code'"); // Spigot

                trustTwoFactorLoginFieldName.add("name='trust'"); // Spigot

                confirmTwoFactorButtonName.add("name='save'"); // Spigot
                /*
                NOW EVERYTHING IS XPATHS
                 */

                accountUrl.add("//*[@id='AccountMenu']/div[1]/ul/li/a"); // Spigot, Hypixel

                accountPic.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[1]/a/img"); // Spigot
                accountPic.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[1]/a/img"); // Hypixel
                accountPic.add("//*[@id='content']/div[2]/div/div[2]/div[1]/div[1]/a/img"); // Minecraft Frontiers
                accountPic.add("//*[@id='content']/div/div/div[2]/div[1]/div[1]/a/img"); // CrystalcraftMC

                accountName.add("//*[@id='userBar']/div/div/div/div/ul[2]/li[1]/a/strong[1]"); // Spigot staff
                accountName.add("//*[@id='userBar']/div/div/div/div/ul/li[1]/a/strong[1]"); // Spigot
                accountName.add("//*[@id='userBar']/div/div/div/ul/li[1]/a/strong[1]"); // Hypixel
                accountName.add("//*[@id='navigation']/div/nav/div/ul[2]/li[1]/a/strong[1]"); // Minecraft Frontiers

                messages.add("//*[@id='ConversationsMenu_Counter']/span[1]"); // Spigot, Hypixel

                alerts.add("//*[@id='AlertsMenu_Counter']/span[1]"); // Spigot, Hypixel

                ratings.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[6]/dd"); // Spigot staff
                ratings.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[5]/dd"); // Spigot
                ratings.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[2]/div/dl[6]/dd"); // Hypixel
                ratings.add("//*[@id='content']/div[2]/div/div[2]/div[1]/div[2]/div/dl[4]/dd"); // Minecraft Frontiers
                ratings.add("//*[@id='content']/div/div/div[2]/div[1]/div[2]/div/dl[6]/dd"); // CrystalcraftMC

                posts.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[3]/dd"); // Spigot
                posts.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[2]/div/dl[3]/dd"); // Hypixel
                posts.add("//*[@id='content']/div[2]/div/div[2]/div[1]/div[2]/div/dl[3]/dd"); // Minecraft Frontiers
                posts.add("//*[@id='content']/div/div/div[2]/div[1]/div[2]/div/dl[3]/dd"); // CrystalcraftMC

                followingList.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[4]/div[1]/div/div[1]/ol"); // Spigot
                followingList.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[6]/div[2]/div[1]/ol"); // Hypixel

                followerList.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[4]/div[2]/div/div[1]/ol"); // Spigot
                followerList.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[6]/div[2]/div[1]/ol"); // Hypixel

                followerCount.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[4]/div[2]/div/h3/a"); // Spigot
                followerCount.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[6]/div[2]/h3/a"); // Hypixel


                break;
            }
        }
    }

    public AccountXpaths(AccountXpaths accountXpaths) {
        userPassLoginForm = new ArrayList<>();
        twoFactorLoginForm = new ArrayList<>();
        passwordFieldName = new ArrayList<>();
        usernameFieldName = new ArrayList<>();
        stayLoggedInFieldName = new ArrayList<>();
        loginButtonValue = new ArrayList<>();
        twoFactorCodeFieldName = new ArrayList<>();
        trustTwoFactorLoginFieldName = new ArrayList<>();
        confirmTwoFactorButtonName = new ArrayList<>();
        accountUrl = new ArrayList<>();
        accountPic = new ArrayList<>();
        accountName = new ArrayList<>();
        messages = new ArrayList<>();
        alerts = new ArrayList<>();
        posts = new ArrayList<>();
        ratings = new ArrayList<>();
        followingList = new ArrayList<>();
        followerList = new ArrayList<>();
        followerCount = new ArrayList<>();


        setUserPassLoginForm(accountXpaths.getUserPassLoginForm());
        setTwoFactorLoginForm(accountXpaths.getTwoFactorLoginForm());
        setPasswordFieldName(accountXpaths.getPasswordFieldName());
        setUsernameFieldName(accountXpaths.getUsernameFieldName());
        setStayLoggedInFieldName(accountXpaths.getStayLoggedInFieldName());
        setLoginButtonValue(accountXpaths.getLoginButtonValue());
        setTwoFactorCodeFieldName(accountXpaths.getTwoFactorCodeFieldName());
        setTrustTwoFactorLoginFieldName(accountXpaths.getTrustTwoFactorLoginFieldName());
        setConfirmTwoFactorButtonName(accountXpaths.getConfirmTwoFactorButtonName());
        setAccountUrl(accountXpaths.getAccountUrl());
        setAccountPic(accountXpaths.getAccountPic());
        setAccountName(accountXpaths.getAccountName());
        setMessages(accountXpaths.getMessages());
        setAlerts(accountXpaths.getAlerts());
        setPosts(accountXpaths.getPosts());
        setRatings(accountXpaths.getRatings());
        setFollowerList(accountXpaths.getFollowerList());
        setFollowerList(accountXpaths.getFollowerList());
        setFollowerCount(accountXpaths.getFollowerCount());
    }

    public AccountXpaths(
            List<String> userPassLoginForm,
            List<String> twoFactorLoginForm,
            List<String> passwordFieldName,
            List<String> usernameFieldName,
            List<String> stayLoggedInFieldName,
            List<String> loginButtonValue,
            List<String> twoFactorCodeFieldName,
            List<String> trustTwoFactorLoginFieldName,
            List<String> confirmTwoFactorButtonName,
            List<String> accountUrl,
            List<String> accountPic,
            List<String> accountName,
            List<String> messages,
            List<String> alerts,
            List<String> ratings,
            List<String> posts,
            List<String> followingList,
            List<String> followerList,
            List<String> followerCount
    ) {
        this.userPassLoginForm = userPassLoginForm;
        this.twoFactorLoginForm = twoFactorLoginForm;
        this.passwordFieldName = passwordFieldName;
        this.usernameFieldName = usernameFieldName;
        this.stayLoggedInFieldName = stayLoggedInFieldName;
        this.loginButtonValue = loginButtonValue;
        this.twoFactorCodeFieldName = twoFactorCodeFieldName;
        this.trustTwoFactorLoginFieldName = trustTwoFactorLoginFieldName;
        this.confirmTwoFactorButtonName = confirmTwoFactorButtonName;
        this.accountUrl = accountUrl;
        this.accountPic = accountPic;
        this.accountName = accountName;
        this.messages = messages;
        this.alerts = alerts;
        this.posts = posts;
        this.ratings = ratings;
        this.followingList = followingList;
        this.followerList = followerList;
        this.followerCount = followerCount;
    }

    /*
    Getters
     */

    public List<String> getRatings() {
        return ratings;
    }

    public List<String> getPosts() {
        return posts;
    }

    public List<String> getAccountName() {
        return accountName;
    }

    public List<String> getAccountPic() {
        return accountPic;
    }

    public List<String> getAccountUrl() {
        return accountUrl;
    }

    public List<String> getAlerts() {
        return alerts;
    }

    public List<String> getConfirmTwoFactorButtonName() {
        return confirmTwoFactorButtonName;
    }

    public List<String> getFollowerCount() {
        return followerCount;
    }

    public List<String> getFollowerList() {
        return followerList;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public List<String> getLoginButtonValue() {
        return loginButtonValue;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getPasswordFieldName() {
        return passwordFieldName;
    }

    public List<String> getStayLoggedInFieldName() {
        return stayLoggedInFieldName;
    }

    public List<String> getTrustTwoFactorLoginFieldName() {
        return trustTwoFactorLoginFieldName;
    }

    public List<String> getTwoFactorCodeFieldName() {
        return twoFactorCodeFieldName;
    }

    public List<String> getTwoFactorLoginForm() {
        return twoFactorLoginForm;
    }

    public List<String> getUsernameFieldName() {
        return usernameFieldName;
    }

    public List<String> getUserPassLoginForm() {
        return userPassLoginForm;
    }

    public Map<String, List<String>> getXpathsMap() {
        Map<String, List<String>> returnMap = new HashMap<>();

        returnMap.put("userPassLoginForm", userPassLoginForm);
        returnMap.put("twoFactorLoginForm", twoFactorLoginForm);
        returnMap.put("passwordFieldName", passwordFieldName);
        returnMap.put("usernameFieldName", usernameFieldName);
        returnMap.put("stayLoggedInFieldName", stayLoggedInFieldName);
        returnMap.put("loginButtonValue", loginButtonValue);
        returnMap.put("twoFactorCodeFieldName", twoFactorCodeFieldName);
        returnMap.put("trustTwoFactorLoginFieldName", trustTwoFactorLoginFieldName);
        returnMap.put("confirmTwoFactorButtonName", confirmTwoFactorButtonName);
        returnMap.put("accountUrl", accountUrl);
        returnMap.put("accountPic", accountPic);
        returnMap.put("accountName", accountName);
        returnMap.put("messages", messages);
        returnMap.put("alerts", alerts);
        returnMap.put("posts", posts);
        returnMap.put("ratings", ratings);
        returnMap.put("followingList", followingList);
        returnMap.put("followerList", followerList);
        returnMap.put("followerCount", followerCount);

        return returnMap;
    }

    /*
    Setters
     */

    public void setAlerts(List<String> alerts) {
        this.alerts = alerts;
    }

    public void setAccountUrl(List<String> accountUrl) {
        this.accountUrl = accountUrl;
    }

    public void setRatings(List<String> ratings) {
        this.ratings = ratings;
    }

    public void setAccountName(List<String> accountName) {
        this.accountName = accountName;
    }

    public void setAccountPic(List<String> accountPic) {
        this.accountPic = accountPic;
    }

    public void setConfirmTwoFactorButtonName(List<String> confirmTwoFactorButtonName) {
        this.confirmTwoFactorButtonName = confirmTwoFactorButtonName;
    }

    public void setFollowerCount(List<String> followerCount) {
        this.followerCount = followerCount;
    }

    public void setFollowerList(List<String> followerList) {
        this.followerList = followerList;
    }

    public void setFollowingList(List<String> followingList) {
        this.followingList = followingList;
    }

    public void setLoginButtonValue(List<String> loginButtonValue) {
        this.loginButtonValue = loginButtonValue;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setPasswordFieldName(List<String> passwordFieldName) {
        this.passwordFieldName = passwordFieldName;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public void setStayLoggedInFieldName(List<String> stayLoggedInFieldName) {
        this.stayLoggedInFieldName = stayLoggedInFieldName;
    }

    public void setTrustTwoFactorLoginFieldName(List<String> trustTwoFactorLoginFieldName) {
        this.trustTwoFactorLoginFieldName = trustTwoFactorLoginFieldName;
    }

    public void setTwoFactorCodeFieldName(List<String> twoFactorCodeFieldName) {
        this.twoFactorCodeFieldName = twoFactorCodeFieldName;
    }

    public void setTwoFactorLoginForm(List<String> twoFactorLoginForm) {
        this.twoFactorLoginForm = twoFactorLoginForm;
    }

    public void setUsernameFieldName(List<String> usernameFieldName) {
        this.usernameFieldName = usernameFieldName;
    }

    public void setUserPassLoginForm(List<String> userPassLoginForm) {
        this.userPassLoginForm = userPassLoginForm;
    }
}
