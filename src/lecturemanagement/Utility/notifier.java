/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturemanagement.Utility;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Amr
 */
public class notifier {

    public notifier(String title, String Text) {
      Platform.runLater(()->{
          
      
            Notifications notificationBuildeer = Notifications.create().
                    title(title).
                    text(Text).
                    graphic(null).
                    hideAfter(Duration.seconds(4)).
                    position(Pos.BOTTOM_LEFT);
            notificationBuildeer.darkStyle();
            notificationBuildeer.showConfirm();
       });
    }

}
