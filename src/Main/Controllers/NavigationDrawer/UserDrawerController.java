package Main.Controllers.NavigationDrawer;

import Main.ErrorAndInfo.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;

import java.net.MalformedURLException;
import java.net.URL;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class UserDrawerController implements Initializable{
    @FXML
    ImageView user_image;

    private String imageFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addOrChangePhoto(ActionEvent actionEvent) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.bmp", "*.png", "*.jpg", "*.gif")); // limit fileChooser options to image files
        File selectedFile = fileChooser.showOpenDialog(user_image.getScene().getWindow());

        if (selectedFile != null) {

            imageFile = selectedFile.toURI().toURL().toString();

            Image image = new Image(imageFile);
            user_image.setImage(image);
        } else {
            new AlertBox(((Stage)((Node)actionEvent.getSource()).getScene().getWindow()),
                    new FXMLLoader(getClass().getResource("../../../Resources/Layouts/alert_stage.fxml")),
                    false,
                    "Could not load image!");
        }

        
    }
}
