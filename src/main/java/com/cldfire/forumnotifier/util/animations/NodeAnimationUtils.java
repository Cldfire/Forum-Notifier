package com.cldfire.forumnotifier.util.animations;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NodeAnimationUtils {

    public static void bindFromToAnimation(final EnumAnimationType animation, final Region bindObject, final Double duration, final Color fromColor, final Color toColor) {
        final ObjectProperty<Color> color = new SimpleObjectProperty<>(fromColor);
        final StringProperty colorStringProperty = createColorStringProperty(color);

        bindObject.styleProperty().bind(
                new SimpleStringProperty(animation.getStyleProperty() + ": transparent transparent ")
                        .concat(colorStringProperty)
                        .concat(" transparent;")
        );

        bindObject.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.seconds(0), new KeyValue(color, fromColor, Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(duration * 0.25), new KeyValue(color, toColor, Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(duration * 0.75), new KeyValue(color, toColor, Interpolator.LINEAR))
                );
                fade.play();
            } else {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.seconds(0), new KeyValue(color, toColor, Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(duration * 0.25), new KeyValue(color, fromColor, Interpolator.LINEAR)),
                        new KeyFrame(Duration.seconds(duration * 0.75), new KeyValue(color, fromColor, Interpolator.LINEAR))
                );
                fade.play();
            }
        });
    }

    private static StringProperty createColorStringProperty(final ObjectProperty<Color> color) {
        final StringProperty colorStringProperty = new SimpleStringProperty();
        setColorStringFromColor(colorStringProperty, color);

        color.addListener((ObservableValue<? extends Color> observableValue, Color oldColor, Color newColor) -> {
                setColorStringFromColor(colorStringProperty, color);
        });

        return colorStringProperty;
    }

    private static void setColorStringFromColor(StringProperty colorStringProperty, ObjectProperty<Color> color) {
        colorStringProperty.set(
                "rgba("
                        + ((int) (color.get().getRed() * 255)) + ","
                        + ((int) (color.get().getGreen() * 255)) + ","
                        + ((int) (color.get().getBlue() * 255)) + ","
                        + color.get().getOpacity() +
                        ")"
        );
    }

}
