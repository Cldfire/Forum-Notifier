package com.cldfire.forumnotifier.util.notifications;

import com.cldfire.forumnotifier.ForumNotifier;
import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.embed.swing.SwingFXUtils;
import javafx.util.Duration;
import org.apache.commons.lang3.SystemUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Date;

class NotificationUtils {
    private static Environment environment;

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

        String path = copyResourceToTemp(notification.getImage());
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
            tray.setImage(fxImageFromAWTImage(notification.getImage()));
            tray.showAndDismiss(Duration.seconds(notification.getDelay()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendWindowsNotification(Notification notification) {
        TrayIcon trayIcon = new TrayIcon(notification.getImage());
        trayIcon.setImageAutoSize(true);
        if (SystemTray.isSupported()) {
            if (!windowsTrayIconExists(trayIcon)) { // make sure we don't duplicate it / cause an error
                try {
                    SystemTray.getSystemTray().add(trayIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            trayIcon.displayMessage(String.valueOf(notification.getTitle()), String.valueOf(notification.getSubtitle()), MessageType.NONE);
        }
    }

    /**
     * Checks whether a tray icon currently exists
     *
     * @param trayIcon the icon we're checking for existence
     * @return whether or not the icon is in the tray
     */
    private static boolean windowsTrayIconExists(TrayIcon trayIcon) {
        for (TrayIcon _trayIcon : SystemTray.getSystemTray().getTrayIcons()) {
            if (trayIcon == _trayIcon) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the environment variable based on detected OS
     */
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
     * Copies an image to the system temp directory so we can use it
     * from there. Returns the path to said temp file or null if it failed
     *
     * @param image The image to write
     * @return the path to the newly created temp file or null if it fails
     */
    private static String copyResourceToTemp(BufferedImage image) {
        try {
            File file = File.createTempFile(new Date().getTime() + "", ".png");
            file.deleteOnExit();
            ImageIO.write(image, "png", file);

            return file.getPath();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Copies a file to the system temp directory so we can use it
     * from there. Returns the path to said temp file or null if it failed
     *
     * @param inJarPath where we're getting our file from
     * @param fileType  the file extension to use ** INCLUDES '.' PREFIX **
     * @return the path to the newly created temp file or null if it fails
     */
    private static String copyResourceToTemp(String inJarPath, String fileType) {
        try {
            InputStream input = ForumNotifier.class.getClassLoader().getResourceAsStream(inJarPath);
            File file = File.createTempFile(new Date().getTime() + "", fileType);
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

    /**
     * Deep copies a BufferedImage
     *
     * @param image BufferedImage to copy
     * @return a new BufferedImage
     */
    static BufferedImage cloneImage (BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(image.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null).getSubimage(0, 0, image.getWidth(), image.getHeight());
    }

    private enum Environment {
        WINDOWS,
        OS_X,
        LINUX,
        OTHER
    }
}
