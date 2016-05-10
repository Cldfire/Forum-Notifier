/*
The MIT License (MIT)

Copyright (c) 2016 Jarek Samic

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.cldfire.forumnotifier;

import com.cldfire.forumnotifier.util.ForumsStore;
import com.cldfire.forumnotifier.util.LangUtils;
import com.cldfire.forumnotifier.util.LangUtils.Locale;
import com.cldfire.forumnotifier.util.Settings;
import com.cldfire.forumnotifier.view.LoginViewController;
import com.cldfire.forumnotifier.view.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ForumNotifier extends Application { // Project started April 1st, 2016

    public static File APP_DIR;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private AnchorPane statView;
    private AnchorPane loginView;

    private LoginViewController loginViewController;

    public static void main(String[] args) {
        launch(args);
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("views/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Material+Icons");
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setForumNotifier(this);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStatView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("views/StatView.fxml"));
            statView = loader.load();
            BorderPane.setMargin(statView, new Insets(0, 0, 450, 0));
            //new ParticleAnimation(statView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("views/LoginView.fxml"));
            loginView = loader.load();
            BorderPane.setMargin(loginView, new Insets(0, 0, 450, 0));

            LoginViewController controller = loader.getController();
            controller.setForumNotifier(this);
            this.loginViewController = controller;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStatView() {
        rootLayout.setCenter(statView);
    }

    public void showLoginView() {
        rootLayout.setCenter(loginView);
        loginViewController.resetForNewLogin();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        APP_DIR = new File(System.getProperty("user.home"), ".forumnotifier");
        if (!APP_DIR.exists()) { // Technically first install
            boolean result = APP_DIR.mkdir();
            if (!result) {
                throw new IOException("Unable to create file: " + APP_DIR.getAbsolutePath()); // Supposedly stops the program
            }
        }

        Settings.load();
        Settings.versionCheck();
        LangUtils.loadLocale(Locale.valueOf(Settings.get("client.lang")));
        ForumsStore.loadForums();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(LangUtils.translate("window.title"));

        initRootLayout();
        loadStatView();
        loadLoginView();
        showStatView();
    }
}
