package com.cldfire.forumnotifier.view;

import com.cldfire.forumnotifier.ForumNotifier;
import com.cldfire.forumnotifier.model.Account;
import com.cldfire.forumnotifier.model.Forum;
import com.cldfire.forumnotifier.util.ForumsStore;
import com.cldfire.forumnotifier.util.LangUtils;
import com.cldfire.forumnotifier.util.animations.EnumAnimationType;
import com.cldfire.forumnotifier.util.animations.NodeAnimationUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewController {
    // TODO: Add method to get connection protocol for a site, store said information in the ForumAccount

    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
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
    private String temp2faUrl;
    private String tempConnProtocol;
    private boolean doesForumExist;

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
    }

    private String getAccountName(final String url) { // TODO: Clean up ways to get name (create method to loop through list of xpaths)
        HtmlPage page;
        HtmlStrong username;

        try {
            page = webClient.getPage(url);

            username = page.getFirstByXPath("//*[@id='userBar']/div/div/div/div/ul[2]/li[1]/a/strong[1]");
            if (username != null) {return username.getTextContent();}
            username = page.getFirstByXPath("//*[@id='userBar']/div/div/div/div/ul/li[1]/a/strong[1]");
            if (username != null) {return username.getTextContent();}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getAccountPic(final String url) {
        File imageFolder;
        File imageFile;
        HtmlPage page;
        HtmlAnchor profileUrl;
        HtmlImage image;

        try {
            imageFolder = new File(ForumNotifier.APP_DIR, "account_images");
            imageFile = new File(imageFolder, this.url.getText() + "_" + username.getText());

            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }

            page = webClient.getPage(url);
            profileUrl = page.getFirstByXPath("//*[@id='AccountMenu']/div[1]/ul/li/a");

            page = webClient.getPage(url + "/" + profileUrl.getAttribute("href"));
            image = page.getFirstByXPath("//*[@id='content']/div/div/div[2]/div/div[1]/div[1]/a/img");
            image.saveAs(imageFile);

            return imageFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // TODO: Return some default placeholder image
    }

    private HtmlPage loginToSite(final String url, final String email, final String password) {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

        HtmlPage page1;
        final HtmlForm loginForm;
        final HtmlSubmitInput loginButton;
        final HtmlCheckBoxInput stayLoggedIn;
        final HtmlTextInput emailField;
        final HtmlPasswordInput passwordField;


        try {
            page1 = webClient.getPage(url);

            System.out.println(page1.getUrl());
            System.out.println(page1.getTitleText());
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
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);

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
        WebClient validateClient = new WebClient();
        validateClient.getOptions().setCssEnabled(false);
        validateClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        final HtmlHtml test;

        try {
            page = validateClient.getPage(url);
            test = page.getFirstByXPath("/html[@id='XenForo']");

            return test.getId().equals("XenForo");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //private Boolean doesAccountExist() {

    //}

    @FXML
    private void handleValidate() {
        Runnable validateRunnable = () -> {
            errorLabel.setVisible(false);
            try {
                if (validateSite("https://" + url.getText())) {
                    validateButton.setVisible(false);
                    url.setVisible(false);
                    password.setVisible(true);
                    username.setVisible(true);

                    tempConnProtocol = "https";
                    loginButton.setVisible(true);
                } else if (validateSite("http://" + url.getText())) {
                    validateButton.setVisible(false);
                    url.setVisible(false);
                    password.setVisible(true);
                    username.setVisible(true);

                    tempConnProtocol = "http";
                    loginButton.setVisible(true);
                } else {
                    Platform.runLater(() -> {
                        errorLabel.setText("Invalid URL / not a XenForo forum"); // TODO: Add a 'continue anyway' override
                        errorLabel.setVisible(true);
                        url.setText("");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        executor.submit(validateRunnable);
    }

    @FXML
    private void handleLogin() { // TODO: Finalize error handling here, clean up where necessary
        Runnable loginRunnable = () -> {
            Platform.runLater(() -> errorLabel.setVisible(false));
            HtmlPage postLoginPage = loginToSite(tempConnProtocol + "://" + url.getText() + "/login", username.getText(), password.getText());

            if (postLoginPage != null) {
                if (postLoginPage.getUrl().toString().startsWith(tempConnProtocol + "://" + url.getText() + "/login/two-step")) {
                    temp2faUrl = postLoginPage.getUrl().toString();
                    password.setVisible(false);
                    username.setVisible(false);
                    loginButton.setVisible(false);
                    errorLabel.setVisible(false);
                    confirmButton.setVisible(true);
                    authCode.setVisible(true);

                } else if (testForLoggedIn()) {
                    System.out.println("We are logged in");

                    ForumsStore.forums.forEach(f -> {
                        System.out.println("forums for each");
                        System.out.println(f.getUrl());
                        if (f.getUrl().equalsIgnoreCase(url.getText())) {
                            System.out.println("found forum");
                            Account newAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), getAccountPic(tempConnProtocol + "://" + url.getText()));
                            f.addAccount(newAccount);
                            ForumsStore.saveForums();
                            doesForumExist = true;
                            Platform.runLater(() -> {
                                StatViewController.addAccountBlock(newAccount);
                                forumNotifier.showStatView();
                                resetForNewLogin();
                            });
                        }
                    });

                    if (!doesForumExist) {
                        Forum addForum = ForumsStore.createForum(url.getText(), Forum.ForumType.XENFORO, tempConnProtocol);
                        Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), getAccountPic(tempConnProtocol + "://" + url.getText()));

                        addForum.addAccount(addAccount);
                        ForumsStore.addForum(addForum);

                        Platform.runLater(() -> {
                            StatViewController.addAccountBlock(addAccount);
                            forumNotifier.showStatView();
                            resetForNewLogin();
                        });
                    }
                } else if (postLoginPage.getUrl().toString().equals(tempConnProtocol + "://" + url.getText() + "/login/login")) {
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

    @FXML
    private void handleTwoFactorAuthLogin() { // TODO: Get this to support 2FA via emailed code or whatever that method is
        Runnable twoFactorRunnable = () -> {
            Platform.runLater(() -> errorLabel.setVisible(false));
            loginTwoFactorAuth(temp2faUrl, authCode.getText());

            if (testForLoggedIn()) {
                System.out.println("We are logged in");

                ForumsStore.forums.forEach(f -> {
                    System.out.println("forums for each");
                    System.out.println(f.getUrl());
                    if (f.getUrl().equalsIgnoreCase(url.getText())) {
                        System.out.println("found forum");
                        Account newAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), getAccountPic(tempConnProtocol + "://" + url.getText()));
                        f.addAccount(newAccount);
                        ForumsStore.saveForums();
                        doesForumExist = true;
                        Platform.runLater(() -> {
                            StatViewController.addAccountBlock(newAccount);
                            forumNotifier.showStatView();
                            resetForNewLogin();
                        });
                    }
                });

                if (!doesForumExist) {
                    Forum addForum = ForumsStore.createForum(url.getText(), Forum.ForumType.XENFORO, tempConnProtocol);
                    Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), getAccountPic(tempConnProtocol + "://" + url.getText()));

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
