package com.cldfire.forumnotifier.view;

import com.cldfire.forumnotifier.model.Account;
import com.cldfire.forumnotifier.model.AccountDisplay;
import com.cldfire.forumnotifier.model.DetailedDisplay;
import com.cldfire.forumnotifier.util.ForumsStore;
import com.cldfire.forumnotifier.util.XpathUtils;
import com.cldfire.forumnotifier.util.notifications.Notification;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class StatViewController {
    private static ObservableList<Account> accountBlocks; // TODO: make sure this is thread-safe
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private XpathUtils xpathUtils;

    @FXML
    private ListView<Account> accountOverview;
    @FXML
    private AnchorPane detailedView;

    // TODO start: This is temp right? Eventually these will be entirely dynamic
    private BufferedImage notifImage = getTempNotifImage();

    private BufferedImage getTempNotifImage() {
        try {
            return ImageIO.read(ClassLoader.getSystemResource("images/notification-bell.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    // TODO end

    public static void addAccountBlock(final Account account) {
        accountBlocks.add(account);
        System.out.println(accountBlocks.size());
        System.out.println(accountBlocks);
    }

    @FXML
    private void initialize() {
        xpathUtils = new XpathUtils();
        accountBlocks = FXCollections.observableArrayList();
        ForumsStore.forums.forEach(f -> f.getAccounts().forEach(a -> accountBlocks.add(a)));

        accountOverview.setCellFactory((ListView<Account> l) -> new AccountDisplay());
        accountOverview.setItems(accountBlocks);
        detailedView.getChildren().add(new DetailedDisplay().get());
        //checkEverythingAtFixedRate();
    }

    private String getXenToken(final Account account) { // TODO: Re-write this if it ever gets used
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlInput token;

        account.getCookies().forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            page = webClient.getPage(account.getForum().getProtocol() + "://" + account.getForum());
            token = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[2]/form/div/input[2]");

            webClient.close();
            return token.getValueAttribute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        return null;
    }

    private Map<String, String> getEverything(final Account a) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        HtmlPage page;
        Map<String, String> values = new HashMap<>();

        // feed the WebClient so that it does what we want it to
        System.out.println(a.getCookies());
        a.getCookies().forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            Map<String, Object> xpaths = new HashMap<>(a.getAccountXpathsMap());
            String url = a.getProfileUrl(); // TODO: Don't use one URL

            page = webClient.getPage(url);

            if (LoginViewController.testForCloudflare(page)) { // TODO: Tell user that Cloudflare is causing the delay
                Cookie cookie = LoginViewController.completeCloudflareBrowserCheck(url);
                webClient.getCookieManager().addCookie(cookie);
                a.addCookie(cookie);
                System.out.println(a.getCookies());
                page = webClient.getPage(url);
            }


            webClient.close();

            values.put("messages", xpathUtils.checkXpathListForString((List<String>) xpaths.get("messagePaths"), page));
            values.put("alerts", xpathUtils.checkXpathListForString((List<String>) xpaths.get("alertPaths"), page));
            values.put("posts", xpathUtils.checkXpathListForString((List<String>) xpaths.get("postPaths"), page));
            values.put("ratings", xpathUtils.checkXpathListForString((List<String>) xpaths.get("ratingPaths"), page));

            return values;
        } catch (Exception e) {
            e.printStackTrace();

        }
        webClient.close();
        values.put("N/A", "N/A");
        return values;
    }

    private void checkEverythingAtFixedRate() { // TODO: I'm very aware this is not going to thread properly atm, will fix in the future
        Runnable getEverythingRunnable = () -> {
            ForumsStore.forums.forEach(f -> {
                System.out.println("Forum list had something");
                System.out.println("Size: " + ForumsStore.forums.size());

                f.getAccounts().forEach(a -> {
                    System.out.println("There were accounts for that site");
                    System.out.println(a.getName());
                    try {
                        System.out.println("Handling " + a.getName() + " @ " + a.getForum().getUrl());
                        final Map<String, String> returnedValues = new HashMap<>(getEverything(a));

                        try {
                            final Integer newMessagesCount = Integer.parseInt(returnedValues.get("messages"));
                            final Integer newAlertsCount = Integer.parseInt(returnedValues.get("alerts"));
                            final Integer postCount = Integer.parseInt(returnedValues.get("posts").replace(",", ""));
                            final Integer positiveRatingCount = Integer.parseInt(returnedValues.get("ratings").replace(",", ""));

                            if (a.getMessageCount() != null && a.getAlertCount() != null) {
                                if (newMessagesCount > a.getMessageCount()) { // TODO: Get notifications to work when both a message and alert notification needs to be created
                                    if (newMessagesCount - a.getMessageCount() == 1) {
                                        new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have a new message", notifImage).send();
                                    } else {
                                        new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have " + (newMessagesCount - a.getMessageCount()) + " new messages", notifImage).send();
                                    }
                                }

                                if (newAlertsCount > a.getAlertCount()) {
                                    if (newAlertsCount - a.getAlertCount() == 1) {
                                        new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have a new alert", notifImage).send();
                                    } else {
                                        new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have " + (newAlertsCount - a.getAlertCount()) + " new alerts", notifImage).send();
                                    }
                                }

                            } else {

                                if (newMessagesCount > 0) {
                                    if (newMessagesCount > 1) {
                                        new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have " + newMessagesCount + " new messages", notifImage).send();
                                    }
                                    new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have a new message", notifImage).send();
                                }

                                if (newAlertsCount > 0) {
                                    if (newAlertsCount > 1) {
                                        new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have " + newAlertsCount + " new alerts", notifImage).send();
                                    }
                                    new Notification(a.getName() + " @ " + a.getForum().getUrl(), "You have a new alert", notifImage).send();
                                }
                            }

                            Platform.runLater(() -> {
                                a.setMessageCount(newMessagesCount.toString());
                                a.setAlertCount(newAlertsCount.toString());
                                a.setPostCount(postCount.toString());
                                a.setPositiveRatingCount(positiveRatingCount.toString());
                            });
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Platform.runLater(() -> {
                                a.setMessageCount("N/A");
                                a.setAlertCount("N/A");
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            System.out.println("Ran checker");
        };
        // TODO: Provide way to cancel this other than task manager / force quit / whatever *nix does
        scheduler.scheduleAtFixedRate(getEverythingRunnable, 0, 30, SECONDS);
    }
}
