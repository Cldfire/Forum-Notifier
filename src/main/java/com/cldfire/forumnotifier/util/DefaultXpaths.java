package com.cldfire.forumnotifier.util;

import com.cldfire.forumnotifier.model.Forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultXpaths {

    private final Map<String, Object> defaultXpathMaps;

    public DefaultXpaths(Forum.ForumType forumType) {

        defaultXpathMaps = new HashMap<>();

        switch (forumType) {
            case XENFORO: {

                final List<String> twoFactorloginForm = new ArrayList<>();
                twoFactorloginForm.add("//*[@id='content']/div/div/div[2]/form"); // Spigot
                twoFactorloginForm.add("//*[@id='content']/div/div/div[1]/form"); // Hypixel
                twoFactorloginForm.add("//*[@id='content']/div[2]/div/form"); // Minecraft Frontiers

                final List<String> accountUrl = new ArrayList<>();
                accountUrl.add("//*[@id='AccountMenu']/div[1]/ul/li/a"); // Spigot, Hypixel

                final List<String> accountPic = new ArrayList<>();
                accountPic.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[1]/a/img"); // Spigot
                accountPic.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[1]/a/img"); // Hypixel
                accountPic.add("//*[@id='content']/div[2]/div/div[2]/div[1]/div[1]/a/img"); // Minecraft Frontiers
                accountPic.add("//*[@id='content']/div/div/div[2]/div[1]/div[1]/a/img"); // CrystalcraftMC

                final List<String> accountName = new ArrayList<>();
                accountName.add("//*[@id='userBar']/div/div/div/div/ul[2]/li[1]/a/strong[1]"); // Spigot staff
                accountName.add("//*[@id='userBar']/div/div/div/div/ul/li[1]/a/strong[1]"); // Spigot
                accountName.add("//*[@id='userBar']/div/div/div/ul/li[1]/a/strong[1]"); // Hypixel
                accountName.add("//*[@id='navigation']/div/nav/div/ul[2]/li[1]/a/strong[1]"); // Minecraft Frontiers

                final List<String> messages = new ArrayList<>();
                messages.add("//*[@id='uix_ConversationsMenu_Counter']/span"); // Spigot
                messages.add("//*[@id='ConversationsMenu_Counter']/span[1]"); // Hypixel

                final List<String> alerts = new ArrayList<>();
                alerts.add("//*[@id='uix_AlertsMenu_Counter']/span"); // Spigot
                alerts.add("//*[@id='AlertsMenu_Counter']/span[1]"); // Hypixel

                final List<String> ratings = new ArrayList<>();
                ratings.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[6]/dd"); // Spigot staff
                ratings.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[5]/dd"); // Spigot
                ratings.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[2]/div/dl[6]/dd"); // Hypixel
                ratings.add("//*[@id='content']/div[2]/div/div[2]/div[1]/div[2]/div/dl[4]/dd"); // Minecraft Frontiers
                ratings.add("//*[@id='content']/div/div/div[2]/div[1]/div[2]/div/dl[6]/dd"); // CrystalcraftMC

                final List<String> posts = new ArrayList<>();
                posts.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[2]/div/dl[3]/dd"); // Spigot
                posts.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[2]/div/dl[3]/dd"); // Hypixel
                posts.add("//*[@id='content']/div[2]/div/div[2]/div[1]/div[2]/div/dl[3]/dd"); // Minecraft Frontiers
                posts.add("//*[@id='content']/div/div/div[2]/div[1]/div[2]/div/dl[3]/dd"); // CrystalcraftMC

                final List<String> followingList = new ArrayList<>();
                followingList.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[4]/div[1]/div/div[1]/ol"); // Spigot
                followingList.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[6]/div[2]/div[1]/ol"); // Hypixel

                final List<String> followingCount = new ArrayList<>();
                followingCount.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[4]/div[1]/div/h3/a"); // Spigot
                followingCount.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[6]/div[1]/h3/a"); // Hypixel

                final List<String> followerList = new ArrayList<>();
                followerList.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[4]/div[2]/div/div[1]/ol"); // Spigot
                followerList.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[6]/div[2]/div[1]/ol"); // Hypixel

                final List<String> followerCount = new ArrayList<>();
                followerCount.add("//*[@id='content']/div/div/div[2]/div/div[1]/div[4]/div[2]/div/h3/a"); // Spigot
                followerCount.add("//*[@id='content']/div/div/div[1]/div/div[1]/div[6]/div[2]/h3/a"); // Hypixel

                defaultXpathMaps.put("twoFactorLoginFormPaths", twoFactorloginForm);
                defaultXpathMaps.put("accountUrlPaths", accountUrl);
                defaultXpathMaps.put("accountPicPaths", accountPic);
                defaultXpathMaps.put("accountNamePaths", accountName);
                defaultXpathMaps.put("messagePaths", messages);
                defaultXpathMaps.put("alertPaths", alerts);
                defaultXpathMaps.put("ratingPaths", ratings);
                defaultXpathMaps.put("postPaths", posts);
                defaultXpathMaps.put("followingListPaths", followingList);
                defaultXpathMaps.put("followingCountPaths", followingCount);
                defaultXpathMaps.put("followerListPaths", followerList);
                defaultXpathMaps.put("followerCountPaths", followerCount);


                break;
            }
        }
    }
    public Map<String, Object> get() {
        return defaultXpathMaps;
    }
}
