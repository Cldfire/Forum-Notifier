package com.cldfire.forumnotifier.view;

import com.cldfire.forumnotifier.ForumNotifier;
import com.cldfire.forumnotifier.util.EnumGoogleIcon;
import com.cldfire.forumnotifier.util.LangUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class RootLayoutController {
    @FXML
    TextFlow addAccount;

    private ForumNotifier forumNotifier;
    private static LoginViewController loginViewController;

    private Text addIcon;
    private Text addText;
    private boolean loggingIn = false;

    @FXML
    private void initialize() {
        LoginViewController.setRootLayoutController(this);
        addIcon = new Text(EnumGoogleIcon.ADD_BOX.get());
        addIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: #7c7c7c;");
        addText = new Text(LangUtils.translate("statview.addaccount.add"));
        addText.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 16; -fx-fill: #7c7c7c;");
        addText.setTranslateY(-6);
        addText.setTranslateX(1);
        addAccount.getChildren().addAll(addIcon, addText);
    }

    public void setForumNotifier(ForumNotifier forumNotifier) {
        this.forumNotifier = forumNotifier;
    }

    public static void setLoginViewController(LoginViewController loginViewController1) {
        loginViewController = loginViewController1;
    }

    @FXML
    private void handleAddAccount() {
        if (!loggingIn) {
            forumNotifier.showLoginView();
            addIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: #ff5757;");
            addIcon.setText(EnumGoogleIcon.BACKSPACE.get());
            addText.setText(LangUtils.translate("statview.addaccount.cancel"));
            addIcon.setTranslateX(1);
            addText.setTranslateX(3.5);
            loggingIn = true;
        } else {
            forumNotifier.showStatView();
            addIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: white;");
            addIcon.setText(EnumGoogleIcon.ADD_BOX.get());
            addText.setText(LangUtils.translate("statview.addaccount.add"));
            addIcon.setTranslateX(0);
            addText.setTranslateX(1);
            loginViewController.resetForNewLogin();
            loggingIn = false;
        }
    }

    @FXML
    private void handleMouseEnterAdd() {
        if (loggingIn) {
            addIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: #ff5757;");
            addText.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 16; -fx-fill: white;");
        } else {
            addIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: white;");
            addText.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 16; -fx-fill: white;");
        }

    }

    @FXML
    private void handleMouseExitAdd() {
        addIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: #7c7c7c;");
        addText.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 16; -fx-fill: #7c7c7c;");
    }

    public void changeButtonToAdd() {
        addIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: white;");
        addIcon.setText(EnumGoogleIcon.ADD_BOX.get());
        addText.setText(LangUtils.translate("statview.addaccount.add"));
        addIcon.setTranslateX(0);
        addText.setTranslateX(1);
        loggingIn = false;
    }
}
