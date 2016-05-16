package com.cldfire.forumnotifier.view;

import com.cldfire.forumnotifier.ForumNotifier;
import com.cldfire.forumnotifier.model.Account;
import com.cldfire.forumnotifier.model.Forum;
import com.cldfire.forumnotifier.util.DefaultXpaths;
import com.cldfire.forumnotifier.util.ForumsStore;
import com.cldfire.forumnotifier.util.LangUtils;
import com.cldfire.forumnotifier.util.XpathUtils;
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
    private static RootLayoutController rootLayoutController;

    private String tempSiteUrl;
    private String tempConnProtocol;
    private boolean doesForumExist;
    private HtmlPage twoFactorPage;


    private XpathUtils xpathUtils;
    private Map<String, Object> defaultXpaths = new HashMap<>(new DefaultXpaths(Forum.ForumType.XENFORO).get());
    private List<String> twoFactorLoginFormPaths = new ArrayList<>((List<String>) defaultXpaths.get("twoFactorLoginFormPaths"));
    private List<String> profileNamePaths = new ArrayList<>((List<String>) defaultXpaths.get("accountNamePaths"));
    private List<String> profileUrlPaths = new ArrayList<>((List<String>) defaultXpaths.get("accountUrlPaths"));
    private List<String> profilePicPaths = new ArrayList<>((List<String>) defaultXpaths.get("accountPicPaths"));

    public void initialize() {
        RootLayoutController.setLoginViewController(this);
        xpathUtils = new XpathUtils();
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

    public static void setRootLayoutController(RootLayoutController rootLayoutController1) {
        rootLayoutController = rootLayoutController1;
    }

    public void resetForNewLogin() {
        validateButton.setVisible(true);
        username.setVisible(false);
        authCode.setVisible(false);
        confirmButton.setVisible(false);
        password.setVisible(false);
        loginButton.setVisible(false);
        errorLabel.setVisible(false);
        url.setVisible(true);

        url.setText("");
        username.setText("");
        password.setText("");
        authCode.setText("");

        tempSiteUrl = null;
        tempConnProtocol = null;
        twoFactorPage = null;
        doesForumExist = false;

        webClient.getCache().clear();
        webClient.getCookieManager().clearCookies();
    }

    private Boolean testForLoggedIn() { // TODO: Add more ways to detect that a user is logged in
        return webClient.getCookieManager().getCookies().size() > 5 || webClient.getCookieManager().getCookie("xf_user") != null || webClient.getCookieManager().getCookie("xf_user") != null;
    }

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

    private String getAccountName(final String url, final List<String> xpaths) {
        final HtmlPage page;

        try {
            page = webClient.getPage(url);
            return xpathUtils.checkXpathListForString(xpaths, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getProfileUrl(final String url, final List<String> xpaths) {
        final HtmlPage page;

        try {
            page = webClient.getPage(url);
            return tempSiteUrl + "/" + xpathUtils.checkXpathListForHref(xpaths, page);
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
            imageFile = new File(imageFolder, this.url.getText().replace("/", "").replace(".", "") + "_" + username.getText() + ".png");

            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }

            page = webClient.getPage(url);
            xpathUtils.checkXpathListForImage(xpaths, page).saveAs(imageFile);

            return imageFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // TODO: Return some default placeholder image
        }
    }

    private HtmlPage loginToSite(final String url, final String email, final String password) {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);

        HtmlPage page1;
        final HtmlForm loginForm;
        HtmlSubmitInput loginButton;
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

    private void loginTwoFactorAuth(final String code, final HtmlPage page) {
        final HtmlForm authForm;
        final HtmlSubmitInput confirmButton;
        final HtmlCheckBoxInput trustDevice;
        final HtmlTextInput codeField;

        try {
            authForm = xpathUtils.checkXpathListForForm(twoFactorLoginFormPaths, page);

            if (authForm != null) { // TODO: Tell user that form couldn't be found, prompt them to enter correct xpath
                confirmButton = authForm.getInputByName("save");
                codeField = authForm.getInputByName("code");
                trustDevice = authForm.getInputByName("trust");

                codeField.setValueAttribute(code);
                trustDevice.setChecked(true);

                confirmButton.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean validateSite(final String url) { // TODO: Have this throwExceptionOnFailingStatusCode and then check for cloudflare if it's a 503, rather than ignoring all other status codes
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        final HtmlPage page;
        final HtmlHtml forumType;

        try {
            page = webClient.getPage(url);

            if (testForCloudflare(page)) { // TODO: Tell user that Cloudflare is causing the delay
                webClient.getCookieManager().addCookie(completeCloudflareBrowserCheck(url));
                validateSite(url);
            }

            forumType = page.getFirstByXPath("/html[@id='XenForo']");

            return forumType.getId().equals("XenForo");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //private boolean doesAccountExist() { // TODO: < ----

    //}

    @FXML
    private void handleValidate() {
        if (url.getText().trim().isEmpty()) {
            errorLabel.setText(LangUtils.translate("login.errorLabel.null"));
            errorLabel.setVisible(true);
        } else {
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
        }
    }

    @FXML
    private void handleLogin() { // TODO: Finalize error handling here, clean up where necessary
        if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            errorLabel.setText(LangUtils.translate("login.errorLabel.null"));
            errorLabel.setVisible(true);
        } else {
            Runnable loginRunnable = () -> {
                Platform.runLater(() -> errorLabel.setVisible(false));
                final HtmlPage postLoginPage = loginToSite(tempSiteUrl + "/login", username.getText(), password.getText());

                if (postLoginPage != null) {
                    System.out.println(postLoginPage.getTitleText());
                    if (postLoginPage.getUrl().toString().startsWith(tempSiteUrl + "/login/two-step")) {
                        twoFactorPage = postLoginPage;
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
                                        rootLayoutController.changeButtonToAdd();
                                        resetForNewLogin();
                                    });
                                }
                            });
                        }

                        if (!doesForumExist) {
                            Forum addForum = ForumsStore.createForum(url.getText(), Forum.ForumType.XENFORO, tempConnProtocol);
                            Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, profileNamePaths), getProfileUrl(tempSiteUrl, profileUrlPaths), getAccountPic(getProfileUrl(tempSiteUrl, profileUrlPaths), profilePicPaths), defaultXpaths);

                            addForum.addAccount(addAccount);
                            ForumsStore.addForum(addForum);

                            Platform.runLater(() -> {
                                StatViewController.addAccountBlock(addAccount);
                                forumNotifier.showStatView();
                                rootLayoutController.changeButtonToAdd();
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
        if (authCode.getText().trim().isEmpty()) {
            errorLabel.setText(LangUtils.translate("login.errorLabel.null"));
            errorLabel.setVisible(true);
        } else {
            Runnable twoFactorRunnable = () -> {
                Platform.runLater(() -> errorLabel.setVisible(false));
                loginTwoFactorAuth(authCode.getText(), twoFactorPage);

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
                                    rootLayoutController.changeButtonToAdd();
                                    resetForNewLogin();
                                });
                            }
                        });
                    }

                    if (!doesForumExist) {
                        Forum addForum = ForumsStore.createForum(url.getText(), Forum.ForumType.XENFORO, tempConnProtocol);
                        Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, profileNamePaths), getProfileUrl(tempSiteUrl, profileUrlPaths), getAccountPic(getProfileUrl(tempSiteUrl, profileUrlPaths), profilePicPaths), defaultXpaths);

                        addForum.addAccount(addAccount);
                        ForumsStore.addForum(addForum);

                        Platform.runLater(() -> {
                            StatViewController.addAccountBlock(addAccount);
                            forumNotifier.showStatView();
                            rootLayoutController.changeButtonToAdd();
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
