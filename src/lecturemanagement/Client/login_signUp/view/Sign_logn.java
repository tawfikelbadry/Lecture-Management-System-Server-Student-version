package lecturemanagement.Client.login_signUp.view;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lecturemanagement.Client.Tray.SystemTray;
import static lecturemanagement.DataTransferProtocol.ServerAddress;
import static lecturemanagement.DataTransferProtocol.SignInOutPort;
import lecturemanagement.ref;
import static lecturemanagement.ref.username;

public class Sign_logn extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("login_signUp.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Client version");
        primaryStage.setOnCloseRequest(e -> {
            sendLogoutToServer();
            System.exit(0);
        }
        );
        primaryStage.setWidth(600);
        primaryStage.setHeight(720);
        primaryStage.show();
        ref.tray = new SystemTray();

    }

    public static void main(String[] args) {
        launch(args);
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
