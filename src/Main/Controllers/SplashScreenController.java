package Main.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SplashScreenController {
    @FXML
    public Label wait_label;

    private LoginController loginController;

    public void init(LoginController loginController) {
    this.loginController=loginController;
    }
}
