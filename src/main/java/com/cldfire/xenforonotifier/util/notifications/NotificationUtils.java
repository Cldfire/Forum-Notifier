package com.cldfire.xenforonotifier.util.notifications;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.util.LangUtils;
import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.util.Duration;
import org.apache.commons.lang3.SystemUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class NotificationUtils {
    private static Environment environment;
    private static TrayIcon trayIcon;

    /**
     * Helper to get a Buffered image from an EnumImage
     *
     * @param enumImage enum for path
     * @return BufferedImage from specified path
     */
    public static BufferedImage imageFromPath(EnumImage enumImage) {
        URL urlToImage = XenForoNotifier.class.getClassLoader().getResource(enumImage.toString());
        if (urlToImage == null) {
            throw new IllegalArgumentException("URL to Image cannot be null! Check your path!");
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(urlToImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return image;
    }

    /**
     * Sends a notification to the user
     * INTERNAL USE ONLY, SUBJECT TO CHANGE, USE NOTIFICATION OBJECT DIRECTLY
     *
     * @param notification the notification to be sent
     */
    static void sendNotification(Notification notification) {
        if (environment == null) {
            identifyEnvironment();
        }
        switch (environment) {
            case WINDOWS:
                sendWindowsNotification(notification);
                break;
            case OS_X:
                sendOSXNotification(notification);
                break;
            case LINUX:
                sendLinuxNotification(notification);
                break;
            case OTHER:
                sendOtherNotification(notification);
                break;
            default:
                throw new UnsupportedOperationException("An unknown environment was given!");
        }
    }

    private static void sendOSXNotification(Notification notification) {
        sendOtherNotification(notification); // TODO: OS X
    }

    private static void sendLinuxNotification(Notification notification) {
        sendOtherNotification(notification); // TODO: LINUX
    }

    private static void sendOtherNotification(Notification notification) {
        try {
            TrayNotification tray = new TrayNotification();

            tray.setTitle(notification.getTitle());
            tray.setMessage(notification.getSubtitle());
            tray.setAnimation(Animations.POPUP); // TODO: How are we standardizing animations (if at all)
            tray.setImage(notification.getFXImage());
            tray.showAndDismiss(Duration.seconds(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendWindowsNotification(Notification notification) {
        trayIcon = new TrayIcon(notification.getAWTImage(), LangUtils.translate(notification.getTitle()));
        trayIcon.setImageAutoSize(true);
        if (SystemTray.isSupported()) {
            if (!trayIconExists()) { // make sure we don't duplicate it / cause an error
                try {
                    SystemTray.getSystemTray().add(trayIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            trayIcon.displayMessage(notification.getTitle(), notification.getSubtitle(), MessageType.NONE);
        }
    }

    private static boolean trayIconExists() {
        for (TrayIcon _trayIcon : SystemTray.getSystemTray().getTrayIcons()) {
            if (trayIcon == _trayIcon) {
                return true;
            }
        }
        return false;
    }

    private static void identifyEnvironment() {
        if (SystemUtils.IS_OS_WINDOWS) {
            environment = Environment.WINDOWS;
        } else if (SystemUtils.IS_OS_MAC_OSX) {
            environment = Environment.OS_X;
        } else if (SystemUtils.IS_OS_LINUX) {
            // TODO: At some point I'd like to be able to go into specific DEs
            // TODO: Such as GNOME and KDE
            environment = Environment.LINUX;
        } else {
            environment = Environment.OTHER;
        }
    }

    private enum Environment {
        WINDOWS,
        OS_X,
        LINUX,
        OTHER
    }
}
