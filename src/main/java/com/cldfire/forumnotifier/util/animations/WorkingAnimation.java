package com.cldfire.forumnotifier.util.animations;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkingAnimation {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public WorkingAnimation(Pane layer) { // yes I know I've done nothing with this yet, too lazy to remove the file. Will use later
        executor.submit(() -> {

                Platform.runLater(() -> {

                });

        });
    }

}
