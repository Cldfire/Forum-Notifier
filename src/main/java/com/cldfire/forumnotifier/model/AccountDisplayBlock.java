package com.cldfire.forumnotifier.model;

import com.cldfire.forumnotifier.util.EnumGoogleIcon;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AccountDisplayBlock {

    private Pane blockPane;
    private Pane backing;
    private Rectangle divider;
    private Label accountName;
    private Label forumUrl;
    private Text alertCount;
    private Text messageCount;
    private Text postCount;
    private Text positiveRatingCount;
    private TextFlow accountStatus;
    private ImageView accountPic;

    public AccountDisplayBlock() {

        blockPane = new AnchorPane();
        blockPane.setPrefSize(0, 100);

        backing = new AnchorPane();
        backing.setPrefSize(350, 80);
        backing.setTranslateY(10);
        backing.getStyleClass().add("display-block");

        divider = new Rectangle(300, 0, Color.TRANSPARENT);
        divider.setStroke(Color.web("#303030"));
        divider.setStrokeLineCap(StrokeLineCap.ROUND);
        divider.setTranslateX(25);
        divider.setTranslateY(100.5);


        accountName = new Label("...");
        accountName.getStyleClass().add("label-header");
        accountName.setTranslateY(10);
        accountName.setTranslateX(90);

        forumUrl = new Label("...");
        forumUrl.getStyleClass().add("label-subtle");
        forumUrl.setTranslateY(35);
        forumUrl.setTranslateX(90);

        Text notificationIcon = new Text(EnumGoogleIcon.NOTIFICATIONS.get());
        Text messageIcon = new Text(" " + EnumGoogleIcon.MESSAGE.get());
        
        postCount = new Text("      " + ".");
        postCount.setTranslateY(-4.5);

        Text slash = new Text(" / ");
        slash.setTranslateY(-4.5);

        positiveRatingCount = new Text(".");
        positiveRatingCount.setTranslateY(-4.5);

        notificationIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: white;");
        messageIcon.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 24; -fx-fill: white;");
        messageIcon.setTranslateX(5);

        postCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: white;");
        slash.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #7c7c7c;");
        positiveRatingCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: lightgreen;");

        alertCount = new Text(".");
        alertCount.setTranslateY(-4.5);
        alertCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #7c7c7c;");

        messageCount = new Text(".");
        messageCount.setTranslateX(7.5);
        messageCount.setTranslateY(-4.5);
        messageCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #7c7c7c;");

        accountStatus = new TextFlow(notificationIcon, alertCount, messageIcon, messageCount, postCount, slash, positiveRatingCount);
        accountStatus.setTranslateY(65);
        accountStatus.setTranslateX(90);

        accountPic = new ImageView();
        accountPic.setTranslateY(10);
        accountPic.setFitHeight(80);
        accountPic.setFitWidth(80);
        accountPic.setPreserveRatio(true);

        blockPane.getChildren().add(backing);
        blockPane.getChildren().add(divider);
        blockPane.getChildren().add(accountName);
        blockPane.getChildren().add(forumUrl);
        blockPane.getChildren().add(accountStatus);
        blockPane.getChildren().add(accountPic);
    }

    public Pane get() {
        return blockPane;
    }

    public void setAlertCount(String alertCount) {
        try {
            if (Integer.parseInt(alertCount) > 0) {
                this.alertCount.setText(alertCount);
                this.alertCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #00ffec;");
            } else {
                this.alertCount.setText(alertCount);
                this.alertCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #7c7c7c;");
            }
        } catch (NumberFormatException e) {
            this.alertCount.setText(alertCount);
            this.alertCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #ff5757;");
        }
    }

    public void setAccountName(String accountName) {
        this.accountName.setText(accountName);
    }

    public void setAccountPic(Image accountPic) {
        this.accountPic.setImage(accountPic);
    }

    public void setForumUrl(String forumUrl) {
        this.forumUrl.setText(forumUrl);
    }

    public void setMessageCount(String messageCount) {
        try {
            if (Integer.parseInt(messageCount) > 0) {
                this.messageCount.setText(messageCount);
                this.messageCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #00ffec;");
            } else {
                this.messageCount.setText(messageCount);
                this.messageCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #7c7c7c;");
            }
        } catch (NumberFormatException e) {
            this.messageCount.setText(messageCount);
            this.messageCount.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18; -fx-fill: #ff5757;");
        }
    }

    public void setPositiveRatingCount(String positiveRatingCount) {
        this.positiveRatingCount.setText(positiveRatingCount);
    }

    public void setPostCount(String postCount) {
        this.postCount.setText("      " + postCount);
    }
}
