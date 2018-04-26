package Main.Controllers.NavigationDrawer;

import Main.ErrorAndInfo.AlertBox;
import Main.JdbcConnection.JDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserDrawerController implements Initializable {
    static int userAccessID;
    @FXML
    ImageView user_image;
    //Owner Info related variables
    @FXML
    Label owner_name, owner_phone, owner_email, owner_address;
    //Store Info related variables
    @FXML
    Label store_name, store_phone, store_address;
    //License Info related variables
    @FXML
    Label license_number, license_valid_till;
    //Variables related to 'Alchem Validity'
    @FXML
    Label alchem_validity;
    private String imageFile;

    public static void setUserAccessID(int id) {
        userAccessID = id;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setUserData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        alchem_validity.setText("1st Jan, 2019");
    }

    public void setUserData() throws SQLException {
        Connection dbConnection = JDBC.databaseConnect();
        StringBuffer ownerSQLQuery = new StringBuffer("SELECT * FROM owner_info where user_access_id=");
        ownerSQLQuery.append(userAccessID);
        try {
            ResultSet ownerResult = dbConnection.createStatement().executeQuery(ownerSQLQuery.toString());
            while (ownerResult.next()) {
                owner_name.setText(ownerResult.getString("name"));
                owner_address.setText(ownerResult.getString("address"));
                if(ownerResult.getString("email")==null)
                    owner_email.setText("Not Provided");
                else
                    owner_email.setText(ownerResult.getString("email"));
                owner_phone.setText(ownerResult.getString("mobile_number"));
                license_number.setText(ownerResult.getString("license_number"));
                license_valid_till.setText(ownerResult.getString("license_valid"));
            }
            ownerResult.close();

            StringBuffer storeSQLQuery = new StringBuffer("SELECT * FROM owner_info where user_access_id=");
            storeSQLQuery.append(userAccessID);
            ResultSet storeResult = dbConnection.createStatement().executeQuery(ownerSQLQuery.toString());
            while (storeResult.next()) {
                store_name.setText(storeResult.getString("name"));
                store_address.setText(storeResult.getString("address"));
                store_phone.setText(storeResult.getString("mobile_number"));
            }
            storeResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnection.close();
        }

    }

    public void addOrChangePhoto() throws MalformedURLException {
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
            new AlertBox(((Stage) user_image.getScene().getWindow()),
                    new FXMLLoader(getClass().getResource("../../../Resources/Layouts/alert_stage.fxml")),
                    false,
                    "Could not load image!");
        }


    }
}
