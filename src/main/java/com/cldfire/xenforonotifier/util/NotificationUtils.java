package com.cldfire.xenforonotifier.util;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class NotificationUtils {
    private static Image image = Toolkit.getDefaultToolkit().getImage(""); // TODO: Get this working
    private static TrayIcon trayIcon = new TrayIcon(image, "Spigot Notifier");

    public static void createNotification(String mainText, String message) { // TODO: Make sure this is thread-safe, use TrayNotification notifications for OS other than Windows
        if (SystemTray.isSupported()) {
            try {
                SystemTray.getSystemTray().add(trayIcon); // TODO: Prevent this from duplicating
            } catch (Exception e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(mainText, message, MessageType.NONE);
        }
    }

}
