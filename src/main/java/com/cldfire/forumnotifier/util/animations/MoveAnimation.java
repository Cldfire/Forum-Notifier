package com.cldfire.forumnotifier.util.animations;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class MoveAnimation {
    final Timeline slideX;
    final Timeline slideY;

    public MoveAnimation(Region node, Double duration, Double endX, Double endY) {
        slideX = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.layoutXProperty(), node.getLayoutX(), Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(duration), new KeyValue(node.layoutXProperty(), endX, Interpolator.EASE_BOTH))
        );

        slideY = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(node.layoutYProperty(), node.getLayoutY(), Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(duration * 0.25), new KeyValue(node.layoutYProperty(), endY, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(duration * 0.75), new KeyValue(node.layoutYProperty(), endY, Interpolator.LINEAR))
        );
    }

    public void play() {
        slideX.play();
        slideY.play();
    }

}
