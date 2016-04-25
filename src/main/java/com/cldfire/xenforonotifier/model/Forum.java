package com.cldfire.xenforonotifier.model;

import java.util.ArrayList;
import java.util.List;

public class Forum {
    private String forum;
    private ForumType type;
    private String protocol;
    private List<Account> accounts = new ArrayList<>();

    public Forum(String forum) {
        this.forum = forum;
    }

    public String getForum() {
        return forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public ForumType getType() {
        return type;
    }

    public void setType(ForumType type) {
        this.type = type;
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
