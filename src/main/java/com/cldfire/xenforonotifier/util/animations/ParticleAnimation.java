package com.cldfire.xenforonotifier.util.animations;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Cldfire on 5/2/2016.
 */
public class ParticleAnimation {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ParticleAnimation(Pane layer) {
        //executor.submit(() -> {
            Random randGen = new Random();

            for (int i = randGen.nextInt(30) * 10 + 20; i > 0; i--) {
                Circle test = createParticle(1.0, Color.AQUAMARINE, layer);
               // Platform.runLater(() -> {
                    layer.getChildren().add(test);
               // });
                animate(test, generateRandomPath(layer));
                try {
                    Thread.sleep(randGen.nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
       // });
    }

    private Circle createParticle(Double radius, Color color, Pane layer) {
        Random randGen = new Random();

        Circle returnCircle = new Circle(radius, color);
        returnCircle.setOpacity(0.0);
        returnCircle.setTranslateX(randGen.nextInt(800));
        returnCircle.setTranslateY(randGen.nextInt(500));
        return returnCircle;
    }

    private void animate(Circle particle, Path path) {
        Random randGen = new Random();

        PathTransition pathTransition = new PathTransition(Duration.seconds(path.getElements().size() * (randGen.nextInt(10) + 9)), path, particle);
        pathTransition.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3f), particle);
        scaleTransition.setToX(10f);
        scaleTransition.setToY(10f);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(6f), particle);
        fadeTransition.setToValue(0.7);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);

        pathTransition.play();
        scaleTransition.play();
        fadeTransition.play();
    }

    private Path generateRandomPath(Pane layer) {
        Random randGen = new Random();

        int dimensionX = (int) layer.getPrefWidth();
        int dimensionY = (int) layer.getPrefHeight();
        int pathPointNum = randGen.nextInt(50) + randGen.nextInt(20) + 5;
        Path particlePath = new Path();
        particlePath.getElements().add(new MoveTo(-20, -20));

        for (int i = pathPointNum; i > 0; i--) {
            particlePath.getElements().add(new CubicCurveTo(randGen.nextInt(500), randGen.nextInt(500), randGen.nextInt(500), randGen.nextInt(500), randGen.nextInt(dimensionX), randGen.nextInt(dimensionY)));
        }
        return particlePath;
    }

}
