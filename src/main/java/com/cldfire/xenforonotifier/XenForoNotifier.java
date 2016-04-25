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

package com.cldfire.xenforonotifier;

import com.cldfire.xenforonotifier.util.ForumsStore;
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

    public static File APP_DIR;
    private Stage primaryStage;
    private BorderPane rootLayout;

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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getClassLoader().getResource("views/LoginView.fxml"));
            AnchorPane loginView = loader.load();

            // Set the view into the center of root layout
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
        if (!APP_DIR.exists()) { // Technically first install
            APP_DIR.mkdir();
        }

        Settings.load();
        LangUtils.loadLocale(Locale.valueOf(Settings.get("client.lang")));
        System.out.println(Settings.version);
        System.out.println(Settings.innerVersion);
        System.out.println(Settings.innerVersion > Settings.version); // If true then add new Settings to their Settings.

        ForumsStore.loadForums();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(LangUtils.translate("window.title"));

        initRootLayout();
        showLoginView();
    }
}
