package com.cldfire.xenforonotifier.util.animations;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkingAnimation {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public WorkingAnimation(Pane layer) {
        executor.submit(() -> {

                Platform.runLater(() -> {

                });

        });
    }

}
