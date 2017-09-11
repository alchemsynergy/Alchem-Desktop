package sample;

import extras.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by VIPUL GOYAL on 9/9/2017.
 */
public class AlertScreenController {

    @FXML
    private Label alertText;

    @FXML
    private Button okButton;

    public void initialize()
    {
        alertText.setText(Message.getMsg());
    }

    public void alertAction(ActionEvent e)
    {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
