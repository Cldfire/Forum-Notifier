package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.util.notifications.EnumImage;
import com.cldfire.xenforonotifier.util.notifications.Notification;
import com.cldfire.xenforonotifier.util.notifications.NotificationUtils;
import com.cldfire.xenforonotifier.util.Settings;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class StatViewController {
    @FXML
    private Label newMessagesField;
    @FXML
    private Label newAlertsField;
    @FXML
    private Label ratingField;
    @FXML
    private Label postCountField;

    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private Integer messagesCount = 0;
    private Integer alertsCount = 0;

    @FXML
    private void initialize() {
        checkEverythingAtFixedRate();
    }

    private String getXenToken(String url, Set<Cookie> cookies) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlInput token;

        cookies.forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            page = webClient.getPage(url);
            token = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[2]/form/div/input[2]");

            webClient.close();
            return token.getValueAttribute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        return null;
    }

    private Map<String, String> getEverything(String url, Set<Cookie> cookies) {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlSpan messages;
        final HtmlSpan alerts;
        final HtmlSpan ratings;
        final HtmlDefinitionDescription posts;

        // feed the WebClient so that it does what we want it to
        cookies.forEach(c -> webClient.getCookieManager().addCookie(c));

        try {
            page = webClient.getPage(url);
            messages = page.getFirstByXPath("//*[@id='uix_ConversationsMenu_Counter']/span");
            alerts = page.getFirstByXPath("//*[@id='uix_AlertsMenu_Counter']/span");
            ratings = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[1]/div/div/div/dl/dd/span");
            posts = page.getFirstByXPath("//*[@id='XenForo']/body/div[1]/aside[2]/div/div/div[1]/div[1]/div/div/div/div/dl/dd");

            Map<String, String> values = new HashMap<>();
            values.put("messages", messages.asText());
            values.put("alerts", alerts.asText());
            values.put("ratings", ratings.asText());
            values.put("posts", posts.asText());

            webClient.close();
            return values;
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        return null;
    }

    private void checkEverythingAtFixedRate() {
        Runnable getEverythingRunnable = () -> {
            // TODO: Verify that returnedValues isn't null
            final Map<String, String> returnedValues = new HashMap<>(getEverything("https://" + Settings.get("website.baseurl"), LoginViewController.getCookies()));
            final Integer newMessagesCount = Integer.parseInt(returnedValues.get("messages"));
            final Integer newAlertsCount = Integer.parseInt(returnedValues.get("alerts"));
            BufferedImage notifImage = NotificationUtils.imageFromPath(EnumImage.ALERT_BELL);

            if (newMessagesCount > messagesCount) { // TODO: Get notifications to work when both a message and alert notification needs to be created
                if (newMessagesCount - messagesCount == 1) {
                    new Notification("XenForo Notifier", "You have a new message", notifImage).send();
                } else {
                    new Notification("XenForo Notifier", "You have " + (newMessagesCount - messagesCount) + " new messages", notifImage).send();
                }
            }

            if (newAlertsCount > alertsCount) {
                if (newAlertsCount - alertsCount == 1) {
                    new Notification("XenForo Notifier", "You have a new alert", notifImage).send();
                } else {
                    new Notification("XenForo Notifier", "You have " + (newAlertsCount - alertsCount) + " new alerts", notifImage).send();
                }
            }

            messagesCount = newMessagesCount;
            alertsCount = newAlertsCount;

            Platform.runLater(() -> {
                try {
                    newMessagesField.setText(returnedValues.get("messages"));
                    newAlertsField.setText(returnedValues.get("alerts"));
                    ratingField.setText(returnedValues.get("ratings"));
                    postCountField.setText(returnedValues.get("posts"));
                } catch(Exception e) {
                    // TODO: returnedValues must have been null, show some sorta error message here
                }
            });
            System.out.println("Ran checker");
        };
        // TODO: Provide way to cancel this other than task manager / force quit / whatever *nix does
        scheduler.scheduleAtFixedRate(getEverythingRunnable, 0, 15, SECONDS);
    }
}
