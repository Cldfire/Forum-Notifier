package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.model.Account;
import com.cldfire.xenforonotifier.model.AccountDisplayBlock;
import com.cldfire.xenforonotifier.util.ForumsStore;
import com.cldfire.xenforonotifier.util.notifications.Notification;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class StatViewController {
    private static ObservableList<Account> accountBlocks;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    @FXML
    private ListView<Account> accountOverview;
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

    public static void addAccountBlock(Account account) {
        accountBlocks.add(account);
        System.out.println(accountBlocks.size());
        System.out.println(accountBlocks);
    }

    @FXML
    private void initialize() {
        accountBlocks = FXCollections.observableArrayList();
        ForumsStore.forums.forEach(f -> f.getAccounts().forEach(a -> accountBlocks.add(a)));

        accountOverview.setCellFactory((ListView<Account> l) -> new AccountDisplayBlock());
        accountOverview.setItems(accountBlocks);
        checkEverythingAtFixedRate();
    }

    private String getXenToken(Account account) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlInput token;

        account.getCookies().forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            page = webClient.getPage("fixme"); // TODO: <------
            token = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[2]/form/div/input[2]");

            webClient.close();
            return token.getValueAttribute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        return null;
    }

    private Map<String, String> getEverything(Account account, String url) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlSpan messages;
        final HtmlSpan alerts;
        final HtmlSpan ratings;
        final HtmlDefinitionDescription posts;
        Map<String, String> values = new HashMap<>();

        // feed the WebClient so that it does what we want it to
        account.getCookies().forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            page = webClient.getPage(url);
            webClient.close();
            messages = page.getFirstByXPath("//*[@id='uix_ConversationsMenu_Counter']/span");
            alerts = page.getFirstByXPath("//*[@id='uix_AlertsMenu_Counter']/span");
            ratings = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[1]/div/div/div/dl/dd/span");
            posts = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[1]/div/div/div/div/dl/dd");

            values.put("messages", messages.asText());
            values.put("alerts", alerts.asText());
            values.put("ratings", ratings.asText());
            values.put("posts", posts.asText());

            return values;
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        values.put("messages", "N/A");
        values.put("alerts", "N/A");
        values.put("ratings", "N/A");
        values.put("posts", "N/A");
        return values;
    } // TODO: Make it so I only have to pass in an account, same for getXenToken

    private void checkEverythingAtFixedRate() { // TODO: I'm very aware this is not going to thread properly atm, will fix in the future
        Runnable getEverythingRunnable = () -> {
            ForumsStore.forums.forEach(f -> {
                System.out.println(ForumsStore.forums.size());
                System.out.println(ForumsStore.forums);
                System.out.println("Forum list had something");

                f.getAccounts().forEach(a -> {
                    System.out.println("There were accounts for that site");
                    System.out.println(a.getName());
                    final Map<String, String> returnedValues = new HashMap<>(getEverything(a, "https://" + f.getUrl()));
                    final Integer newMessagesCount = Integer.parseInt(returnedValues.get("messages"));
                    final Integer newAlertsCount = Integer.parseInt(returnedValues.get("alerts"));

                    if (newMessagesCount > a.getMessageCount()) { // TODO: Get notifications to work when both a message and alert notification needs to be created
                        if (newMessagesCount - a.getMessageCount() == 1) {
                            new Notification("XenForo Notifier", "You have a new message", notifImage).send();
                        } else {
                            new Notification("XenForo Notifier", "You have " + (newMessagesCount - a.getMessageCount()) + " new messages", notifImage).send();
                        }
                    }

                    if (newAlertsCount > a.getAlertCount()) {
                        if (newAlertsCount - a.getAlertCount() == 1) {
                            new Notification("XenForo Notifier", "You have a new alert", notifImage).send();
                        } else {
                            new Notification("XenForo Notifier", "You have " + (newAlertsCount - a.getAlertCount()) + " new alerts", notifImage).send();
                        }
                    }
                    a.setMessageCount(newMessagesCount);
                    a.setAlertCount(newAlertsCount);
                });
            });
            System.out.println("Ran checker");
        };
        // TODO: Provide way to cancel this other than task manager / force quit / whatever *nix does
        scheduler.scheduleAtFixedRate(getEverythingRunnable, 0, 15, SECONDS);
    }
}
