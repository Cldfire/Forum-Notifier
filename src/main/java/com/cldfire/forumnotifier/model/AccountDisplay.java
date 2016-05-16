package com.cldfire.forumnotifier.model;

import javafx.scene.control.ListCell;

public class AccountDisplay extends ListCell<Account> {

    @Override
    protected void updateItem(Account a, boolean empty) {
        super.updateItem(a, empty);
        if (empty || a == null) {
            setGraphic(null);
            setText(null);
        } else {
            AccountDisplayBlock displayBlock = a.getDisplayBlock();

            displayBlock.setAccountPic(a.getAccountPic());
            displayBlock.setAccountName(a.getName());
            displayBlock.setForumUrl(a.getForum().getUrl());
            try {
                displayBlock.setAlertCount(a.getAlertCount().toString());
                displayBlock.setMessageCount(a.getMessageCount().toString());
                displayBlock.setPostCount(a.getPostCount());
                displayBlock.setPositiveRatingCount(a.getPositiveRatingCount());
            } catch (NullPointerException e) {
                displayBlock.setAlertCount("~");
                displayBlock.setMessageCount("~");
                displayBlock.setPostCount("~");
                displayBlock.setPositiveRatingCount("~");
            }

            setGraphic(displayBlock.get());
        }
    }
}
