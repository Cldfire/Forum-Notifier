package com.cldfire.forumnotifier.view;

import com.cldfire.forumnotifier.ForumNotifier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RootLayoutController {
    @FXML
    Button addAccount;

    private ForumNotifier forumNotifier;

    public void setForumNotifier(ForumNotifier forumNotifier) {
        this.forumNotifier = forumNotifier;
    }

    @FXML
    private void handleAddAccount() {
        if (addAccount.getText().equalsIgnoreCase("add")) {
            forumNotifier.showLoginView();
            addAccount.setText("Cancel");
        } else {
            forumNotifier.showStatView();
            addAccount.setText("Add");
        }

    }

}
