package com.cldfire.xenforonotifier.util.notifications;

import com.cldfire.xenforonotifier.XenForoNotifier;
import com.cldfire.xenforonotifier.util.LangUtils;
import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.apache.commons.lang3.SystemUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Date;

class NotificationUtils {
    private static Environment environment;
    private static TrayIcon WINDOWS_trayIcon;

    /**
     * Helper to get a Buffered image from an EnumImageType
     *
     * @param enumImageType enum for path
     * @return BufferedImage from specified path
     */
    private static BufferedImage awtImageFromEnum(EnumImageType enumImageType) {
        URL urlToImage = XenForoNotifier.class.getClassLoader().getResource(enumImageType.toString());
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
     * Helper to get FX images from BufferedImages
     *
     * @param image BufferedImage to convert
     * @return JavaFX Image
     */
    private static javafx.scene.image.Image fxImageFromAWTImage(BufferedImage image) {
        return SwingFXUtils.toFXImage(image, null);
    }

    /**
     * Helper to get an FX Image from an EnumImageType
     *
     * @param enumImageType enum for path
     * @return BufferedImage from specified path
     */
    private static Image fxImageFromEnum(EnumImageType enumImageType) {
        return fxImageFromAWTImage(awtImageFromEnum(enumImageType));
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
        /**
         * Because we're going to be using a distro-specific binary to accomplish native-ish notifications
         * check to make sure it exists, and if it doesn't use our generic system instead
         */
        File notifyBin = new File("/usr/bin/notify-send");
        if (!notifyBin.exists() || notifyBin.isDirectory()) {
            sendOtherNotification(notification);
            return; // Bail out
        }

        String path = copyResourceToTemp(notification);
        if (path == null) {
            sendOtherNotification(notification);
            return; // Bail out
        }

        String[] cmd = {"/usr/bin/notify-send",
                "-t",
                String.valueOf(notification.getDelay() * 1000),
                String.valueOf(notification.getTitle()),
                String.valueOf(notification.getSubtitle()),
                "-i",
                path};
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendOtherNotification(Notification notification) {
        try {
            TrayNotification tray = new TrayNotification();

            tray.setTitle(String.valueOf(notification.getTitle()));
            tray.setMessage(String.valueOf(notification.getSubtitle()));
            tray.setAnimation(Animations.POPUP);
            tray.setImage(fxImageFromEnum(notification.getImageType()));
            tray.showAndDismiss(Duration.seconds(notification.getDelay()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendWindowsNotification(Notification notification) {
        System.out.println("Sending windows notification");
        WINDOWS_trayIcon = new TrayIcon(awtImageFromEnum(notification.getImageType()), "test"); // TODO: This needs to use the translate method
        WINDOWS_trayIcon.setImageAutoSize(true);
        if (SystemTray.isSupported()) {
            if (!trayIconExists()) { // make sure we don't duplicate it / cause an error
                try {
                    SystemTray.getSystemTray().add(WINDOWS_trayIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            WINDOWS_trayIcon.displayMessage(String.valueOf(notification.getTitle()), String.valueOf(notification.getSubtitle()), MessageType.NONE);
        }
    }

    private static boolean trayIconExists() {
        for (TrayIcon _trayIcon : SystemTray.getSystemTray().getTrayIcons()) {
            if (WINDOWS_trayIcon == _trayIcon) {
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
            environment = Environment.LINUX;
        } else {
            environment = Environment.OTHER;
        }
    }

    /**
     * Copies the image from a notification to the system temp directory so we can use it
     * from there. Returns the path to said temp file or null if it failed
     *
     * @param notification where we're getting our image from
     * @return the path to the newly created temp file or null if it fails
     */
    private static String copyResourceToTemp(Notification notification) {
        File file;
        String resource = notification.getImageType().toString();
        try {
            InputStream input = XenForoNotifier.class.getClassLoader().getResourceAsStream(resource);
            file = File.createTempFile(new Date().getTime() + "", ".png");
            OutputStream out = new FileOutputStream(file);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            input.close();
            file.deleteOnExit();

            return file.getPath();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private enum Environment {
        WINDOWS,
        OS_X,
        LINUX,
        OTHER
    }

    /*
    ┈┈┈┈╭╯╭╯╭╯┈┈┈┈┈
    ┈┈┈╱▔▔▔▔▔╲▔╲┈┈┈
    ┈┈╱┈╭╮┈╭╮┈╲╮╲┈┈
    ┈┈▏┈▂▂▂▂▂┈▕╮▕┈┈
    ┈┈▏┈╲▂▂▂╱┈▕╮▕┈┈
    ┈┈╲▂▂▂▂▂▂▂▂╲╱┈┈
    F U C K  Y E A H
    E A T  T A C O S
     */
}
