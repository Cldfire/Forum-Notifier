package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.util.LangUtils;
import com.cldfire.xenforonotifier.util.Settings;
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

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewController {
    // TODO: Re-write this to support multiple logins for checking multiple accounts / sites

    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    @FXML
    public Button loginButton;
    @FXML
    public TextField authCode;
    @FXML
    public Button confirmButton;
    @FXML
    public Label errorLabel;

    private XenForoNotifier xenForoNotifier;
    private static Set<Cookie> cookies;

    private final WebClient webClient = new WebClient(BrowserVersion.CHROME);
    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    public static Set<Cookie> getCookies() {
        return cookies;
    }

    public void setXenForoNotifier(XenForoNotifier xenForoNotifier) {
        this.xenForoNotifier = xenForoNotifier;
    }

    private Boolean testForLoggedIn() {
        return webClient.getCookieManager().getCookie("xf_user") != null;
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

    @FXML
    private void handleLogin() {
        Runnable loginRunnable = () -> { // TODO: Finalize error handling here, clean up where necessary
            Platform.runLater(() -> errorLabel.setVisible(false));
            final HtmlPage postLoginPage = loginToSite("https://" + Settings.get("website.baseurl") + "/login", username.getText(), password.getText());

            if (postLoginPage != null) {
                if (postLoginPage.getUrl().toString().startsWith("https://" + Settings.get("website.baseurl") + "/login/two-step")) {
                    password.setVisible(false);
                    username.setVisible(false);
                    loginButton.setVisible(false);
                    errorLabel.setVisible(false);
                    confirmButton.setVisible(true);
                    authCode.setVisible(true);

                } else if (testForLoggedIn()) {
                    cookies = webClient.getCookieManager().getCookies();
                    Platform.runLater(() -> xenForoNotifier.showStatView());

                } else if (postLoginPage.getUrl().toString().equals("https://" + Settings.get("website.baseurl") + "/login/login")) {
                    Platform.runLater(() -> {
                        errorLabel.setVisible(true);
                        password.setText("");
                    });
                } else {
                    errorLabel.setText(LangUtils.translate("login.errorLabel.other"));
                    errorLabel.setVisible(true);
                }
            }
        };
        executor.submit(loginRunnable);
    }

    @FXML
    private void handleTwoFactorAuthLogin() {
        Runnable twoFactorRunnable = () -> { // TODO: Finalize error handling here
            Platform.runLater(() -> errorLabel.setVisible(false));
            final HtmlPage postLoginPage = loginTwoFactorAuth("https://" + Settings.get("website.baseurl") + "/login/two-step?redirect=https%3A%2F%2Fwww.spigotmc.org%2F&remember=1", authCode.getText());

            if (postLoginPage != null) {
                if (testForLoggedIn()) {
                    cookies = webClient.getCookieManager().getCookies();
                    Platform.runLater(() -> xenForoNotifier.showStatView());
                } else {
                    Platform.runLater(() -> {
                        errorLabel.setText(LangUtils.translate("login.errorLabel.authCode"));
                        errorLabel.setVisible(true);
                        authCode.setText("");
                    });
                }
            } else {
                Platform.runLater(() -> {
                    errorLabel.setText(LangUtils.translate("login.errorLabel.other"));
                    errorLabel.setVisible(true);
                });
            }
        };
        executor.submit(twoFactorRunnable);
    }
}
