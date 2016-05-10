package com.cldfire.xenforonotifier.model;

import com.cldfire.xenforonotifier.util.EnumGoogleIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AccountDisplayBlock extends ListCell<Account> {

    private Pane blockPane;
    private Rectangle backing;
    private Rectangle divider;
    private Label accountName;
    private Label forumUrl;
    private Text alertCount;
    private Text messageCount;
    private TextFlow newNotifications;
    private ImageView accountPic;
    private boolean areListenersSet;

    public AccountDisplayBlock() {
        areListenersSet = false;

        blockPane = new AnchorPane();
        blockPane.setPrefSize(0, 100);

        backing = new Rectangle(350, 100, Color.TRANSPARENT);

        divider = new Rectangle(300, 0, Color.TRANSPARENT);
        divider.setStroke(Color.web("#303030"));
        divider.setStrokeLineCap(StrokeLineCap.ROUND);
        divider.setLayoutX(25);
        divider.setLayoutY(100.5);

        accountName = new Label("...");
        accountName.getStyleClass().add("label-header");
        accountName.setLayoutY(10);
        accountName.setLayoutX(85);

        forumUrl = new Label("...");
        forumUrl.getStyleClass().add("label-subtle");
        forumUrl.setLayoutY(35);
        forumUrl.setLayoutX(85);

        Text notificationIcon = new Text(EnumGoogleIcon.NOTIFICATIONS.get() + " ");
        alertCount = new Text("0");
        Text messageIcon = new Text("  " + EnumGoogleIcon.MESSAGE.get() + " ");
        messageCount = new Text("0");

        notificationIcon.setStyle("-fx-font-family: \'Material Icons\'; -fx-font-size: 16; -fx-fill: white;");
        alertCount.setStyle("-fx-font-family: \'Segoe UI\'; -fx-font-size: 16; -fx-fill: #00ffec;");
        messageIcon.setStyle("-fx-font-family: \'Material Icons\'; -fx-font-size: 16; -fx-fill: white;");
        messageCount.setStyle("-fx-font-family: \'Segoe UI\'; -fx-font-size: 16; -fx-fill: #00ffec;");

        TextFlow textFlow = new TextFlow(notificationIcon, alertCount, messageIcon, messageCount);
        textFlow.setLayoutY(65);
        textFlow.setLayoutX(85);

        accountPic = new ImageView();
        accountPic.setLayoutY(10);
        accountPic.setLayoutX(10);

        blockPane.getChildren().add(backing);
        blockPane.getChildren().add(divider);
        blockPane.getChildren().add(accountName);
        blockPane.getChildren().add(forumUrl);
        blockPane.getChildren().add(textFlow);
        blockPane.getChildren().add(accountPic);
    }

    @Override
    protected void updateItem(Account a, boolean empty) {
        super.updateItem(a, empty);
        if (empty || a == null) {
            setGraphic(null);
            setText(null);
        } else {
            accountName.setText(a.getName());
            forumUrl.setText(a.getForum().getUrl());
            accountPic.setImage(a.getAccountPic());
            setGraphic(blockPane);

            if (!areListenersSet) {
                a.getAlertProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {



                    }
                });

                a.getMessageProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    }
                });

                areListenersSet = true;
            }
        }
    }
}
