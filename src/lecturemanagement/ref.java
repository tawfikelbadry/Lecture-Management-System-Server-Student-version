/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturemanagement;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lecturemanagement.Client.Main.control.RecieveSlideNumberAndQuizEndLecture;
import lecturemanagement.Client.Tray.SystemTray;

/**
 *
 * @author Amr
 */
public class ref {

    public static String username;    // user name of this Student
    public static Stage SlideStage;
    //-------------------------------------------
    // main 
    public static BorderPane RootPane;     // main pane 
    public static BorderPane SlidePane;     // slide pane  
    public static boolean LectureRunning;
    //-------------------------------------------
    public static SystemTray tray;
    //--------------------------------------------
    // Quiz 
    public static boolean QuizRunning;
    public static RecieveSlideNumberAndQuizEndLecture SlideQuizObject = new RecieveSlideNumberAndQuizEndLecture();
    //--------------------------------------------
}
