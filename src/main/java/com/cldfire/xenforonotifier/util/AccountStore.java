package com.cldfire.xenforonotifier.util;

import com.cldfire.xenforonotifier.model.Account;
import com.cldfire.xenforonotifier.model.Forum;

public class AccountStore {

    public static void test() {
        Account account = new Account();
        account.setName("ScruffyRules");
        account.setCookies(null);
        Forum forum = new Forum("www.spigotmc.org/");
        forum.setProtocol("https");
        forum.addAccount(account);
    }
}