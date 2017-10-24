package Main.Controllers;

import Main.Controllers.Retailers.ViewSaleController;
import Main.ErrorAndInfo.AlertBox;
import Main.Helpers.Billing;
import Main.Helpers.Billing_history;
import Main.Helpers.Purchase;
import Main.Helpers.Retailers.Sale;
import Main.Helpers.UserInfo;
import Main.JdbcConnection.JDBC;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class AddPurchaseController {

    ObservableList<String> Mode = FXCollections.observableArrayList("Cash", "Debit Card", "Credit Card");
    ObservableList<String> Type = FXCollections.observableArrayList("prescribed", "non prescribed");
    ObservableList<Purchase> table_data = FXCollections.observableArrayList();

    @FXML
    private TextField wholesaler_name, bill_no, hsn_code,medicine_name,salt,company,batch_number,quantity,cost_price,mrp,total_amount;

    @FXML
    private DatePicker date,expiry_date,mfg_date;

    @FXML
    private TableView<Purchase> purchase_table;

    @FXML
    private TableColumn<Purchase, String> purchase_item;

    @FXML
    private TableColumn<Purchase, String> purchase_batch;

    @FXML
    private TableColumn<Purchase, Integer> purchase_quantity;

    @FXML
    private TableColumn<Purchase, Float> purchase_cost;

    @FXML
    private TableColumn<Purchase, Float> purchase_mrp;

    @FXML
    private ComboBox mode,medicine_type;

    public void initialize() {

        purchase_table.setItems(table_data);
        mode.setItems(Mode);
        mode.getSelectionModel().selectFirst();
        medicine_type.setItems(Type);
        medicine_type.getSelectionModel().selectFirst();
        medicine_name.setDisable(true);
        salt.setDisable(true);
        company.setDisable(true);
        medicine_type.setDisable(true);

        hsnKeyReleaseEvent();
        initializePurchaseProperty();
    }

    public void initializePurchaseProperty()
    {
        purchase_item.setCellValueFactory(new PropertyValueFactory<Purchase, String>("purchaseItem"));
        purchase_batch.setCellValueFactory(new PropertyValueFactory<Purchase, String>("purchaseBatch"));
        purchase_quantity.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("purchaseQuantity"));
        purchase_cost.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("purchaseCost"));
        purchase_mrp.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("purchaseMrp"));
    }

    public void hsnKeyReleaseEvent()
    {
        hsn_code.setOnKeyReleased((KeyEvent keyEvent) -> {

            medicine_name.setText("");
            salt.setText("");
            company.setText("");
            String HsnCode = hsn_code.getText();
            int flag = 0;

            try {
                Connection dbConnection = JDBC.databaseConnect();
                Statement statement = dbConnection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT hsn_number FROM medicine");
                while(resultSet.next())
                {
                    if(HsnCode.equals(resultSet.getString("hsn_number")))
                    {
                        flag = 1;
                        break;
                    }
                    else flag = 0;
                }
                if(flag == 0)
                {
                    medicine_name.setDisable(false);
                    salt.setDisable(false);
                    company.setDisable(false);
                    medicine_type.setDisable(false);
                }
                else
                {
                    medicine_name.setEditable(false);
                    salt.setEditable(false);
                    company.setEditable(false);

                    PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT  name, salt, company, type FROM medicine WHERE hsn_number=?");
                    preparedStatement.setString(1,HsnCode);
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    if(resultSet1.next())
                    {
                        medicine_name.setText(resultSet1.getString("name"));
                        salt.setText(resultSet1.getString("salt"));
                        company.setText(resultSet1.getString("company"));
                        medicine_type.getSelectionModel().select(resultSet1.getString("type"));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public void onAddPurchase(ActionEvent actionEvent)
    {
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Resources/Layouts/alert_stage.fxml"));

        String regexDecimal = "[0-9]*\\.?[0-9]+";
        String regexQuantity = "^[0-9]*$";

        String Item = medicine_name.getText();
        String Batch = batch_number.getText();
        String Quantity = quantity.getText();
        String Cost = cost_price.getText();
        String Mrp = mrp.getText();

        if (Item.equals("") || Batch.equals("") || Quantity.equals("") || Cost.equals("") || Mrp.equals("")) {
            if (Item.equals(""))
                medicine_name.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Batch.equals(""))
                batch_number.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Quantity.equals(""))
                quantity.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Cost.equals(""))
                cost_price.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Mrp.equals(""))
                mrp.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else if (!Quantity.matches(regexQuantity)) {
            new AlertBox(currentStage, fxmlLoader, false, "Quantity must be a number !!");
            quantity.setStyle("-fx-text-inner-color: red;");
        }
        else if(!Cost.matches(regexDecimal)) {
            new AlertBox(currentStage, fxmlLoader, false, "Cost must be a number !!");
            cost_price.setStyle("-fx-text-inner-color: red;");
        }
        else if(!Mrp.matches(regexDecimal)) {
            new AlertBox(currentStage, fxmlLoader, false, "MRP must be a number !!");
            mrp.setStyle("-fx-text-inner-color: red;");
        }
        else
        {
            int intQuantity = Integer.parseInt(Quantity);
            float floatCost = Float.parseFloat(Cost);
            float floatMrp = Float.parseFloat(Mrp);

            table_data.add(new Purchase(Item, Batch, intQuantity, floatCost, floatMrp));

            medicine_name.setText("");
            batch_number.setText("");
            quantity.setText("");
            cost_price.setText("");
            mrp.setText("");
        }
    }
}
