package com.cldfire.xenforonotifier.util.notifications;

import javafx.animation.Animation;

/**
 * Basic notification class
 */
public class DetailedNotification extends Notification {

    // TODO: How is this different
    // TODO: What does this expand upon? (Requested in NotificationUtil, so here we are)

    /**
     * Creates a new notification
     *
     * @param title     this notification's title
     * @param subtitle  this notification's subtitle
     * @param type      this notification's image type
     * @param animation this notification's animation
     */
    public DetailedNotification(String title, String subtitle, EnumImageType type, Animation animation) {
        super(title, subtitle, type, animation);
    }
}
