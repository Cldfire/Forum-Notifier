package com.cldfire.xenforonotifier.util.notifications;

/**
 * A class of image enums
 */
public enum EnumImageType {
    ALERT("images/notification-bell.png");

    private final String path;

    /**
     * @param path path to the image
     */
    EnumImageType(final String path) {
        this.path = path;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return path;
    }
}
