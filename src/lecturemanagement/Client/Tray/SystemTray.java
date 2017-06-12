/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturemanagement.Client.Tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javax.imageio.ImageIO;
import static lecturemanagement.Client.Main.control.RecieveSlideNumberAndQuizEndLecture.QuizControl;
import static lecturemanagement.DataTransferProtocol.ServerAddress;
import static lecturemanagement.DataTransferProtocol.SignInOutPort;
import lecturemanagement.Utility.notifier;
import lecturemanagement.ref;
import static lecturemanagement.ref.username;

/**
 *
 * @author Amr
 */
public class SystemTray {

    private static final String iconImageLoc = "C:\\Users\\tito\\Desktop\\LectureManagementClient\\src\\resource\\doctor.png";

    private Timer notificationTimer = new Timer();
    private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();

    private void addAppToTray() {
        
        try {
            java.awt.Toolkit.getDefaultToolkit();
            if (!java.awt.SystemTray.isSupported()) {
                Platform.exit();
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            File imageLoc = new File(iconImageLoc);
            java.awt.Image image = ImageIO.read(imageLoc);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);
            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    if (ref.QuizRunning) {
                        new notifier("info", "can't open lecture while running quiz ");
                        return;
                    }
                    if (ref.LectureRunning && ref.SlideStage != null) {
                        Platform.runLater(SystemTray.this::showStage);
                    } else {
                        new notifier("info", "The doctor doesn't share lecture ");
                    }
                }
            });
            // if the user selects the default menu item (which includes the app name), 
            // show the main app stage.
            java.awt.MenuItem openItem = new java.awt.MenuItem("Show Lecture");
            openItem.addActionListener((ActionEvent event) -> {
                if (ref.QuizRunning) {
                    new notifier("info", "can't open lecture while running quiz ");
                    return;
                }
                if (ref.LectureRunning && ref.SlideStage != null) {
                    Platform.runLater(SystemTray.this::showStage);
                } else {
                    new notifier("info", "The doctor doesn't share lecture ");
                }
            });

            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                notificationTimer.cancel();
                Platform.exit();
                sendLogoutToServer();
                tray.remove(trayIcon);
            });
            java.awt.MenuItem ShowQuizItem = new java.awt.MenuItem("Show Quiz");
            ShowQuizItem.addActionListener(event -> {
                if (ref.QuizRunning && !QuizControl.getStage().isShowing()) {
                    Platform.runLater(() -> (QuizControl.getStage().show()));

                } else {
                    new notifier("info", " you haven't a recent Quiz ");
                }
            });
            java.awt.MenuItem NoteOption = new java.awt.MenuItem("note option");
            ShowQuizItem.addActionListener(event -> {
                // note 
            });

            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.add(ShowQuizItem);
            popup.add(NoteOption);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            // create a timer which periodically displays a notification message.
            notificationTimer.schedule(
                    new TimerTask() {
                @Override
                public void run() {
                    javax.swing.SwingUtilities.invokeLater(()
                            -> trayIcon.displayMessage(
                                    "Notice",
                                    "Hi , click right to see options ",
                                    java.awt.TrayIcon.MessageType.INFO
                            )
                    );
                }
            }, 5000, 1500000);

            // add the application tray icon to the system tray.
            tray.add(trayIcon);
        } catch (Exception e ) {
          new notifier("Error", "some error occured");
        }
    }

    private void showStage() {
        
        ref.SlideStage.show();
        ref.SlideStage.toFront();
    }

    public SystemTray() {
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        Platform.setImplicitExit(false);
    }

    private void sendLogoutToServer() {
        try {
            Socket client = new Socket(ServerAddress, SignInOutPort);
            DataOutputStream output = new DataOutputStream(client.getOutputStream());
            output.writeUTF(username);
        } catch (Exception ex) {
            
        }
    }

}
