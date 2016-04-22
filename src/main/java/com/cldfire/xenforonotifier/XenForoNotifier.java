package com.cldfire.xenforonotifier;

import com.cldfire.xenforonotifier.util.LangUtils;
import com.cldfire.xenforonotifier.util.NotificationUtils;
import com.cldfire.xenforonotifier.util.Settings;
import com.cldfire.xenforonotifier.util.LangUtils.Locale;

import com.cldfire.xenforonotifier.view.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;

public class XenForoNotifier extends Application { // Project started April 1st, 2016

    private Stage primaryStage;
    private BorderPane rootLayout;
    public static File APP_DIR;

    public static void main(String[] args) {
        launch(args);
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("views/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showStatView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("views/StatView.fxml"));
            AnchorPane statView = loader.load();

            rootLayout.setCenter(statView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoginView() {
        try {
            // Load login view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("views/LoginView.fxml"));
            AnchorPane loginView = loader.load();

            // Set the stat view into the center of root layout.
            rootLayout.setCenter(loginView);

            LoginViewController controller = loader.getController();
            controller.setXenForoNotifier(this);
            controller.username.requestFocus();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        APP_DIR = new File(System.getProperty("user.home"), ".spigotnotifier");
        if (!APP_DIR.exists()) {
            APP_DIR.mkdir();
        }
        LangUtils.loadLocale(Locale.EN_US);
        Settings.load();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(LangUtils.translate("window.title"));

        initRootLayout();
        showLoginView();
    }
}
