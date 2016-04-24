package com.cldfire.xenforonotifier.util;

import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.*;
import java.util.Set;

public class CookieUtils { // TODO: Use these

    public Set<Cookie> loadCookiesFromFile(String filename) {
        try {
            ObjectInputStream cookieLoader = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + ".xenforonotifier/cookies/" + filename));
            cookieLoader.close();
            return (Set<Cookie>) cookieLoader.readObject();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveCookiesToFile(String filename, Set<Cookie> cookies) {
        try {
            ObjectOutput cookieSaver = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.home") + ".xenforonotifier/cookies/" + filename));
            cookieSaver.writeObject(cookies);
            cookieSaver.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


}
