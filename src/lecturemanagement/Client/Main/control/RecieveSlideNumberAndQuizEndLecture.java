/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturemanagement.Client.Main.control;

import SerClass.SavedQuizquestion;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;

import lecturemanagement.Client.Quiz.view.QuizFXMLController;
import lecturemanagement.Client.Waiting.control.ReceiveLectureListener;
import static lecturemanagement.Client.Waiting.control.ReceiveLectureListener.SlideStage;
import static lecturemanagement.Client.login_signUp.control.LoginSocket.WaitingControl;
import lecturemanagement.Utility.LoadFXML;
import lecturemanagement.ref;
import static lecturemanagement.DataTransferProtocol.QuizSlideNumberPort;
import static lecturemanagement.DataTransferProtocol.ServerAddress;
import lecturemanagement.Utility.FxDialogs;
import lecturemanagement.Utility.undecoratedStage.undecoratedStageOptions;

/**
 *
 * @author Amr
 */
public class RecieveSlideNumberAndQuizEndLecture implements Runnable {

    public static Socket SlideNumberAndQuizEndLectureSocket;
    private DataInputStream input;
    private Pane LectureView;
    private final String EndLectureMarker = "END_LECTURE";
    private final String StartMarkOfQuizSending = "QUIZ_START";
    private final String EndMarkOfQuizSending = "QUIZ_END";
    private String QuizTerminator_markSending = "QUIZ_TERMINATE";
    private final String path = "tempFile/";
    private final String QuizPathFXML = "/lecturemanagement/Client/Quiz/view/QuizFXML.fxml";
    public static QuizFXMLController QuizControl;
    private ArrayList<SavedQuizquestion> QuizQuestionList;

    public void setLectureView(Pane LectureView) {
        this.LectureView = LectureView;
    }

    public void StartListen() {
        QuizQuestionList = new ArrayList<>();
        Thread th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        try {

            while (true) {
                SlideNumberAndQuizEndLectureSocket = new Socket(ServerAddress, QuizSlideNumberPort);
                input = new DataInputStream(SlideNumberAndQuizEndLectureSocket.getInputStream());
                String Str = input.readUTF();
                String QuizTerminatorAr[] = DecapsulationReceived(Str);
                if (StartMarkOfQuizSending.equals(Str)) {
                    ref.QuizRunning = true;
                    RecieveQuiz();
                    if (SlideStage.isShowing()) {
                        Platform.runLater(() -> {
                            SlideStage.close();
                        });
                    }
                } else if (QuizTerminator_markSending.equals(QuizTerminatorAr[0])) {
                    String status = QuizTerminatorAr[1];
                    QuizControl.QuizTerminatorFromDoctor(status);
                } else if (Str.equals(EndLectureMarker)) {   // lecture terminated
                    ref.LectureRunning = false;
                    if (SlideStage != null && SlideStage.isShowing()) {
                        Platform.runLater(() -> (SlideStage.close()));
                    }
                    LectureView.setBackground(Background.EMPTY);
                    WaitingControl.setStatus("Lecture Terminated");
                    ReceiveLectureListener lec = new ReceiveLectureListener();
                    lec.StartRecieveLecture();
                }
                if (!ref.QuizRunning) {
                    MoveToSlide(Str);
                }
            }
        } catch (IOException ex) {
            FxDialogs.showInformation("info", ex.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            FxDialogs.showInformation("info", ex.getMessage());
            System.exit(0);
        }
    }

    private void MoveToSlide(String number) {
        ChangeImage(new File(path + number + ".png"), LectureView);
    }

    public void ChangeImage(File file, Pane borderRoot) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            //-------------------------------
            BackgroundImage bgImg = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
            borderRoot.setBackground(new Background(bgImg));
        } catch (IOException ex) {
        }
    }

    public void RecieveQuiz() throws IOException, ClassNotFoundException {
        String Time, AllowToShowResult;
        QuizQuestionList.clear();
        input = new DataInputStream(SlideNumberAndQuizEndLectureSocket.getInputStream());
        String split[] = DecapsulationReceived(input.readUTF());
        Time = split[0];
        AllowToShowResult = split[1];

        while (true) {
            ObjectInputStream inObject = new ObjectInputStream(SlideNumberAndQuizEndLectureSocket.getInputStream());
            SavedQuizquestion question = (SavedQuizquestion) inObject.readObject();
            if (question.getQuestion().equals(EndMarkOfQuizSending)) {
                break;
            }
            QuizQuestionList.add(question);
        }
        Platform.runLater(() -> {
            // loadQuizFXML
            loadQuizFXML(AllowToShowResult, Time);
            // Quiz Running
            ref.QuizRunning = true;
        });
    }

    private void loadQuizFXML(String AllowToShowResult, String Time) {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = null;
        LoadFXML loader = new LoadFXML();
        root = loader.LoadFXML(QuizPathFXML);
        stage.setScene(new Scene(root));
        QuizControl = (QuizFXMLController) loader.getController();
        //------------
        QuizControl.setQuiz(QuizQuestionList);
        QuizControl.setTime(Time);
        QuizControl.setAllowToShowResult(Integer.parseInt(AllowToShowResult));
        QuizControl.setStage(stage);
        HBox border = QuizControl.getBorderBox();
        //------------

        new undecoratedStageOptions(stage, border).fullTools();
        stage.show();
        // QuizShowed
        WaitingControl.setStatus("Quiz running");
    }

    private String[] DecapsulationReceived(String rec) {
        String[] split = rec.split("-"); //  -----------> split[0] for Time  , split[1] for Allowing To Show Result        
        return split;                    // split[0] QuizMarkerTirminator  , if he want get result or not 
    }
}
