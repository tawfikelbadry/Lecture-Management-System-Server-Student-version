package lecturemanagement.Client.login_signUp.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lecturemanagement.Client.ChatRoom.StudentChat.StudentChatFXMLController;
import lecturemanagement.Client.Main.control.MessageSocket;
import lecturemanagement.Client.Quiz.view.QuizFXMLController;
import lecturemanagement.Client.Waiting.view.WaitingFXMLController;
import static lecturemanagement.Client.Waiting.view.WaitingFXMLController.WAITING_WIDTH;
import lecturemanagement.DataTransferProtocol;
import lecturemanagement.Utility.FxDialogs;
import lecturemanagement.Utility.LoadFXML;
import lecturemanagement.Utility.ScreenTools;
import lecturemanagement.Utility.notifier;

import lecturemanagement.ref;

/**
 *
 * @author Amr
 */
public class LoginSocket implements Runnable {

    private Socket socket;
    private String username, password;
    private DataInputStream input;
    private Stage loginStage;
    private DataOutputStream outPut;
    private ScreenTools screen;
    private final String WatingPath = "/lecturemanagement/Client/Waiting/view/WaitingFXML.fxml";
    private final String ChatPath = "/lecturemanagement/Client/ChatRoom/StudentChat/StudentChatFXML.fxml";
    public static StudentChatFXMLController ChatController;
    public static WaitingFXMLController WaitingControl;
    private Stage stage;

    public LoginSocket(String username, String password, Stage lStage) {
        loginStage = lStage;
        this.username = username;
        this.password = password;

    }

    public void SendLoginInfo() {
        Thread th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(DataTransferProtocol.ServerAddress, DataTransferProtocol.SignInOutPort);
            input = new DataInputStream(socket.getInputStream());
            outPut = new DataOutputStream(socket.getOutputStream());
            outPut.writeUTF(username + "-" + password);
            String state = input.readUTF();
            Platform.runLater(() -> {
                System.out.println(state + " --------------------");
                moveToNextStage(state);
            });
        } catch (IOException ex) {
            FxDialogs.showInformation("info", ex.getMessage());
            System.exit(0);
        }
    }

    private void moveToNextStage(String state) {

        screen = new ScreenTools();
        if ("1".equals(state)) {
            loginStage.close();
            ref.username = username;    // set username  
            LoadWaitingScreen();
            LoadChat();
            //--------------------------------
            new MessageSocket().StartListen(); // Start Listen Socket
            //--------------------------------
            return;
        } else if ("2".equals(state)) {
            //  new notifier("error", "You already Logged  ");
            return;
        }
        FxDialogs.showInformation("info", "incorrect password or user");

    }

    private void LoadChat() {
        LoadFXML load = new LoadFXML();
        load.LoadFXMLRootedStage(stage, ChatPath);
        ChatController = (StudentChatFXMLController) load.getController();
        Stage stageChat = load.getStage();
        stageChat.setX(screen.getScreenWidth() - stageChat.getWidth());
        stageChat.setY(screen.getScreenHeight() - stageChat.getHeight() - screen.getTaskBarHeight());
    }

    private void LoadWaitingScreen() {
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        LoadFXML loader = new LoadFXML();
        Parent root = loader.LoadFXML(WatingPath);
        stage.setScene(new Scene(root));
        WaitingControl = (WaitingFXMLController) loader.getController();
        WaitingControl.setUsernameTxt(username);
        stage.setX(screen.getScreenWidth() - WAITING_WIDTH);
        stage.setY(0);
        stage.show();
        stage.toFront();

    }

}
