package com.cldfire.forumnotifier.util;

public enum EnumGoogleIcon {
    NOTIFICATIONS("\uE7F4"),
    MESSAGE("\uE554"),
    ADD("\uE145"),
    ADD_BOX("\uE146"),
    BACKSPACE("\uE14A"),
    POST("\uE0C9"),
    STAR_BORDER("\uE83A"),
    WARNING("\uE002"),
    EDIT("\uE3C9"),
    SETTINGS("\uE8B8");

    private final String unicode;

    EnumGoogleIcon(final String unicode) {
        this.unicode = unicode;
    }

    public String get() {
        return unicode;
    }

}
