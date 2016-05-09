package com.cldfire.xenforonotifier.model;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AccountDisplayBlock extends ListCell<Account> {

    private Pane blockPane;
    private Label header;
    private Label alertCount;
    private Label messageCount;
    private ImageView favicon;

    public AccountDisplayBlock() {
        blockPane = new AnchorPane();
        blockPane.setPrefSize(760, 100);

        header = new Label("...");
        header.setLayoutY(10);
        header.setLayoutX(100);

        alertCount = new Label("...");
        alertCount.setLayoutY(50);
        alertCount.setLayoutX(450);

        messageCount = new Label("...");
        messageCount.setLayoutY(35);
        messageCount.setLayoutX(450);

        favicon = new ImageView();
        favicon.setLayoutY(10);
        favicon.setLayoutX(10);

        blockPane.getChildren().add(header);
        blockPane.getChildren().add(alertCount);
        blockPane.getChildren().add(messageCount);
        blockPane.getChildren().add(favicon);
    }

    @Override
    protected void updateItem(Account a, boolean empty) {
        super.updateItem(a, empty);
        if (empty || a == null) {
            setGraphic(null);
            setText(null);
        } else {
            header.setText(a.getName() + " @ " + a.getForum().getUrl());
            alertCount.textProperty().bind(a.getAlertProperty());
            messageCount.textProperty().bind(a.getMessageProperty());
            //favicon.setImage(a.getFavicon());
            setGraphic(blockPane);
        }
    }

}
