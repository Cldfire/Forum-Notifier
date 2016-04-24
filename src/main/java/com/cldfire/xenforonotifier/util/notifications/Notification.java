package com.cldfire.xenforonotifier.util.notifications;

/**
 * Represents a notification
 */
public class Notification {
    /**
     * Title for this notification
     */
    private final String title;

    /**
     * Subtitle for this notification
     */
    private final String subtitle;

    /**
     * Type of image associated with this notification
     */
    private final EnumImageType imageType;

    /**
     * Delay (in seconds) this notification will be shown for
     */
    private final int delay;

    /**
     * Creates a new notification
     *
     * @param title this notification's title
     * @param type  this notification's image type
     */
    public Notification(String title, EnumImageType type) {
        this(title, null, type);
    }

    /**
     * Creates a new notification
     *
     * @param title    this notification's title
     * @param subtitle this notification's subtitle
     * @param type     this notification's image type
     */
    public Notification(String title, String subtitle, EnumImageType type) {
        this(title, subtitle, type, 4); // 4 second default delay time if unspecified
    }

    /**
     * Creates a new notification
     *
     * @param title    this notification's title
     * @param subtitle this notification's subtitle
     * @param type     this notification's image type
     * @param delay    this notification's delay
     */
    public Notification(String title, String subtitle, EnumImageType type, int delay) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageType = type;
        this.delay = delay;
    }

    /**
     * Creates a new notification
     *
     * @param builder NotificationBuilder to create notification from
     */
    public Notification(NotificationBuilder builder) {
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.imageType = builder.imageType;
        this.delay = builder.delay;
    }

    /**
     * Sends the specified notification to the user
     */
    public static void send(Notification notification) {
        NotificationUtils.sendNotification(notification);
    }

    /**
     * Sends this notification to the user
     */
    public void send() {
        NotificationUtils.sendNotification(this);
    }

    /**
     * Gets the main title for this notification
     *
     * @return the title of this notification
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets the subtitle for this notification
     *
     * @return the subtitle for this notification
     */
    public String getSubtitle() {
        return this.subtitle;
    }

    /**
     * Gets the image type associated with this notification
     *
     * @return the type of image for this notification
     */
    public EnumImageType getImageType() {
        return this.imageType;
    }

    /**
     * Gets the delay associated with this notification
     * IE how long will this notification be shown on the user's screen
     *
     * @return the delay for this notification
     */
    public int getDelay() {
        return this.delay;
    }

    public static class NotificationBuilder {
        private String title;
        private String subtitle;
        private EnumImageType imageType;
        private int delay;

        public NotificationBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NotificationBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public NotificationBuilder imageType(EnumImageType type) {
            this.imageType = type;
            return this;
        }

        public NotificationBuilder delay(int delay) {
            this.delay = delay;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
