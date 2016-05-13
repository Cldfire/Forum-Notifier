package com.cldfire.forumnotifier.view;

import com.cldfire.forumnotifier.ForumNotifier;
import com.cldfire.forumnotifier.model.Account;
import com.cldfire.forumnotifier.model.Forum;
import com.cldfire.forumnotifier.util.DefaultXpaths;
import com.cldfire.forumnotifier.util.ForumsStore;
import com.cldfire.forumnotifier.util.LangUtils;
import com.cldfire.forumnotifier.util.animations.EnumAnimationType;
import com.cldfire.forumnotifier.util.animations.NodeAnimationUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewController {

    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @FXML
    private TextField url;
    @FXML
    private Button validateButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private TextField authCode;
    @FXML
    private Button confirmButton;
    @FXML
    private Label errorLabel;

    private ForumNotifier forumNotifier;
    private String tempSiteUrl;
    private String temp2faUrl;
    private String tempConnProtocol;
    private boolean doesForumExist;

    private Map<String, Map<String, Object>> defaultXpaths = new HashMap<>(new DefaultXpaths(Forum.ForumType.XENFORO, tempSiteUrl).get());
    private Map<String, Object> accountXpathsMap = new HashMap<>(defaultXpaths.get("accountXpathsMap"));
    private List<String> profileNamePaths = new ArrayList<>((List<String>) accountXpathsMap.get("accountNamePaths"));
    private List<String> profileUrlPaths = new ArrayList<>((List<String>) accountXpathsMap.get("accountUrlPaths"));
    private List<String> profilePicPaths = new ArrayList<>((List<String>) accountXpathsMap.get("accountPicPaths"));

    public void initialize() {
        RootLayoutController.setLoginViewController(this);
        errorLabel.setText(LangUtils.translate("login.errorLabel"));
        url.setPromptText(LangUtils.translate("login.url"));
        validateButton.setText(LangUtils.translate("login.validate"));
        username.setPromptText(LangUtils.translate("login.username"));
        password.setPromptText(LangUtils.translate("login.password"));
        loginButton.setText(LangUtils.translate("login.button"));
        authCode.setPromptText(LangUtils.translate("login.authCode"));
        confirmButton.setText(LangUtils.translate("login.confirm"));

        NodeAnimationUtils.bindFromToAnimation(EnumAnimationType.COLOR_FADE, url, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));
        NodeAnimationUtils.bindFromToAnimation(EnumAnimationType.COLOR_FADE, username, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));
        NodeAnimationUtils.bindFromToAnimation(EnumAnimationType.COLOR_FADE, password, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));
        NodeAnimationUtils.bindFromToAnimation(EnumAnimationType.COLOR_FADE, authCode, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));
    }

    public void setForumNotifier(final ForumNotifier forumNotifier) {
        this.forumNotifier = forumNotifier;
    }

    public void resetForNewLogin() {
        validateButton.setVisible(true);
        username.setVisible(false);
        authCode.setVisible(false);
        confirmButton.setVisible(false);
        password.setVisible(false);
        loginButton.setVisible(false);
        url.setVisible(true);

        url.setText("");
        username.setText("");
        password.setText("");
        authCode.setText("");

        temp2faUrl = "";
        tempConnProtocol = "";
        doesForumExist = false;

        webClient.getCache().clear();
        webClient.getCookieManager().clearCookies();
    }

    private Boolean testForLoggedIn() { // TODO: Add more ways to detect that a user is logged in
        return webClient.getCookieManager().getCookies().size() > 5 || webClient.getCookieManager().getCookie("xf_user") != null || webClient.getCookieManager().getCookie("xf_user") != null;
    } // TODO: Move utility methods to a utility class to maintain some semblance of organization

    public static Boolean testForCloudflare(final HtmlPage page) {
        return page.getTitleText().equalsIgnoreCase("just a moment...");
    }

    public static Cookie completeCloudflareBrowserCheck(final String url) {
        WebClient completeClient = new WebClient(BrowserVersion.CHROME);
        completeClient.getOptions().setCssEnabled(false);
        completeClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        final HtmlPage page;
        final HtmlElement submitButton;
        final HtmlForm challengeForm;

        try {
            page = completeClient.getPage(url);
            completeClient.waitForBackgroundJavaScript(5000);

            submitButton = (HtmlElement) page.createElement("button");
            submitButton.setAttribute("type", "submit");

            challengeForm = (HtmlForm) page.getElementById("challenge-form");
            challengeForm.appendChild(submitButton);
            submitButton.click();

            return completeClient.getCookieManager().getCookie("cf_clearance");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String checkXpathListForString(final List<String> xpaths, final HtmlPage page) {
        for (String x : xpaths) {
            Object htmlElement = page.getFirstByXPath(x);

            if (htmlElement != null) {
                System.out.println(htmlElement.getClass().getTypeName());

                switch (htmlElement.getClass().getTypeName()) {
                    case "com.gargoylesoftware.htmlunit.html.HtmlSpan": {
                        HtmlSpan element = (HtmlSpan) htmlElement;
                        if (element.asText() != null) {
                            return element.asText();
                        }
                    }

                    case "com.gargoylesoftware.htmlunit.html.HtmlStrong": {
                        HtmlStrong element = (HtmlStrong) htmlElement;
                        if (element.asText() != null) {
                            return element.asText();
                        }
                    }

                    case "com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription": {
                        HtmlDefinitionDescription element = (HtmlDefinitionDescription) htmlElement;
                        if (element.asText() != null) {
                            return element.asText();
                        }
                    }

                    case "com.gargoylesoftware.htmlunit.html.HtmlHeader": {
                        HtmlHeader element = (HtmlHeader) htmlElement;
                        if (element.asText() != null) {
                            return element.asText();
                        }
                    }

                    case "com.gargoylesoftware.htmlunit.html.HtmlAnchor": {
                        HtmlAnchor element = (HtmlAnchor) htmlElement;
                        if (element.getAttribute("href") != null) {
                            return element.getAttribute("href");
                        } else if (element.asText() != null) {
                            return element.asText();
                        }
                    }
                }
            }
        }
        return null;
    }

    private String getAccountName(final String url, final List<String> xpaths) {
        final HtmlPage page;

        try {
            page = webClient.getPage(url);
            return checkXpathListForString(xpaths, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getProfileUrl(final String url, final List<String> xpaths) {
        final HtmlPage page;

        try {
            page = webClient.getPage(url);
            return tempSiteUrl + "/" + checkXpathListForString(xpaths, page);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAccountPic(final String url, final List<String> xpaths) {
        final File imageFolder;
        final File imageFile;
        HtmlPage page;

        try {
            imageFolder = new File(ForumNotifier.APP_DIR, "account_images");
            imageFile = new File(imageFolder, this.url.getText() + "_" + username.getText());

            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }

            page = webClient.getPage(url);


            for (String x : xpaths) {
                Object htmlElement = page.getFirstByXPath(x);

                if (htmlElement != null) {
                    switch (htmlElement.getClass().getTypeName()) {
                        case "com.gargoylesoftware.htmlunit.html.HtmlImage": {
                            HtmlImage element = (HtmlImage) htmlElement;
                            element.saveAs(imageFile);

                            return imageFile.getPath();
                        }
                    }
                }
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // TODO: Return some default placeholder image
        }
    }

    private HtmlPage loginToSite(final String url, final String email, final String password) {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);

        HtmlPage page1;
        final HtmlForm loginForm;
        final HtmlSubmitInput loginButton;
        final HtmlCheckBoxInput stayLoggedIn;
        final HtmlTextInput emailField;
        final HtmlPasswordInput passwordField;


        try {
            page1 = webClient.getPage(url);

            loginForm = (HtmlForm) page1.getElementById("pageLogin");
            loginButton = loginForm.getInputByValue("Log in");
            stayLoggedIn = loginForm.getInputByName("remember");
            emailField = loginForm.getInputByName("login");
            passwordField = loginForm.getInputByName("password");

            stayLoggedIn.setChecked(true);
            emailField.setValueAttribute(email);
            passwordField.setValueAttribute(password);

            return loginButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HtmlPage loginTwoFactorAuth(final String url, final String code) {

        final HtmlForm authForm;
        final HtmlSubmitInput confirmButton;
        final HtmlCheckBoxInput trustDevice;
        final HtmlTextInput codeField;
        HtmlPage page;

        try {
            page = webClient.getPage(url);
            authForm = page.getFirstByXPath("/html/body//form[@action='login/two-step']");
            confirmButton = authForm.getInputByName("save");
            codeField = authForm.getInputByName("code");
            trustDevice = authForm.getInputByName("trust");

            codeField.setValueAttribute(code);
            trustDevice.setChecked(true);

            return confirmButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Boolean validateSite(final String url) {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        HtmlPage page;
        final HtmlHtml forumType;

        try {
            page = webClient.getPage(url);

            if (testForCloudflare(page)) { // TODO: Tell user that Cloudflare is causing the delay
                webClient.getCookieManager().addCookie(completeCloudflareBrowserCheck(url));
                page = webClient.getPage(url);
            }

            forumType = page.getFirstByXPath("/html[@id='XenForo']");

            return forumType.getId().equals("XenForo");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //private Boolean doesAccountExist() { // TODO: < ----

    //}

    @FXML
    private void handleValidate() {
        if (url.getText() != "Forum URL") {
            Runnable validateRunnable = () -> {
                errorLabel.setVisible(false);
                try {
                    if (validateSite("https://" + url.getText())) {
                        validateButton.setVisible(false);
                        url.setVisible(false);
                        password.setVisible(true);
                        username.setVisible(true);
                        loginButton.setVisible(true);

                        tempConnProtocol = "https";
                        tempSiteUrl = tempConnProtocol + "://" + url.getText();
                    } else if (validateSite("http://" + url.getText())) {
                        validateButton.setVisible(false);
                        url.setVisible(false);
                        password.setVisible(true);
                        username.setVisible(true);
                        loginButton.setVisible(true);

                        tempConnProtocol = "http";
                        tempSiteUrl = tempConnProtocol + "://" + url.getText();
                    } else {
                        Platform.runLater(() -> {
                            errorLabel.setText("Invalid URL / not a compatible forum"); // TODO: Add a 'continue anyway' override
                            errorLabel.setVisible(true);
                            url.setText("");
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            executor.submit(validateRunnable);
        } else {
            errorLabel.setText(LangUtils.translate("login.errorLabel.null"));
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleLogin() { // TODO: Finalize error handling here, clean up where necessary
        if (username.getText() == "" || password.getText() == "") {
            errorLabel.setText(LangUtils.translate("login.errorLabel.null"));
            errorLabel.setVisible(true);
        } else {
            Runnable loginRunnable = () -> {
                Platform.runLater(() -> errorLabel.setVisible(false));
                HtmlPage postLoginPage = loginToSite(tempSiteUrl + "/login", username.getText(), password.getText());

                if (postLoginPage != null) {
                    if (postLoginPage.getUrl().toString().startsWith(tempSiteUrl + "/login/two-step")) {
                        temp2faUrl = postLoginPage.getUrl().toString();
                        password.setVisible(false);
                        username.setVisible(false);
                        loginButton.setVisible(false);
                        errorLabel.setVisible(false);
                        confirmButton.setVisible(true);
                        authCode.setVisible(true);

                    } else if (testForLoggedIn()) {
                        System.out.println("We are logged in");

                        if (ForumsStore.forums != null) {
                            ForumsStore.forums.forEach(f -> {
                                System.out.println("forums for each");
                                System.out.println(f.getUrl());
                                if (f.getUrl().equalsIgnoreCase(url.getText())) {
                                    System.out.println("found forum");
                                    Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, profileNamePaths), getProfileUrl(tempSiteUrl, profileUrlPaths), getAccountPic(getProfileUrl(tempSiteUrl, profileUrlPaths), profilePicPaths), defaultXpaths);

                                    f.addAccount(addAccount);
                                    ForumsStore.saveForums();

                                    doesForumExist = true;
                                    Platform.runLater(() -> {
                                        StatViewController.addAccountBlock(addAccount);
                                        forumNotifier.showStatView();
                                        resetForNewLogin();
                                    });
                                }
                            });
                        }

                        if (!doesForumExist) {
                            System.out.println("creating forum");
                            Forum addForum = ForumsStore.createForum(url.getText(), Forum.ForumType.XENFORO, tempConnProtocol);
                            System.out.println("Created forum");
                            Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, profileNamePaths), getProfileUrl(tempSiteUrl, profileUrlPaths), getAccountPic(getProfileUrl(tempSiteUrl, profileUrlPaths), profilePicPaths), defaultXpaths);
                            System.out.println("Created account");

                            addForum.addAccount(addAccount);
                            ForumsStore.addForum(addForum);

                            Platform.runLater(() -> {
                                StatViewController.addAccountBlock(addAccount);
                                forumNotifier.showStatView();
                                resetForNewLogin();
                            });
                        }

                    } else if (postLoginPage.getUrl().toString().equals(tempSiteUrl + "/login/login")) {
                        Platform.runLater(() -> {
                            errorLabel.setText(LangUtils.translate("login.errorLabel"));
                            errorLabel.setVisible(true);
                            password.setText("");
                        });
                    } else {
                        Platform.runLater(() -> {
                            errorLabel.setText(LangUtils.translate("login.errorLabel.other"));
                            errorLabel.setVisible(true);
                        });
                    }
                }
            };
            executor.submit(loginRunnable);
        }
    }

    @FXML
    private void handleTwoFactorAuthLogin() { // TODO: Get this to support 2FA via emailed code or whatever that method is
        if (authCode.getText() == "") {
            errorLabel.setText(LangUtils.translate("login.errorLabel.null"));
            errorLabel.setVisible(true);
        } else {
            Runnable twoFactorRunnable = () -> {
                Platform.runLater(() -> errorLabel.setVisible(false));
                loginTwoFactorAuth(temp2faUrl, authCode.getText());

                if (testForLoggedIn()) {
                    System.out.println("We are logged in");

                    if (ForumsStore.forums != null) {
                        ForumsStore.forums.forEach(f -> {
                            System.out.println("forums for each");
                            System.out.println(f.getUrl());
                            if (f.getUrl().equalsIgnoreCase(url.getText())) {
                                System.out.println("found forum");
                                Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, profileNamePaths), getProfileUrl(tempSiteUrl, profileUrlPaths), getAccountPic(getProfileUrl(tempSiteUrl, profileUrlPaths), profilePicPaths), defaultXpaths);

                                f.addAccount(addAccount);
                                ForumsStore.saveForums();

                                doesForumExist = true;
                                Platform.runLater(() -> {
                                    StatViewController.addAccountBlock(addAccount);
                                    forumNotifier.showStatView();
                                    resetForNewLogin();
                                });
                            }
                        });
                    }

                    if (!doesForumExist) {
                        System.out.println("creating forum");
                        Forum addForum = ForumsStore.createForum(url.getText(), Forum.ForumType.XENFORO, tempConnProtocol);
                        System.out.println("Created forum");
                        Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, profileNamePaths), getProfileUrl(tempSiteUrl, profileUrlPaths), getAccountPic(getProfileUrl(tempSiteUrl, profileUrlPaths), profilePicPaths), defaultXpaths);
                        System.out.println("Created account");

                        addForum.addAccount(addAccount);
                        ForumsStore.addForum(addForum);

                        Platform.runLater(() -> {
                            StatViewController.addAccountBlock(addAccount);
                            forumNotifier.showStatView();
                            resetForNewLogin();
                        });
                    }

                } else { // wrong code entered
                    Platform.runLater(() -> {
                        errorLabel.setText(LangUtils.translate("login.errorLabel.authCode"));
                        errorLabel.setVisible(true);
                        authCode.setText("");
                    });
                }
            };
            executor.submit(twoFactorRunnable);
        }
    }
}
