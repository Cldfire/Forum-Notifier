package com.cldfire.xenforonotifier.util.notifications;

import javafx.animation.Animation;

/**
 * Represents a notification
 */
public class Notification {
    private final String title;
    private final String subtitle;
    private final EnumImageType imageType;
    private final Animation animation;

    /**
     * Creates a new notification
     *
     * @param title this notification's title
     */
    public Notification(String title) {
        this(title, null, null, null);
    }

    /**
     * Creates a new notification
     *
     * @param title    this notification's title
     * @param subtitle this notification's subtitle
     */
    public Notification(String title, String subtitle) {
        this(title, subtitle, null, null);
    }

    /**
     * Creates a new notification
     *
     * @param title this notification's title
     * @param type  this notification's image type
     */
    public Notification(String title, EnumImageType type) {
        this(title, null, type, null);
    }

    /**
     * Creates a new notification
     *
     * @param title    this notification's title
     * @param subtitle this notification's subtitle
     * @param type     this notification's image type
     */
    public Notification(String title, String subtitle, EnumImageType type) {
        this(title, subtitle, type, null);
    }

    /**
     * Creates a new notification
     *
     * @param title     this notification's title
     * @param subtitle  this notification's subtitle
     * @param type      this notification's image type
     * @param animation this notification's animation
     */
    public Notification(String title, String subtitle, EnumImageType type, Animation animation) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageType = type;
        this.animation = animation;
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
        this.animation = builder.animation;
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
     * Gets the animation associated with this notification
     *
     * @return the animation for this notification
     */
    public Animation getAnimation() { // TODO: How are we (are we?) standardizing animations?
        return this.animation;
    }

    public static class NotificationBuilder {
        private String title;
        private String subtitle;
        private EnumImageType imageType;
        private Animation animation;

        public NotificationBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NotificationBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public NotificationBuilder image(EnumImageType type) {
            this.imageType = type;
            return this;
        }

        public NotificationBuilder animation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
