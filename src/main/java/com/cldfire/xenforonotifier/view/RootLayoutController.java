package com.cldfire.xenforonotifier.view;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.util.LangUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RootLayoutController {
    @FXML
    Button addAccount;

    private XenForoNotifier xenForoNotifier;

    public void setXenForoNotifier(XenForoNotifier xenForoNotifier) {
        this.xenForoNotifier = xenForoNotifier;
    }

    @FXML
    private void handleAddAccount() {
        xenForoNotifier.showLoginView();
    }

}
