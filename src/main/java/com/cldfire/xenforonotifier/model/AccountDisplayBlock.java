package com.cldfire.xenforonotifier.model;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Cldfire on 4/25/2016.
 */
public class AccountDisplayBlock extends ListCell<Account> {

    private Pane blockPane;
    private Rectangle backing;
    private Label header;
    private Label alertCount;
    private Label messageCount;
    private ImageView favicon;

    public AccountDisplayBlock() {
        blockPane = new AnchorPane();
        blockPane.setPrefSize(600, 100);
        backing = new Rectangle(600, 100, Color.ALICEBLUE);
        backing.setOpacity(0.7);

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

        blockPane.getChildren().add(backing);
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
            header.setText(a.getName() + "@" + a.getForum().getUrl());
            alertCount.setText("Alerts: " + a.getAlertCount().toString());
            messageCount.setText("Messages " + a.getMessageCount().toString());
            //favicon.setImage(a.getFavicon());
            setGraphic(blockPane);
        }
    }

}
