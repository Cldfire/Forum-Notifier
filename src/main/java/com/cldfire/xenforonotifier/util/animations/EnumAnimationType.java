package com.cldfire.xenforonotifier.util.animations;

/**
 * Created by Cldfire on 5/2/2016.
 */
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
