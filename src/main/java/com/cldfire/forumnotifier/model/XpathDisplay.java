package com.cldfire.forumnotifier.model;

import javafx.scene.control.ListCell;

public class XpathDisplay extends ListCell<XpathDisplayBlock> {

    @Override
    protected void updateItem(XpathDisplayBlock xdb, boolean empty) {
        super.updateItem(xdb, empty);
        if (empty || xdb == null) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(xdb.get());
        }
    }
}
