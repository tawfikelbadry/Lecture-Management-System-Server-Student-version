package lecturemanagement.Client.login_signUp.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lecturemanagement.Client.login_signUp.control.LoginSocket;
import lecturemanagement.Utility.notifier;
import lecturemanagement.ref;

public class LoginController implements Initializable {

    @FXML
    JFXButton btnLogin;

    @FXML
    JFXTextField txtusername;
    @FXML
    JFXPasswordField txtpassword;

    Socket socket;
    DataInputStream input;
    DataOutputStream outPut;

    Parent root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void LoginButtonAction(ActionEvent event) {

        Stage stg = (Stage) btnLogin.getScene().getWindow();
        try {
            LoginSocket login = new LoginSocket(txtusername.getText().trim(), txtpassword.getText(), stg);
            login.SendLoginInfo();  // Start login Socket
        } catch (Exception e) {
new notifier("No Server", "There is no server open right now ");
        }
    }

    ////////////////////////////---- end of code ------------///////////////////////////////////////////////
//    @FXML
//    public void onLogingAction() {
//
//        try {
//            String userName = txtusername.getText();
//            String pass = txtpassword.getText();
//
//            if (userName.equals("") || pass.equals("")) {
//
//                return;
//            }
//
//            int state = input.readInt();
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//    }
//
//    public void login() {
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    root = FXMLLoader.load(getClass().getResource("../Main/MainFXML.fxml"));
//                    Scene scene = new Scene(root);
//                    Stage mainStage = new Stage();
//                    mainStage.setScene(scene);
//                    mainStage.show();
//                    Stage st = (Stage) btnLogin.getScene().getWindow();
//
//                    st.hide();
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//
//            }
//
//        });
//
//    }
//
//    // this method is for displaying msg to user
//    public void msgError(String msg) {
//
////        lblMessage.setText(msg);
////        lblMessage.setVisible(true);
////
////        new Timeline(new KeyFrame(Duration.seconds(5), null, new EventHandler<ActionEvent>() {
////
////            @Override
////            public void handle(ActionEvent event) {
////                lblMessage.setText("");
////                lblMessage.setVisible(false);
////            }
////        })).play();
//    }
}
