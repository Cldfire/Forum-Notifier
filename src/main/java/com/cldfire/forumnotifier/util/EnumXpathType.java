package com.cldfire.forumnotifier.util;

public enum EnumXpathType {
    USERPASSLOGINFORM("xpath", "Path to the login form"),
    TWOFACTORLOGINFORM("xpath", "Path to the two factor auth form"),
    PASSWORDFIELDNAME("attribute", "Name or value for password field"),
    USERNAMEFIELDNAME("attribute", "Name or value for the username field"),
    STAYLOGGEDINFIELDNAME("attribute", "Name or value for the checkbox to stay logged in"),
    LOGINBUTTONVALUE("attribute", "Name or value for the login button"),
    TWOFACTORCODEFIELDNAME("attribute", "Name or value for the two factor code field"),
    TRUSTTWOFACTORLOGINFIELDNAME("attribute", "Name or value for the checkbox to trust two factor auth"),
    CONFIRMTWOFACTORBUTTONNAME("attribute", "Name or value for the two factor confirm button"),
    ACCOUNTURL("xpath", "Path to the accounturl"),
    ACCOUNTPIC("xpath", "Path to the accountpic"),
    ACCOUNTNAME("xpath", "Path to the accountname"),
    MESSAGES("xpath", "Path to new conversations count"),
    ALERTS("xpath", "Path to new alerts count"),
    RATINGS("xpath", "Path to ratings count"),
    POSTS("xpath", "Path to post count"),
    FOLLOWINGLIST("xpath", "Path to your following list"),
    FOLLOWERLIST("xpath", "Path to your follower list"),
    FOLLOWERCOUNT("xpath", "Path to your follower count");

    private final String promptText;
    private final String labelText;

    EnumXpathType(final String promptText, final String labelText) {
        this.promptText = promptText;
        this.labelText = labelText;
    }

    public String getLabelText() {
        return labelText;
    }

    public String getPromptText() {
        return promptText;
    }
}
