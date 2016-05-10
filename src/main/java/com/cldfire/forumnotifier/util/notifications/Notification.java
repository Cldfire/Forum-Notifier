package com.cldfire.forumnotifier.util.notifications;

import java.awt.image.BufferedImage;

/**
 * Represents a notification
 */
public class Notification implements Cloneable {
    /**
     * Title for this notification
     */
    private final String title;

    /**
     * Subtitle for this notification
     */
    private final String subtitle;

    /**
     * Image associated with this notification
     */
    private BufferedImage image;

    /**
     * Delay (in seconds) this notification will be shown for
     */
    private final int delay;

    /**
     * Creates a new notification
     *
     * @param title this notification's title
     * @param image this notification's image
     */
    public Notification(String title, BufferedImage image) {
        this(title, null, image);
    }

    /**
     * Creates a new notification
     *
     * @param title    this notification's title
     * @param subtitle this notification's subtitle
     * @param image    this notification's image
     */
    public Notification(String title, String subtitle, BufferedImage image) {
        this(title, subtitle, image, 4); // 4 second default delay time if unspecified
    }

    /**
     * Creates a new notification
     *
     * @param title    this notification's title
     * @param subtitle this notification's subtitle
     * @param image    this notification's image
     * @param delay    this notification's delay
     */
    public Notification(String title, String subtitle, BufferedImage image, int delay) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
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
        this.image = builder.image;
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
     * Gets the image associated with this notification
     *
     * @return the image for this notification
     */
    public BufferedImage getImage() {
        return this.image;
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
        private BufferedImage image;
        private int delay;

        public NotificationBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NotificationBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public NotificationBuilder image(BufferedImage image) {
            this.image = image;
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

    /**
     * Returns a new Notification that is an exact copy of this one
     *
     * @return new Notification that is a copy of this one
     * @throws CloneNotSupportedException
     */
    @Override
    public Notification clone() throws CloneNotSupportedException {
        Notification clone = (Notification) super.clone();
        clone.image = NotificationUtils.cloneImage(this.getImage()); // BufferedImages are not immutable

        return clone;
    }
}
