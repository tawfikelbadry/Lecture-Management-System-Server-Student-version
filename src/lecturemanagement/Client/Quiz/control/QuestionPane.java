package lecturemanagement.Client.Quiz.control;

import com.jfoenix.controls.JFXRadioButton;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class QuestionPane extends VBox {

    private HBox radiosPane;
    private static int questionNum = 1;
    private Label lblQuestion;
    private ArrayList<RadioButton> rdbtns = new ArrayList();
    private ToggleGroup grp = new ToggleGroup();

    public QuestionPane(String questinText, ArrayList<String> answers) {
        radiosPane = new HBox();
        lblQuestion = new Label(" (" + questionNum + ") " + questinText);
        questionNum++;
        lblQuestion.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        lblQuestion.setPadding(new Insets(15, 20, 10, 20));
        for (int i = 0; i < answers.size(); i++) {
            JFXRadioButton rdbtn = new JFXRadioButton(answers.get(i));
            rdbtn.setFont(new Font(13));
            rdbtn.setPadding(new Insets(15));
            rdbtn.setToggleGroup(grp);
            rdbtns.add(rdbtn);
            radiosPane.getChildren().add(rdbtns.get(i));
        }
        this.getChildren().add(lblQuestion);
        this.getChildren().add(radiosPane);
        this.setStyle("fx-background-color :#3498db");

    }

    public int getSelectedAnswer() {
        for (int i = 0; i < rdbtns.size(); i++) {
            if (rdbtns.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

}
