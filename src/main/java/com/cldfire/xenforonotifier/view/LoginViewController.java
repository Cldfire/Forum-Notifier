package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.util.LangUtils;
import com.cldfire.xenforonotifier.model.ForumAccount;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewController {
    // TODO: Add method to get connection protocol for a site, store said information in the ForumAccount

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

    private XenForoNotifier xenForoNotifier;
    public static Map<String, List<ForumAccount>> accounts = new HashMap<>();
    public static Set<String> websiteList = new HashSet<>();
    private String temp2faUrl;
    private String tempConnProtocol;

    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    public void initialize() {
        errorLabel.setText(LangUtils.translate("login.errorLabel"));
        url.setPromptText(LangUtils.translate("login.url"));
        validateButton.setText(LangUtils.translate("login.validate"));
        username.setPromptText(LangUtils.translate("login.username"));
        password.setPromptText(LangUtils.translate("login.password"));
        loginButton.setText(LangUtils.translate("login.button"));
        authCode.setPromptText(LangUtils.translate("login.authCode"));
        confirmButton.setText(LangUtils.translate("login.confirm"));
        url.requestFocus();
    }

    public void setXenForoNotifier(XenForoNotifier xenForoNotifier) {
        this.xenForoNotifier = xenForoNotifier;
    }

    private Boolean testForLoggedIn() { // TODO: Add more ways to detect that a user is logged in
        return webClient.getCookieManager().getCookies().size() > 5 || webClient.getCookieManager().getCookie("xf_user") != null || webClient.getCookieManager().getCookie("xf_user") != null;
    }

    private String getAccountName(String url) { // TODO: Get this working
        WebClient getNameClient = new WebClient();
        getNameClient.getOptions().setCssEnabled(false);
        getNameClient.getOptions().setJavaScriptEnabled(false);

        final HtmlPage page;
        HtmlStrong username;

        return null;
    }

    private HtmlPage loginToSite(String url, String email, String password) {
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

    private HtmlPage loginTwoFactorAuth(String url, String code) {
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

    private Boolean validateSite(String url) {
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
                    if (accounts.get(url.getText()) == null) { // no ArrayList exists for this forum, create one
                        System.out.println("Creating arraylist");
                        ArrayList<ForumAccount> addAccount = new ArrayList<>();
                        addAccount.add(new ForumAccount(url.getText(), webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), tempConnProtocol)); // TODO: Make sure this uses correct protocol

                        accounts.put(url.getText(), addAccount);
                        System.out.println("Account has been added to a new arraylist");
                    } else { // there was already an ArrayList for that forum
                        System.out.println("Arraylist already existed");
                        ArrayList<ForumAccount> addAccounts = new ArrayList<>();
                        accounts.get(url.getText()).forEach(addAccounts::add);
                        addAccounts.add(new ForumAccount(url.getText(), webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), tempConnProtocol));

                        accounts.replace(url.getText(), addAccounts);
                    }

                    if (!websiteList.contains(url.getText())) {
                        System.out.println("Adding website");
                        websiteList.add(url.getText());
                    }

                    Platform.runLater(() -> {
                        xenForoNotifier.showStatView();
                    });

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
               if (accounts.get(url.getText()) == null) { // no ArrayList exists for this forum, create one
                   System.out.println("Creating arraylist");
                   ArrayList<ForumAccount> addAccount = new ArrayList<>();
                   addAccount.add(new ForumAccount(url.getText(), webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), tempConnProtocol));

                   accounts.put(url.getText(), addAccount);
                   System.out.println("Account has been added to a new arraylist");
               } else { // there was already an ArrayList for that forum
                   System.out.println("Arraylist already existed");
                   ArrayList<ForumAccount> addAccounts = new ArrayList<>();
                   accounts.get(url.getText()).forEach(addAccounts::add);
                   addAccounts.add(new ForumAccount(url.getText(), webClient.getCookieManager().getCookies(), getAccountName(tempConnProtocol + "://" + url.getText()), tempConnProtocol));

                   accounts.replace(url.getText(), addAccounts);
               }

               if (!websiteList.contains(url.getText())) {
                   System.out.println("Adding website");
                   websiteList.add(url.getText());
               }

               Platform.runLater(() -> {
                   xenForoNotifier.showStatView();
               });

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
