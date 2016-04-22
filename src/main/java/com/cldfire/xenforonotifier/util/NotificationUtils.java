package com.cldfire.xenforonotifier.util;

import com.github.plushaze.traynotification.animations.Animation;
import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.TrayNotification;
import javafx.scene.paint.Paint;

import javax.imageio.ImageIO;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.awt.TrayIcon.MessageType;

public class NotificationUtils { // TODO: createDetailedNotification
    private static TrayIcon trayIcon;

    public static void createNotification(String mainText, String message) { // TODO: Make sure this is thread-safe, use TrayNotification notifications for OS other than Windows
        ClassLoader cl = NotificationUtils.class.getClassLoader();

        if (System.getProperty("os.name").toLowerCase().contains("win")) { // because Java is a Windows f4n
            try {
                java.awt.Image image = ImageIO.read(cl.getSystemResource("images/notification-bell.png")); // TODO: Image loads, doesn't show up properly
                trayIcon = new TrayIcon(image, LangUtils.translate("trayicon.title"));
                trayIcon.setImageAutoSize(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (SystemTray.isSupported()) {
                if (!trayIconExists()) { // make sure we don't duplicate it / cause an error
                    try {
                        SystemTray.getSystemTray().add(trayIcon);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                trayIcon.displayMessage(mainText, message, MessageType.NONE);
            }
        } else { // also because Java is a Windows f4n
            Image image;
            try {
                image = new Image("images/notification-bell.png");
                TrayNotification tray = new TrayNotification();

                tray.setTitle(mainText);
                tray.setMessage(message);
                tray.setAnimation(Animations.POPUP);
                tray.setImage(image);
                tray.showAndDismiss(Duration.seconds(4));
            } catch (Exception e) {
                e.printStackTrace();
            }
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

}
