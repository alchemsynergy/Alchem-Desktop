package Main.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertScreenController {

    @FXML
    private Label alert_text;

    @FXML
    private Button ok_button;


    public void alertAction(ActionEvent actionEvent)
    {
        Stage stage = (Stage) ok_button.getScene().getWindow();
        stage.close();
    }
}
