package com.cldfire.forumnotifier.util;

/**
 * Created by Cldfire on 5/9/2016.
 */
public enum EnumGoogleIcon {

    NOTIFICATIONS("\uE7F4"), MESSAGE("\uE0C9"), ADD("\uE145"), ADD_BOX("\uE146"), BACKSPACE("\uE14A");

    private final String unicode;

    EnumGoogleIcon(final String unicode) {
        this.unicode = unicode;
    }

    public String get() {
        return unicode;
    }

}
