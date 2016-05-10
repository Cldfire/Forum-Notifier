package com.cldfire.forumnotifier.util.animations;

public enum EnumAnimationType {

    COLOR_FADE("-fx-border-color");

    private final String styleProperty;

    EnumAnimationType(final String styleProperty) {
        this.styleProperty = styleProperty;
    }

    public String getStyleProperty() {
        return styleProperty;
    }
}
