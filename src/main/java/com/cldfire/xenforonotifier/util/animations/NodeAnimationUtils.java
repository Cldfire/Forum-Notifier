package com.cldfire.xenforonotifier.util.animations;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NodeAnimationUtils {

    public static void bindFromToAnimation(final EnumAnimationType animation, final Region bindObject, final Integer duration) {
        final ObjectProperty<Color> warningColor = new SimpleObjectProperty<>(Color.GRAY);
        final StringProperty colorStringProperty = createWarningColorStringProperty(warningColor);

        bindObject.styleProperty().bind(
                new SimpleStringProperty(animation.getStyleProperty() + ": transparent transparent ")
                        .concat(colorStringProperty)
                        .concat(" transparent;")
        );

        bindObject.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue)
                {
                    Timeline fade = new Timeline(
                            new KeyFrame(Duration.seconds(0),    new KeyValue(warningColor, Color.GRAY, Interpolator.LINEAR)),
                            new KeyFrame(Duration.seconds(0.1),  new KeyValue(warningColor, Color.GRAY, Interpolator.LINEAR)),
                            new KeyFrame(Duration.seconds(0.35), new KeyValue(warningColor, Color.AQUAMARINE,  Interpolator.LINEAR)),
                            new KeyFrame(Duration.seconds(0.75), new KeyValue(warningColor, Color.AQUAMARINE,  Interpolator.LINEAR))
                    );
                    fade.play();
                }
                else
                {
                    Timeline fade = new Timeline(
                            new KeyFrame(Duration.seconds(0),    new KeyValue(warningColor, Color.AQUAMARINE, Interpolator.LINEAR)),
                            new KeyFrame(Duration.seconds(0.1),  new KeyValue(warningColor, Color.AQUAMARINE, Interpolator.LINEAR)),
                            new KeyFrame(Duration.seconds(0.35), new KeyValue(warningColor, Color.GRAY,  Interpolator.LINEAR)),
                            new KeyFrame(Duration.seconds(0.75), new KeyValue(warningColor, Color.GRAY,  Interpolator.LINEAR))
                    );
                    fade.play();
                }
            }
        });
    }

    private static StringProperty createWarningColorStringProperty(final ObjectProperty<Color> warningColor) {
        final StringProperty colorStringProperty = new SimpleStringProperty();
        setColorStringFromColor(colorStringProperty, warningColor);
        warningColor.addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color oldColor, Color newColor) {
                setColorStringFromColor(colorStringProperty, warningColor);
            }
        });

        return colorStringProperty;
    }

    private static void setColorStringFromColor(StringProperty colorStringProperty, ObjectProperty<Color> color) {
        colorStringProperty.set(
                "rgba("
                        + ((int) (color.get().getRed()   * 255)) + ","
                        + ((int) (color.get().getGreen() * 255)) + ","
                        + ((int) (color.get().getBlue()  * 255)) + ","
                        + color.get().getOpacity() +
                        ")"
        );
    }

}
