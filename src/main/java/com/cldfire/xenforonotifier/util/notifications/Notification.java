package com.cldfire.xenforonotifier.util.notifications;

import javafx.animation.Animation;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

/**
 * Represents a notification
 */
public class Notification {
    private final String title;
    private final String subtitle;
    private final BufferedImage image;
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
     * @param image this notification's image
     */
    public Notification(String title, BufferedImage image) {
        this(title, null, image, null);
    }

    /**
     * Creates a new notification
     *
     * @param title    this notification's title
     * @param subtitle this notification's subtitle
     * @param image    this notification's image
     */
    public Notification(String title, String subtitle, BufferedImage image) {
        this(title, subtitle, image, null);
    }

    /**
     * Creates a new notification
     *
     * @param title     this notification's title
     * @param subtitle  this notification's subtitle
     * @param image     this notification's image
     * @param animation this notification's animation
     */
    public Notification(String title, String subtitle, BufferedImage image, Animation animation) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
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
        this.image = builder.image;
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
     * Gets the image associated with this notification
     *
     * @return the AWT image for this notification
     */
    public BufferedImage getAWTImage() {
        return this.image;
    }

    /**
     * Gets the image associated with this notification
     *
     * @return the FX image for this notification
     */
    public javafx.scene.image.Image getFXImage() {
        return SwingFXUtils.toFXImage(this.getAWTImage(), null);
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
        private BufferedImage image;
        private Animation animation;

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

        public NotificationBuilder animation(Animation animation) {
            this.animation = animation;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
