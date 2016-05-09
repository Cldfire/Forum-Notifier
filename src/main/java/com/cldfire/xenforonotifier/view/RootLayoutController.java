package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.XenForoNotifier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RootLayoutController {
    @FXML
    Button addAccount;

    private XenForoNotifier xenForoNotifier;

    public void setXenForoNotifier(XenForoNotifier xenForoNotifier) {
        this.xenForoNotifier = xenForoNotifier;
    }

    @FXML
    private void handleAddAccount() {
        if (addAccount.getText().equalsIgnoreCase("add")) {
            xenForoNotifier.showLoginView();
            addAccount.setText("Cancel");
        } else {
            xenForoNotifier.showStatView();
            addAccount.setText("Add");
        }

    }

}
