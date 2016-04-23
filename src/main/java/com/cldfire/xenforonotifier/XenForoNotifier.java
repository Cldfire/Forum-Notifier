package com.cldfire.xenforonotifier;

import com.cldfire.xenforonotifier.util.LangUtils;
import com.cldfire.xenforonotifier.util.LangUtils.Locale;
import com.cldfire.xenforonotifier.util.Settings;
import com.cldfire.xenforonotifier.view.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        APP_DIR = new File(System.getProperty("user.home"), ".xenforonotifier");
        if (!APP_DIR.exists()) { // Technically First Install
            APP_DIR.mkdir();
        }

        Settings.load();
        LangUtils.loadLocale(Locale.valueOf(Settings.get("client.lang")));
        System.out.println(Settings.version);
        System.out.println(Settings.innerVersion);
        System.out.println(Settings.innerVersion > Settings.version); // If true then add new Settings to their Settings.


        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(LangUtils.translate("window.title"));

        initRootLayout();
        showLoginView();
    }
}
