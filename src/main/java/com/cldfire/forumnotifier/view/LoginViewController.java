package com.cldfire.forumnotifier.view;

import com.cldfire.forumnotifier.ForumNotifier;
import com.cldfire.forumnotifier.model.*;
import com.cldfire.forumnotifier.util.EnumXpathType;
import com.cldfire.forumnotifier.util.ForumsStore;
import com.cldfire.forumnotifier.util.LangUtils;
import com.cldfire.forumnotifier.util.XpathUtils;
import com.cldfire.forumnotifier.util.animations.EnumColorFadeAnimationType;
import com.cldfire.forumnotifier.util.animations.MoveAnimation;
import com.cldfire.forumnotifier.util.animations.NodeAnimationUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewController {
    private ObservableList<XpathDisplayBlock> xpathDisplayBlocks;
    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label softwareConfirmLabel;
    @FXML
    private Button softwareConfirmButton;
    @FXML
    private ChoiceBox softwareBox;
    @FXML
    private Label validateLabel;
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
    @FXML
    private Button xpathEditButton;

    private ListView<XpathDisplayBlock> xpathDisplay;

    private ForumNotifier forumNotifier;
    private static RootLayoutController rootLayoutController;

    private Forum.ForumType tempForumType;
    private String tempSiteUrl;
    private String tempConnProtocol;
    private boolean doesForumExist;
    private HtmlPage twoFactorPage;

    private XpathUtils xpathUtils;
    AccountXpaths accountXpaths;

    @FXML
    public void initialize() {
        RootLayoutController.setLoginViewController(this);
        xpathUtils = new XpathUtils();

        xpathDisplay = new ListView<>();
        xpathDisplay.setCellFactory((ListView<XpathDisplayBlock> l) -> new XpathDisplay());
        xpathDisplay.setTranslateX(400);
        xpathDisplay.setTranslateY(10);
        xpathDisplay.setPrefSize(400, 420);
        xpathDisplay.getStyleClass().add("list-cell");
        xpathDisplay.setVisible(false);
        xpathDisplay.setEditable(false);
        xpathDisplay.setFocusTraversable(false);
        anchorPane.getChildren().add(xpathDisplay);

        errorLabel.setText(LangUtils.translate("login.errorLabel"));
        url.setPromptText(LangUtils.translate("login.url"));
        validateButton.setText(LangUtils.translate("login.validate"));
        username.setPromptText(LangUtils.translate("login.username"));
        password.setPromptText(LangUtils.translate("login.password"));
        loginButton.setText(LangUtils.translate("login.button"));
        authCode.setPromptText(LangUtils.translate("login.authCode"));
        confirmButton.setText(LangUtils.translate("login.confirm"));

        for (Forum.ForumType value : Forum.ForumType.values()) {
            softwareBox.getItems().add(value.getName());
        }

        NodeAnimationUtils.bindColorFadeAnimation(EnumColorFadeAnimationType.COLOR_FADE, url, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));
        NodeAnimationUtils.bindColorFadeAnimation(EnumColorFadeAnimationType.COLOR_FADE, username, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));
        NodeAnimationUtils.bindColorFadeAnimation(EnumColorFadeAnimationType.COLOR_FADE, password, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));
        NodeAnimationUtils.bindColorFadeAnimation(EnumColorFadeAnimationType.COLOR_FADE, authCode, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));


        validateLabel.textProperty().bind(new StringBinding() {
            {
                bind(url.textProperty());
            }

            @Override
            protected String computeValue() {
                errorLabel.setVisible(false);
                if (url.getText().startsWith("http://") || url.getText().startsWith("https://")) {
                    validateButton.setDisable(true);
                    return "Please remove the transfer protocol.";
                }
                else if (url.getText().endsWith("/")) {
                    validateButton.setDisable(true);
                    return "Please remove the forward slash at the end.";
                }
                else if(!url.getText().isEmpty() && !url.getText().matches("^(([a-zA-Z]{1})|([a-zA-Z]{1}[a-zA-Z]{1})|([a-zA-Z]{1}[0-9]{1})|([0-9]{1}[a-zA-Z]{1})|([a-zA-Z0-9][a-zA-Z0-9-_]{1,61}[a-zA-Z0-9]))\\.([a-zA-Z]{2,6}|[a-zA-Z0-9-]{2,30}\\.[a-zA-Z]{2,3})$")){
                    validateButton.setDisable(true);
                    return "Please enter a valid domain.";
                }
                validateButton.setDisable(false);
                return "";
            }
        });
    }

    public void setForumNotifier(final ForumNotifier forumNotifier) {
        this.forumNotifier = forumNotifier;
    }

    public static void setRootLayoutController(RootLayoutController rootLayoutController1) {
        rootLayoutController = rootLayoutController1;
    }

    public void resetForNewLogin() {
        resetLayout();

        softwareBox.setVisible(true);
        softwareConfirmButton.setVisible(true);
        softwareConfirmLabel.setVisible(true);
        validateButton.setVisible(false);
        username.setVisible(false);
        authCode.setVisible(false);
        confirmButton.setVisible(false);
        password.setVisible(false);
        loginButton.setVisible(false);
        errorLabel.setVisible(false);
        url.setVisible(false);
        xpathEditButton.setVisible(false);
        xpathDisplay.setVisible(false);

        softwareBox.getSelectionModel().clearSelection();
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
            imageFile = new File(imageFolder, this.url.getText().replace("/", "").replace(".", "") + "_" + username.getText() + ".img");

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

        final HtmlPage page;
        final HtmlForm loginForm;
        final HtmlSubmitInput loginButton;
        final HtmlCheckBoxInput stayLoggedIn;
        final HtmlTextInput usernameField;
        final HtmlPasswordInput passwordField;


        try {
            page = webClient.getPage(url);

            loginForm = xpathUtils.checkXpathListForForm(accountXpaths.getUserPassLoginForm(), page);

            loginButton = xpathUtils.getSubmitInputFromForm(accountXpaths.getLoginButtonValue(), loginForm);
            stayLoggedIn = xpathUtils.getCheckboxInputFromForm(accountXpaths.getStayLoggedInFieldName(), loginForm);
            usernameField = xpathUtils.getTextInputFromForm(accountXpaths.getUsernameFieldName(), loginForm);
            passwordField = xpathUtils.getPasswordInputFromForm(accountXpaths.getPasswordFieldName(), loginForm);

            stayLoggedIn.setChecked(true);
            usernameField.setValueAttribute(email);
            passwordField.setValueAttribute(password);

            return loginButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loginTwoFactorAuth(final String code, final HtmlPage page) {
        final HtmlForm twoFactorForm;
        final HtmlSubmitInput confirmButton;
        final HtmlCheckBoxInput trustDevice;
        final HtmlTextInput codeField;

        try {
            twoFactorForm = xpathUtils.checkXpathListForForm(accountXpaths.getTwoFactorLoginForm(), page);

            confirmButton = xpathUtils.getSubmitInputFromForm(accountXpaths.getConfirmTwoFactorButtonName(), twoFactorForm);
            codeField = xpathUtils.getTextInputFromForm(accountXpaths.getTwoFactorCodeFieldName(), twoFactorForm);
            trustDevice = xpathUtils.getCheckboxInputFromForm(accountXpaths.getTrustTwoFactorLoginFieldName(), twoFactorForm);

            codeField.setValueAttribute(code);
            trustDevice.setChecked(true);

            confirmButton.click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean validateSite(final String url, final Forum.ForumType forumType) { // TODO: Have this throwExceptionOnFailingStatusCode and then check for cloudflare if it's a 503, rather than ignoring all other status codes
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        final HtmlPage page;
        final HtmlHtml htmlToCheck;

        try {
            page = webClient.getPage(url);

            if (testForCloudflare(page)) { // TODO: Tell user that Cloudflare is causing the delay
                webClient.getCookieManager().addCookie(completeCloudflareBrowserCheck(url));
                validateSite(url, forumType);
            }

            switch (forumType) {
                case XENFORO: {
                    htmlToCheck = page.getFirstByXPath("/html[@id='XenForo']");
                    return htmlToCheck.getId().equals("XenForo");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void resetLayout() {
        password.setLayoutX(300);
        username.setLayoutX(300);
        loginButton.setLayoutX(370);
        authCode.setLayoutX(300);
        confirmButton.setLayoutX(365);
    }

    //private boolean doesAccountExist() { // TODO: Implement this and make sure that the account the user is trying to add does not already exist

    //}

    @FXML
    private void handleXpathEdit() {
        xpathEditButton.setVisible(false);
        if (password.isVisible()) {
            new MoveAnimation(password, 0.1, password.getLayoutX() - 180, password.getLayoutY()).play();
            new MoveAnimation(username, 0.1, username.getLayoutX() - 180, username.getLayoutY()).play();
            new MoveAnimation(loginButton, 0.1, loginButton.getLayoutX() - 180, loginButton.getLayoutY()).play();

            xpathDisplayBlocks = FXCollections.observableArrayList();
            xpathDisplayBlocks.addAll(
                    new XpathDisplayBlock(EnumXpathType.USERPASSLOGINFORM, accountXpaths, accountXpaths.getUserPassLoginForm().get(0)),
                    new XpathDisplayBlock(),
                    new XpathDisplayBlock(EnumXpathType.USERNAMEFIELDNAME, accountXpaths, accountXpaths.getUsernameFieldName().get(0)),
                    new XpathDisplayBlock(EnumXpathType.PASSWORDFIELDNAME, accountXpaths, accountXpaths.getPasswordFieldName().get(0)),
                    new XpathDisplayBlock(EnumXpathType.LOGINBUTTONVALUE, accountXpaths, accountXpaths.getLoginButtonValue().get(0)),
                    new XpathDisplayBlock(EnumXpathType.STAYLOGGEDINFIELDNAME, accountXpaths, accountXpaths.getStayLoggedInFieldName().get(0))
            );
            xpathDisplay.setItems(xpathDisplayBlocks);
            xpathDisplay.setVisible(true);
        }

        if (authCode.isVisible()) {
            new MoveAnimation(authCode, 0.1, authCode.getLayoutX() - 180, authCode.getLayoutY()).play();
            new MoveAnimation(confirmButton, 0.1, confirmButton.getLayoutX() - 180, confirmButton.getLayoutY()).play();

            xpathDisplayBlocks = FXCollections.observableArrayList();
            xpathDisplayBlocks.addAll(
                    new XpathDisplayBlock(EnumXpathType.TWOFACTORLOGINFORM, accountXpaths, accountXpaths.getTwoFactorLoginForm().get(0)),
                    new XpathDisplayBlock(),
                    new XpathDisplayBlock(EnumXpathType.TWOFACTORCODEFIELDNAME, accountXpaths, accountXpaths.getTwoFactorCodeFieldName().get(0)),
                    new XpathDisplayBlock(EnumXpathType.CONFIRMTWOFACTORBUTTONNAME, accountXpaths, accountXpaths.getConfirmTwoFactorButtonName().get(0)),
                    new XpathDisplayBlock(EnumXpathType.TRUSTTWOFACTORLOGINFIELDNAME, accountXpaths, accountXpaths.getTrustTwoFactorLoginFieldName().get(0))
            );
            xpathDisplay.setItems(xpathDisplayBlocks);
            xpathDisplay.setVisible(true);
        }
    }

    @FXML
    private void handleSoftwareSelect() {
        errorLabel.setVisible(false);

        if (softwareBox.getSelectionModel().getSelectedItem() != null) {
            softwareBox.setVisible(false);
            softwareConfirmButton.setVisible(false);
            softwareConfirmLabel.setVisible(false);
            url.setVisible(true);
            validateButton.setVisible(true);

            switch ((String) softwareBox.getSelectionModel().getSelectedItem()) {
                case "XenForo": tempForumType = Forum.ForumType.XENFORO;
                    break;
            }

            accountXpaths = new AccountXpaths(Forum.ForumType.XENFORO);
        }
        else {
            softwareConfirmLabel.setVisible(false);
            errorLabel.setVisible(true);
            errorLabel.setText("Please select something");
        }
    }

    @FXML
    private void handleValidate() {
        if (url.getText().trim().isEmpty()) {
            errorLabel.setText(LangUtils.translate("login.errorLabel.null"));
            errorLabel.setVisible(true);
        } else {
            Runnable validateRunnable = () -> {
                errorLabel.setVisible(false);
                try {
                    if (validateSite("https://" + url.getText(), tempForumType)) {
                        validateButton.setVisible(false);
                        url.setVisible(false);
                        password.setVisible(true);
                        username.setVisible(true);
                        loginButton.setVisible(true);
                        xpathEditButton.setVisible(true);

                        tempConnProtocol = "https";
                        tempSiteUrl = tempConnProtocol + "://" + url.getText();
                    } else if (validateSite("http://" + url.getText(), tempForumType)) {
                        validateButton.setVisible(false);
                        url.setVisible(false);
                        password.setVisible(true);
                        username.setVisible(true);
                        loginButton.setVisible(true);
                        xpathEditButton.setVisible(true);

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
                        xpathDisplay.setVisible(false);
                        xpathEditButton.setVisible(true);
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
                                    Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, accountXpaths.getAccountName()), getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), getAccountPic(getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), accountXpaths.getAccountPic()), accountXpaths);

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
                            Forum addForum = ForumsStore.createForum(url.getText(), tempForumType, tempConnProtocol);
                            Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, accountXpaths.getAccountName()), getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), getAccountPic(getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), accountXpaths.getAccountPic()), accountXpaths);

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
                                Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, accountXpaths.getAccountName()), getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), getAccountPic(getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), accountXpaths.getAccountPic()), accountXpaths);

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
                        Forum addForum = ForumsStore.createForum(url.getText(), tempForumType, tempConnProtocol);
                        Account addAccount = ForumsStore.createAccount(webClient.getCookieManager().getCookies(), getAccountName(tempSiteUrl, accountXpaths.getAccountName()), getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), getAccountPic(getProfileUrl(tempSiteUrl, accountXpaths.getAccountUrl()), accountXpaths.getAccountPic()), accountXpaths);

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
