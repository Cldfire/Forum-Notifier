package com.cldfire.xenforonotifier.util.notifications;

/**
 * A class of image enums
 */
public enum EnumImage {
    ALERT_BELL("images/notification-bell.png");

    private final String path;

    /**
     * @param path path to the image
     */
    EnumImage(final String path) {
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
