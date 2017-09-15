package sample;

import JdbcConnection.jdbc;
import extras.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

/**
 * Created by A.K on 9/12/2017.
 */
public class registerController {

    ObservableList<String> StoreTypes = FXCollections.observableArrayList("Wholesaler", "Retailer");

    @FXML
    private TextField ownerName, ownerPhone, ownerAddress, ownerPan, ownerEmail, storeName, storePhone, storeAddress, storeGst, licenseNumber, username;

    @FXML
    private DatePicker licenseValidity;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox storeType;

    @FXML
    private Button cancelButton;

    public void initialize()
    {
        storeType.setItems(StoreTypes);
        storeType.getSelectionModel().selectFirst();
    }
    public void onRegister(ActionEvent ae)
    {
        String ownerName1 = ownerName.getText();
        String ownerPhone1 = ownerPhone.getText();
        String ownerAddress1 = ownerAddress.getText();
        String ownerPan1 = ownerPan.getText();
        String ownerEmail1 = ownerEmail.getText();
        String storeName1 = storeName.getText();
        String storePhone1 = storePhone.getText();
        String storeAddress1 = storeAddress.getText();
        String storeGst1 = storeGst.getText();
        String licenseNumber1 = licenseNumber.getText();
        String user = licenseNumber1;
        String pass = password.getText();

        Node source = (Node) ae.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("AlertScreen.fxml"));

        String sql = null;
        int increment = 0;
        String regexPhone = "^[0-9]*$";
        String regexName = "^[a-zA-Z]*$";

        int user_type_id;
        if(storeType.getSelectionModel().getSelectedItem() == "Wholesaler")
        {
            user_type_id = 1;
        }
        else
            user_type_id = 2;

        if(ownerName1.equals("") || ownerAddress1.equals("") || ownerPan1.equals("") || storeName1.equals("") || storeAddress1.equals("")
                || storeGst1.equals("") || licenseNumber1.equals("") || licenseValidity.getValue() == null || pass.equals(""))
        {
            new AlertBox().alertfunction(currentStage,fxmlLoader,"Fill in the missing fields !!");
        }
        else if (!ownerPhone1.matches(regexPhone) || !storePhone1.matches(regexPhone))
        {
            new AlertBox().alertfunction(currentStage,fxmlLoader,"Phone must be a number !!");
        }
        else if (!ownerName1.matches(regexName) || !storeName1.matches(regexName))
        {
            new AlertBox().alertfunction(currentStage,fxmlLoader,"Name must be alphabets only");
        }
        else
        {
            String licenseValidity1 = licenseValidity.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            try
            {
                Connection conn = jdbc.jconn_ankit();

                PreparedStatement ps = conn.prepareStatement("INSERT INTO user_access VALUES (?,?,?,DEFAULT )");
                ps.setString(1, user);
                ps.setString(2, pass);
                ps.setInt(3, user_type_id);
                ps.executeUpdate();

                Statement st = conn.createStatement();
                sql = "SELECT MAX(user_access_id) FROM user_access";
                ResultSet rs = st.executeQuery(sql);
                if(rs.next())
                {
                    increment = rs.getInt(1);
                }

                ps = conn.prepareStatement("INSERT INTO owner_info VALUES (?,?,?,?,?,?,?,?,DEFAULT )");
                ps.setInt(1, increment);
                ps.setString(2, ownerName1);
                ps.setString(3, ownerPhone1);
                ps.setString(4, ownerPan1);
                ps.setString(5, ownerAddress1);
                ps.setString(6, ownerEmail1);
                ps.setString(7, licenseNumber1);
                ps.setString(8, licenseValidity1);
                ps.executeUpdate();

                ps = conn.prepareStatement("INSERT INTO store_info VALUES (?,?,?,?,?,DEFAULT )");
                ps.setInt(1, increment);
                ps.setString(2, storeName1);
                ps.setString(3, storePhone1);
                ps.setString(4, storeAddress1);
                ps.setString(5, storeGst1);
                ps.executeUpdate();

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            new AlertBox().alertfunction(currentStage,fxmlLoader,user + "" + ": User Registered");

            ownerName.setText(null);
            ownerPhone.setText(null);
            ownerAddress.setText(null);
            ownerPan.setText(null);
            ownerEmail.setText(null);
            storeName.setText(null);
            storePhone.setText(null);
            storeAddress.setText(null);
            storeGst.setText(null);
            licenseNumber.setText(null);
            licenseValidity.setValue(null);
            username.setText(null);
            password.setText(null);
        }

    }
    public void onChangeLicense()
    {
        username.setText(licenseNumber.getText());
    }

    public void cancel()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
