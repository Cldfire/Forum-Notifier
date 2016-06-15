package com.cldfire.forumnotifier.model;

import com.cldfire.forumnotifier.util.EnumXpathType;
import com.cldfire.forumnotifier.util.animations.EnumColorFadeAnimationType;
import com.cldfire.forumnotifier.util.animations.NodeAnimationUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XpathDisplayBlock {
    private AnchorPane xpathPane;
    private Label xpathLabel;
    private TextField xpathTextField;
    private Rectangle divider;

    public XpathDisplayBlock(final EnumXpathType type, final AccountXpaths xpaths, final String text) {
        Pattern p = Pattern.compile("'([^']*)'");

        xpathPane = new AnchorPane();
        xpathPane.setPrefSize(300, 80);

        xpathLabel = new Label();
        xpathLabel.setTranslateX(20);
        xpathLabel.setTranslateY(10);
        xpathLabel.getStyleClass().add("error-label");
        xpathLabel.getStyleClass().set(1, "label");
        xpathLabel.setText(type.getLabelText());

        xpathTextField = new TextField();
        xpathTextField.setTranslateX(10);
        xpathTextField.setTranslateY(30);
        xpathTextField.setPrefWidth(200);
        xpathTextField.setText(text);
        xpathTextField.setPromptText(type.getPromptText());
        xpathTextField.getStyleClass().add("text-input-code");
        NodeAnimationUtils.bindColorFadeAnimation(EnumColorFadeAnimationType.COLOR_FADE, xpathTextField, 0.7, new Color(0.3098039215686275, 0.3098039215686275, 0.3098039215686275, 1), new Color(0, 1, 0.9254901960784314, 1));

        xpathPane.getChildren().addAll(
                xpathLabel,
                xpathTextField
        );

        xpathTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<String> newList = new ArrayList<>();
            newList.add(newValue);

            switch (type) {
                case CONFIRMTWOFACTORBUTTONNAME:
                case PASSWORDFIELDNAME:
                case STAYLOGGEDINFIELDNAME:
                case TRUSTTWOFACTORLOGINFIELDNAME:
                case TWOFACTORCODEFIELDNAME:
                case USERNAMEFIELDNAME:
                case LOGINBUTTONVALUE:
                    String result = "";
                    Matcher m = p.matcher(newValue);

                    while (m.find()) {
                        result = m.group(1);
                    }

                    if ((xpathTextField.getText().startsWith("name='") && result != "") ||
                            (xpathTextField.getText().startsWith("value='")) && result != "") {

                        xpathLabel.setText(type.getLabelText());
                        xpathLabel.getStyleClass().set(0, "label");

                        switch (type) {
                            case PASSWORDFIELDNAME:
                                xpaths.setPasswordFieldName(newList);
                                break;
                            case USERNAMEFIELDNAME:
                                xpaths.setUsernameFieldName(newList);
                                break;
                            case STAYLOGGEDINFIELDNAME:
                                xpaths.setStayLoggedInFieldName(newList);
                                break;
                            case LOGINBUTTONVALUE:
                                xpaths.setStayLoggedInFieldName(newList);
                                break;
                            case TWOFACTORCODEFIELDNAME:
                                xpaths.setTwoFactorCodeFieldName(newList);
                                break;
                            case TRUSTTWOFACTORLOGINFIELDNAME:
                                xpaths.setTrustTwoFactorLoginFieldName(newList);
                                break;
                            case CONFIRMTWOFACTORBUTTONNAME:
                                xpaths.setConfirmTwoFactorButtonName(newList);
                                break;
                        }
                    } else {
                        xpathLabel.setText("format: <type>='<value>'");
                        xpathLabel.getStyleClass().set(0, "error-label");
                    }
            }
                switch (type) {
                    case USERPASSLOGINFORM:
                        xpaths.setUserPassLoginForm(newList);
                        break;
                    case TWOFACTORLOGINFORM:
                        xpaths.setTwoFactorLoginForm(newList);
                        break;
                    case ACCOUNTURL:
                        xpaths.setAccountUrl(newList);
                        break;
                    case ACCOUNTPIC:
                        xpaths.setAccountPic(newList);
                        break;
                    case ACCOUNTNAME:
                        xpaths.setAccountName(newList);
                        break;
                    case MESSAGES:
                        xpaths.setMessages(newList);
                        break;
                    case ALERTS:
                        xpaths.setAlerts(newList);
                        break;
                    case POSTS:
                        xpaths.setPosts(newList);
                        break;
                    case RATINGS:
                        xpaths.setRatings(newList);
                        break;
                    case FOLLOWINGLIST:
                        xpaths.setFollowingList(newList);
                        break;
                    case FOLLOWERLIST:
                        xpaths.setFollowerList(newList);
                        break;
                    case FOLLOWERCOUNT:
                        xpaths.setFollowerCount(newList);
                        break;
                }
        });
    }

    /*
    Create a plain block that separates groups
     */

    public XpathDisplayBlock() {
        xpathPane = new AnchorPane();
        xpathPane.setPrefSize(300, 10);

        divider = new Rectangle(150, 0, Color.TRANSPARENT);
        divider.setStroke(Color.web("#303030"));
        divider.setStrokeLineCap(StrokeLineCap.ROUND);
        divider.setTranslateX(35);
        divider.setTranslateY(4);

        xpathPane.getChildren().add(divider);
    }

    public AnchorPane get() {
        return xpathPane;
    }


}
