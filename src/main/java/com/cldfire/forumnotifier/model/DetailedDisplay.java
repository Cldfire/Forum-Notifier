package com.cldfire.forumnotifier.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class DetailedDisplay {

    private Pane viewPane;

    public DetailedDisplay() {
        viewPane = new AnchorPane();
        viewPane.setPrefSize(400, 500);
    }

    public Pane get() {
        return viewPane;
    }

}
