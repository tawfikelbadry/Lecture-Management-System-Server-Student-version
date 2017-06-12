package lecturemanagement.Client.Waiting.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import lecturemanagement.Client.Waiting.control.ReceiveLectureListener;
import lecturemanagement.ref;

/**
 *
 * @author Amr
 */
public class WaitingFXMLController implements Initializable {

    @FXML
    private BorderPane background;
    @FXML
    private Text status;

    @FXML
    private Text usernameTxt;

    //----------------------------------
    public static final int WAITING_WIDTH = 190;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ref.SlideQuizObject.StartListen();
        ReceiveLectureListener lec = new ReceiveLectureListener();
        lec.StartRecieveLecture();
        background.setPrefWidth(WAITING_WIDTH);
    }

    public String getStatus() {
        return status.getText();
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    public String getUsernameTxt() {
        return usernameTxt.getText();
    }

    public void setUsernameTxt(String usernameTxt) {
        this.usernameTxt.setText(usernameTxt);
    }

}
