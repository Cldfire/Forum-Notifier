package com.cldfire.forumnotifier.util.animations;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
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

public class ParticleAnimation {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ParticleAnimation(Pane layer) {
        executor.submit(() -> {
            Random randGen = new Random();

            for (int i = randGen.nextInt(30) * 10 + 20; i > 0; i--) {
                Circle test = createParticle(randGen.nextDouble(), Color.AQUAMARINE);
                Platform.runLater(() -> {
                    layer.getChildren().add(test);
                });
                animate(test, generateRandomPath(layer));
                try {
                    Thread.sleep(randGen.nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
       });
    }

    private Circle createParticle(Double radius, Color color) {
        Random randGen = new Random();

        Circle returnCircle = new Circle(radius, color);
        returnCircle.setOpacity(0.0);
        returnCircle.setTranslateX(randGen.nextInt(800));
        returnCircle.setTranslateY(randGen.nextInt(500));
        returnCircle.setEffect(new GaussianBlur(randGen.nextInt(10) + 1));

        returnCircle.setCache(true);
        returnCircle.setCacheHint(CacheHint.QUALITY);
        return returnCircle;
    }

    private void animate(Circle particle, Path path) {
        Random randGen = new Random();

        PathTransition pathTransition = new PathTransition(Duration.seconds(path.getElements().size() * (randGen.nextInt(30) + 30)), path, particle);
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

        int dimensionX = (int) layer.getPrefWidth(); // TODO: Have this not use prefWidth / Height
        int dimensionY = (int) layer.getPrefHeight();
        int pathPointNum = randGen.nextInt(50) + randGen.nextInt(20) + 5;
        Path particlePath = new Path();
        particlePath.getElements().add(new MoveTo(randGen.nextInt(dimensionX), randGen.nextInt(dimensionY)));

        for (int i = pathPointNum; i > 0; i--) {
            particlePath.getElements().add(new CubicCurveTo(randGen.nextInt(500), randGen.nextInt(500), randGen.nextInt(500), randGen.nextInt(500), randGen.nextInt(dimensionX), randGen.nextInt(dimensionY)));
        }
        return particlePath;
    }

    private Image createImage(Node node) {
        WritableImage wi;

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        int imageWidth = (int) node.getBoundsInLocal().getWidth();
        int imageHeight = (int) node.getBoundsInLocal().getHeight();

        wi = new WritableImage(imageWidth, imageHeight);
        node.snapshot(parameters, wi);

        return wi;
    }
}
