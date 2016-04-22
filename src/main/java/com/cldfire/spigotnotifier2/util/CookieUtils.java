package com.cldfire.spigotnotifier2.util;

import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.*;
import java.util.Set;

public class CookieUtils { // TODO: Actually finish / use these

    public Set<Cookie> loadCookiesFromFile(String filepath) {
        try {
            ObjectInputStream cookieLoader = new ObjectInputStream(new FileInputStream(filepath));
            cookieLoader.close();
            return (Set<Cookie>) cookieLoader.readObject();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveCookiesToFile(String filepath, Set<Cookie> cookies) {
        try {
            ObjectOutput cookieSaver = new ObjectOutputStream(new FileOutputStream(filepath));
            cookieSaver.writeObject(cookies);
            cookieSaver.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


}
