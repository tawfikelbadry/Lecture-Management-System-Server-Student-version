/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lecturemanagement.Client.Quiz.view;

import SerClass.SavedQuizquestion;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lecturemanagement.Client.Quiz.control.QuestionPane;
import lecturemanagement.Utility.notifier;
import lecturemanagement.ref;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static lecturemanagement.Client.login_signUp.control.LoginSocket.WaitingControl;
import lecturemanagement.Utility.FxDialogs;
import static lecturemanagement.Client.Main.control.RecieveSlideNumberAndQuizEndLecture.SlideNumberAndQuizEndLectureSocket;

/**
 * FXML Controller class
 *
 * @author Amr
 */
public class QuizFXMLController implements Initializable {

    @FXML
    private VBox QuizBox;
    @FXML
    private Label timeTxt;
    @FXML
    private StackPane MainCenterPane;
    @FXML
    private HBox BorderBox;
    @FXML
    private HBox BottomBox;
    //----------------------------------
    private ArrayList<SavedQuizquestion> Quiz;
    private ArrayList<QuestionPane> QuizQuestionListPane;
    private Timeline time;
    private int AllowToShowResult;
    private int second, minute;
    private int numberOfRightAns = 0;
    private final String gradeMarker = "-";
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        QuizQuestionListPane = new ArrayList<>();

    }

    @FXML
    void submitButtonAction(ActionEvent event) {
        QuizTimeOut();

    }

    public void setQuiz(ArrayList<SavedQuizquestion> Quiz) {
        this.Quiz = Quiz;
        drawQuetions();

    }

    @FXML
    void CloseAction(ActionEvent event) {
        FxDialogs.showInformation("info", "notice : you can enter the quiz again"
                + " from click right to taskbar icon and choose Show Quiz ");
        stage.close();

    }

    public void setTime(String Time) {
        minute = Integer.parseInt(Time);
        TimeTransition();

    }

    public void setAllowToShowResult(int AllowToShowResult) {
        this.AllowToShowResult = AllowToShowResult;
    }

    private void TimeTransition() {
        time = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            if (second == 0 && minute == 0) {
                QuizTimeOut();
            }
            if (second == 0) {
                minute--;
                second = 60;
            }
            second--;
            timeTxt.setText(minute + " : " + second);
        }));
        time.setCycleCount(Timeline.INDEFINITE);
        time.play();
    }

    private void QuizTimeOut() {
        new notifier("info", " Quiz Time out ");
        time.stop();
        calculateGrade();
        SendGrade();
        ref.QuizRunning = false;
        WaitingControl.setStatus("no recent action");
        stage.close();

    }

    public void QuizTerminatorFromDoctor(String status) {
        Platform.runLater(() -> {
            new notifier("info", " Quiz is Stopped from doctor ");
            time.stop();
            WaitingControl.setStatus("no recent action");
            if (status.equals("1")) {
                calculateGrade();
                SendGrade();
            }
            MainCenterPane.getChildren().clear();
            ref.QuizRunning = false;
        });
        stage.close();
    }

    private void SendGrade() {
        try {
            DataOutputStream out = new DataOutputStream(SlideNumberAndQuizEndLectureSocket.getOutputStream());
            out.writeUTF(ref.username + gradeMarker + numberOfRightAns);
        } catch (IOException ex) {
            Logger.getLogger(QuizFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void calculateGrade() {
        for (int i = 0; i < QuizQuestionListPane.size(); i++) {
            if (QuizQuestionListPane.get(i).getSelectedAnswer() == Integer.parseInt(Quiz.get(i).getRightans()) - 1) {
                numberOfRightAns++;
            }
        }
        MainCenterPane.getChildren().clear();
        if (AllowToShowResult == 0) {
            return;
        }
        Text result = new Text("Your correct Questions is " + numberOfRightAns + " from " + QuizQuestionListPane.size() + " Questions");
        result.setFont(Font.font("Arial", 20));
        result.setFill(Color.BLACK);
        MainCenterPane.getChildren().add(result);

    }

    private void drawQuetions() {
        for (int i = 0; i < Quiz.size(); i++) {
            QuestionPane question = new QuestionPane(Quiz.get(i).getQuestion(), Quiz.get(i).getChoicesData());
            QuizBox.getChildren().add(question);
            QuizQuestionListPane.add(question);
        }
    }

    public HBox getBorderBox() {
        return BorderBox;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        resizePane(stage.getScene(), QuizBox);
    }

    public Stage getStage() {
        return this.stage;
    }

    public void resizePane(Scene scene, VBox node) {

        scene.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            node.setPrefWidth((double) newSceneWidth);
            node.setMaxWidth((double) newSceneWidth);
        });
        scene.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            node.setPrefHeight((double) newSceneHeight - BottomBox.getHeight());
            node.setMaxHeight((double) newSceneHeight);
        });
    }
}
