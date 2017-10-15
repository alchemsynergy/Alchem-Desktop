package Main.Controllers;

import Main.JdbcConnection.JDBC;
import Main.ErrorAndInfo.AlertBox;
import Main.Helpers.Billing;

import java.lang.String;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.Iterator;

public class BillStageController {

    ObservableList<String> Mode = FXCollections.observableArrayList("Cash", "Debit Card", "Credit Card");
    ObservableList<String> whole_item_list = FXCollections.observableArrayList();
    ObservableList<String> batch_list = FXCollections.observableArrayList();
    ObservableList<Billing> bill_data = FXCollections.observableArrayList();

    @FXML
    private Label display_amount;

    @FXML
    private TextField patient_name, bill_no, company, quantity, free, item;

    @FXML
    private TextField discount;

    @FXML
    private ComboBox mode, batch;

    @FXML
    private DatePicker bill_date;

    @FXML
    private TableView<Billing> bill_table;

    @FXML
    private TableColumn<Billing,String> bill_item;

    @FXML
    private TableColumn<Billing,String> bill_batch;

    @FXML
    private TableColumn<Billing,Integer> bill_quantity;

    @FXML
    private TableColumn<Billing,String> bill_free;

    @FXML
    private TableColumn<Billing,Float> bill_rate;

    @FXML
    private TableColumn<Billing,Float> bill_amount;

    @FXML
    private Button delete;

    public void initialize()
    {
        bill_item.setCellValueFactory(new PropertyValueFactory<Billing, String>("billItem"));
        bill_batch.setCellValueFactory(new PropertyValueFactory<Billing, String>("billBatch"));
        bill_quantity.setCellValueFactory(new PropertyValueFactory<Billing, Integer>("billQuantity"));
        bill_free.setCellValueFactory(new PropertyValueFactory<Billing, String>("billFree"));
        bill_rate.setCellValueFactory(new PropertyValueFactory<Billing, Float>("billRate"));
        bill_amount.setCellValueFactory(new PropertyValueFactory<Billing, Float>("billAmount"));
        bill_table.setItems(bill_data);

        batch.setDisable(true);
        delete.setDisable(true);
        discount.setDisable(true);

        mode.setItems(Mode);
        mode.getSelectionModel().selectFirst();

        tableSelectionEvent();
        tableKeyPressEvent();
        initializeItemsList();
        itemAutoCompleteBinding();
        batchKeyReleaseEvent();
        quantityKeyPressEvent();
        freeKeyPressEvent();

    }
    // End of Initialize

    public void tableSelectionEvent()
    {
        bill_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if(bill_table.getSelectionModel().getSelectedItem() != null)
                {
                    delete.setDisable(false);
                }
                else delete.setDisable(true);
            }
        });
    }

    public void tableKeyPressEvent()
    {
        bill_table.setOnKeyPressed((KeyEvent keyEvent) -> {
            if(bill_table.getSelectionModel().getSelectedItem() != null)
            {
                if(keyEvent.getCode() == KeyCode.DELETE)
                    onDelete(null);
            }
        });
    }

    public void initializeItemsList()
    {
        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT name FROM medicine");
            while (resultSet.next())
            {
                whole_item_list.add(resultSet.getString("name"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemAutoCompleteBinding()
    {
        TextFields.bindAutoCompletion(item, whole_item_list).setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> autoCompletionEvent) -> {
            batch_list.clear();
            String selectedItem = item.getText();
            batch.setItems(batch_list);
            try {
                Connection dbConnection = JDBC.databaseConnect();
                PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT medicine_info.batch_number FROM medicine JOIN medicine_info ON medicine.medicine_id=medicine_info.medicine_id WHERE medicine.name=?");
                preparedStatement.setString(1, selectedItem);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next())
                {
                    batch_list.add(resultSet.getString("batch_number"));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            batch.setDisable(false);
        });
    }

    public void batchKeyReleaseEvent()
    {
        batch.getEditor().setOnKeyReleased((KeyEvent keyEvent) -> {
            if(keyEvent.getCode().isLetterKey() || keyEvent.getCode().isDigitKey() || keyEvent.getCode()==KeyCode.BACK_SPACE)
            {
                String Batch = batch.getEditor().getText();

                ObservableList<String> sorted_batch_list = FXCollections.observableArrayList();
                batch.setItems(sorted_batch_list);

                Iterator it = batch_list.iterator();
                while (it.hasNext())
                {
                    String batch_item = it.next().toString();
                    if(batch_item.contains(Batch))
                    {
                        sorted_batch_list.add(batch_item);
                    }
                }

                if(Batch.equals(""))
                {
                    batch.hide();
                    batch.setItems(batch_list);
                }
                else if(sorted_batch_list.size() == 0)
                {
                    batch.hide();
                }
                else
                    batch.show();
            }
        });
    }

    public void quantityKeyPressEvent()
    {
        quantity.setOnKeyPressed((KeyEvent keyEvent) -> {
            Node source = (Node) keyEvent.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../Layouts/alert_stage.fxml"));

            if(keyEvent.getCode() == KeyCode.ENTER)
                addEntry(currentStage, fxmlLoader);
        });
    }

    public void freeKeyPressEvent()
    {
        free.setOnKeyPressed((KeyEvent keyEvent) -> {
            Node source = (Node) keyEvent.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../Layouts/alert_stage.fxml"));

            if(keyEvent.getCode() == KeyCode.ENTER)
                addEntry(currentStage, fxmlLoader);
        });
    }

    public void onAddBill(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../Layouts/alert_stage.fxml"));

        addEntry(currentStage, fxmlLoader);
    }

    public void addEntry(Stage currentStage, FXMLLoader fxmlLoader)
    {
        String regexQuantity = "^[0-9]*$";
        String Item = item.getText();
        String Batch = batch.getEditor().getText();
        String Quantity = quantity.getText();
        String Free = free.getText();
        float Rate = getRate(Item, Batch);

        item.setStyle(null);
        batch.setStyle(null);
        quantity.setStyle(null);
        free.setStyle(null);

        display_amount.setText("");

        if(Item.equals("") || Batch.equals("") || Quantity.equals(""))
        {
            if(Item.equals(""))
                item.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if(Batch.equals(""))
                batch.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if(Quantity.equals(""))
                quantity.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        }
        else if (!Quantity.matches(regexQuantity))
        {
            new AlertBox(currentStage,fxmlLoader,"Quantity must be a number !!");
            quantity.setStyle("-fx-text-inner-color: red;");
        }
        else
        {
            float amount = Rate * Integer.parseInt(quantity.getText());
            bill_data.add(new Billing(Item, Batch, Integer.parseInt(quantity.getText()), Free ,Rate, amount));

            discount.setDisable(false);
            batch.setDisable(true);
            item.setText("");
            batch.getEditor().setText("");
            quantity.setText("");
            free.setText("");
        }
    }

    public float getRate(String Item, String Batch)
    {
        float rate = 0;
        try {
            Connection dbConnection = JDBC.databaseConnect();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT medicine_info.mrp FROM medicine JOIN medicine_info ON medicine.medicine_id=medicine_info.medicine_id WHERE medicine.name=? AND medicine_info.batch_number=?");
            preparedStatement.setString(1, Item);
            preparedStatement.setString(2, Batch);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                rate = Float.parseFloat(resultSet.getString("mrp"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }

    public void onCalculateAmount(ActionEvent actionEvent)
    {
        discount.setStyle(null);

        float temp_amount = 0;
        String Discount = discount.getText();
        String regexDiscount = "[0-9]*\\.?[0-9]+";

        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../Layouts/alert_stage.fxml"));

        Iterator<Billing> it = bill_data.iterator();
        if(it.hasNext())
        {
            while (it.hasNext())
            {
                temp_amount += it.next().getBillAmount();
            }

            if(Discount.matches(regexDiscount) || Discount.equals(""))
            {
                if(Discount.equals(""))
                {
                    display_amount.setText(String.valueOf(temp_amount));
                }
                else
                {
                    float temp_discount = Float.parseFloat(Discount);

                    temp_amount = temp_amount - (temp_amount * temp_discount / 100);
                    display_amount.setText(String.valueOf(temp_amount));
                }

            }
            else
            {
                new AlertBox(currentStage,fxmlLoader,"Discount must be a number !");
                discount.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                display_amount.setText("");
            }

        }
        else
        {
            new AlertBox(currentStage,fxmlLoader,"Items list empty !");
        }
    }

    public void onDelete(ActionEvent actionEvent)
    {
        int selectedIndex = bill_table.getSelectionModel().getSelectedIndex();
        bill_table.getItems().remove(selectedIndex);
        display_amount.setText("");

        if(bill_table.getItems().size()==0)
            discount.setDisable(true);
    }

}
