package com.cldfire.xenforonotifier.util.animations;

import javafx.animation.PathTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by Cldfire on 5/2/2016.
 */
public class ParticleAnimation {

    public ParticleAnimation(Pane layer) {
        Circle test = createParticle(20.0, Color.AQUAMARINE);
        layer.getChildren().add(test);
        animate(test, generateRandomPath(layer));
    }

    private Circle createParticle(Double radius, Color color) {
        return new Circle(radius, color);
    }

    private void animate(Circle particle, Path path) {
        PathTransition pathTransition = new PathTransition(Duration.minutes(1.0), path, particle);
        pathTransition.play();
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
