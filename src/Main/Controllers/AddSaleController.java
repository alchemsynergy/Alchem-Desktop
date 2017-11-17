package Main.Controllers;

import Main.Controllers.Retailers.ProfitLossController;
import Main.Controllers.Retailers.ViewSaleController;
import Main.ErrorAndInfo.AlertBox;
import Main.Helpers.Billing;
import Main.Helpers.Billing_history;
import Main.Helpers.Medicine;
import Main.Helpers.Retailers.ProfitLoss;
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

public class AddSaleController {

    private static double billDrawableWidth;
    ObservableList<String> Mode = FXCollections.observableArrayList("Cash", "Debit Card", "Credit Card");
    ObservableList<String> whole_item_list = FXCollections.observableArrayList();
    ObservableList<String> batch_list = FXCollections.observableArrayList();
    ObservableList<Billing> bill_data = FXCollections.observableArrayList();
    ObservableList<Billing_history> search_bill_table_list = FXCollections.observableArrayList();
    MainFeaturesTabSceneController mainFeaturesTabSceneController;
    @FXML
    private ScrollPane add_sale_parent_pane;
    @FXML
    private Label display_amount, bill_no;
    @FXML
    private TextField patient_name, company, doctor, quantity, free, item, discount, search_bill;
    @FXML
    private ComboBox mode, batch;
    @FXML
    private DatePicker bill_date;
    @FXML
    private TableView<Billing> bill_table;
    @FXML
    private TableColumn<Billing, String> bill_item;
    @FXML
    private TableColumn<Billing, String> bill_batch;
    @FXML
    private TableColumn<Billing, Integer> bill_quantity;
    @FXML
    private TableColumn<Billing, String> bill_free;
    @FXML
    private TableColumn<Billing, Float> bill_rate;
    @FXML
    private TableColumn<Billing, Float> bill_amount;
    @FXML
    private TableView<Billing_history> search_bill_table;
    @FXML
    private TableColumn<Billing_history, Long> search_bill_no;
    @FXML
    private TableColumn<Billing_history, String> search_date;
    @FXML
    private TableColumn<Billing_history, Float> search_amount;
    @FXML
    private Button delete, save_bill;

    public static void setBillDrawableWidth(double width) {
        billDrawableWidth = width;
    }

    public void initialize() {
        add_sale_parent_pane.setPrefWidth(billDrawableWidth);
        bill_table.setItems(bill_data);
        initializeBillingProperty();
        setSearchBillDataList();
        search_bill_table.setItems(search_bill_table_list);
        initializeBillingHistoryProperty();

        bill_date.setEditable(false);
        bill_date.setValue(LocalDate.now());
        batch.setDisable(true);
        delete.setDisable(true);
        discount.setDisable(true);
        save_bill.setDisable(true);

        mode.setItems(Mode);
        mode.getSelectionModel().selectFirst();

        tableSelectionEvent();
        searchTableClickEvent();
        tableKeyPressEvent();
        initializeItemsList();
        itemAutoCompleteBinding();
        batchKeyReleaseEvent();
        quantityKeyPressEvent();
        freeKeyPressEvent();
        initializeBillNo();
        searchKeyReleaseEvent();

    }

    // End of Initialize

    public void setSearchBillDataList() {
        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement sqlStatement = dbConnection.createStatement();
            ResultSet searchTableResultSet = sqlStatement.executeQuery("SELECT bill_no,date,total_amount FROM retailer_sale_bill where user_access_id='" + UserInfo.accessId + "'");
            while (searchTableResultSet.next()) {
                search_bill_table_list.add(new Billing_history(searchTableResultSet.getLong("bill_no"), searchTableResultSet.getString("date"), searchTableResultSet.getFloat("total_amount")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeBillingProperty() {
        bill_item.setCellValueFactory(new PropertyValueFactory<Billing, String>("billItem"));
        bill_batch.setCellValueFactory(new PropertyValueFactory<Billing, String>("billBatch"));
        bill_quantity.setCellValueFactory(new PropertyValueFactory<Billing, Integer>("billQuantity"));
        bill_free.setCellValueFactory(new PropertyValueFactory<Billing, String>("billFree"));
        bill_rate.setCellValueFactory(new PropertyValueFactory<Billing, Float>("billRate"));
        bill_amount.setCellValueFactory(new PropertyValueFactory<Billing, Float>("billAmount"));
    }

    public void initializeBillingHistoryProperty() {
        search_bill_no.setCellValueFactory(new PropertyValueFactory<Billing_history, Long>("searchBillNo"));
        search_date.setCellValueFactory(new PropertyValueFactory<Billing_history, String>("searchDate"));
        search_amount.setCellValueFactory(new PropertyValueFactory<Billing_history, Float>("searchAmount"));
    }

    public void tableSelectionEvent() {
        bill_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (bill_table.getSelectionModel().getSelectedItem() != null) {
                    delete.setDisable(false);
                } else delete.setDisable(true);
            }
        });
    }

    public void tableKeyPressEvent() {
        bill_table.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (bill_table.getSelectionModel().getSelectedItem() != null) {
                if (keyEvent.getCode() == KeyCode.DELETE)
                    onDelete(null);
            }
        });
    }

    public void initializeItemsList() {
        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement stmt = dbConnection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT name FROM medicine");
            while (resultSet.next()) {
                whole_item_list.add(resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemAutoCompleteBinding() {
        TextFields.bindAutoCompletion(item, whole_item_list).setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<String> autoCompletionEvent) -> {
            batch_list.clear();
            String selectedItem = item.getText();
            batch.setItems(batch_list);
            try {
                Connection dbConnection = JDBC.databaseConnect();
                PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT medicine_info.batch_number FROM medicine JOIN medicine_info ON medicine.medicine_id=medicine_info.medicine_id WHERE medicine.name=?");
                preparedStatement.setString(1, selectedItem);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    batch_list.add(resultSet.getString("batch_number"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            batch.setDisable(false);
        });
    }

    public void batchKeyReleaseEvent() {
        batch.getEditor().setOnKeyReleased((KeyEvent keyEvent) -> {
            if (keyEvent.getCode().isLetterKey() || keyEvent.getCode().isDigitKey() || keyEvent.getCode() == KeyCode.BACK_SPACE) {
                String Batch = batch.getEditor().getText();

                ObservableList<String> sorted_batch_list = FXCollections.observableArrayList();
                batch.setItems(sorted_batch_list);

                Iterator it = batch_list.iterator();
                while (it.hasNext()) {
                    String batch_item = it.next().toString();
                    if (batch_item.contains(Batch)) {
                        sorted_batch_list.add(batch_item);
                    }
                }

                if (Batch.equals("")) {
                    batch.hide();
                    batch.setItems(batch_list);
                } else if (sorted_batch_list.size() == 0) {
                    batch.hide();
                } else
                    batch.show();
            }
        });
    }

    public void quantityKeyPressEvent() {
        quantity.setOnKeyPressed((KeyEvent keyEvent) -> {
            Node source = (Node) keyEvent.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Resources/Layouts/alert_stage.fxml"));

            if (keyEvent.getCode() == KeyCode.ENTER)
                addEntry(currentStage, fxmlLoader);
        });
    }

    public void freeKeyPressEvent() {
        free.setOnKeyPressed((KeyEvent keyEvent) -> {
            Node source = (Node) keyEvent.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Resources/Layouts/alert_stage.fxml"));

            if (keyEvent.getCode() == KeyCode.ENTER)
                addEntry(currentStage, fxmlLoader);
        });
    }

    public void onAddBill(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Resources/Layouts/alert_stage.fxml"));

        addEntry(currentStage, fxmlLoader);
    }

    public void addEntry(Stage currentStage, FXMLLoader fxmlLoader) {
        String regexQuantity = "^[0-9]*$";
        String Item = item.getText();
        String Batch = batch.getEditor().getText();
        String Quantity = quantity.getText();
        String Free = free.getText();
        float Rate = getRate(Item, Batch);
        int piece = 0;

        item.setStyle(null);
        batch.setStyle(null);
        quantity.setStyle(null);
        free.setStyle(null);

        display_amount.setText("");

        //  Getting Max Quantity from database

        try {
            Connection dbConnection = JDBC.databaseConnect();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT quantity.piece FROM medicine JOIN medicine_info ON medicine.medicine_id=medicine_info.medicine_id JOIN quantity ON medicine_info.medicine_info_id=quantity.medicine_info_id WHERE medicine.name=? AND medicine_info.batch_number=?");
            preparedStatement.setString(1, Item);
            preparedStatement.setString(2, Batch);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                piece = rs.getInt("piece");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Item.equals("") || Batch.equals("") || Quantity.equals("")) {
            if (Item.equals(""))
                item.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Batch.equals(""))
                batch.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Quantity.equals(""))
                quantity.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        } else if (!Quantity.matches(regexQuantity)) {
            new AlertBox(currentStage, fxmlLoader, false, "Quantity must be a number !!");
            quantity.setStyle("-fx-text-inner-color: red;");
        } else if (Integer.parseInt(Quantity) > piece) {
            new AlertBox(currentStage, fxmlLoader, false, "Quantity greater than :" + piece);
            quantity.setStyle("-fx-text-inner-color: red;");
        } else {
            int intQuantity = Integer.parseInt(Quantity);
            float amount = Rate * intQuantity;
            bill_data.add(new Billing(Item, Batch, intQuantity, Free, Rate, amount));

            discount.setDisable(false);
            batch.setDisable(true);
            item.setText("");
            batch.getEditor().setText("");
            quantity.setText("");
            free.setText("");

        }
    }


    public float getRate(String Item, String Batch) {
        float rate = 0;
        try {
            Connection dbConnection = JDBC.databaseConnect();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT medicine_info.mrp FROM medicine JOIN medicine_info ON medicine.medicine_id=medicine_info.medicine_id WHERE medicine.name=? AND medicine_info.batch_number=?");
            preparedStatement.setString(1, Item);
            preparedStatement.setString(2, Batch);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                rate = Float.parseFloat(resultSet.getString("mrp"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }

    public void onCalculateAmount(ActionEvent actionEvent) {
        discount.setStyle(null);

        float temp_amount = 0;
        String Discount = discount.getText();
        String regexDiscount = "[0-9]*\\.?[0-9]+";

        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Resources/Layouts/alert_stage.fxml"));

        Iterator<Billing> it = bill_data.iterator();
        if (it.hasNext()) {
            while (it.hasNext()) {
                temp_amount += it.next().getBillAmount();
            }

            if (Discount.matches(regexDiscount) || Discount.equals("")) {
                if (Discount.equals("")) {
                    display_amount.setText(String.valueOf(temp_amount));
                } else {
                    float temp_discount = Float.parseFloat(Discount);

                    temp_amount = temp_amount - (temp_amount * temp_discount / 100);
                    display_amount.setText(String.valueOf(temp_amount));
                }
                save_bill.setDisable(false);
            } else {
                new AlertBox(currentStage, fxmlLoader, false, "Discount must be a number !");
                discount.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                display_amount.setText("");
            }

        } else {
            new AlertBox(currentStage, fxmlLoader, false, "Items list empty !");
        }
    }

    public void onDelete(ActionEvent actionEvent) {
        int selectedIndex = bill_table.getSelectionModel().getSelectedIndex();
        String getItem = bill_table.getSelectionModel().getSelectedItem().getBillItem();
        String getBatch = bill_table.getSelectionModel().getSelectedItem().getBillBatch();
        int getQuantity = bill_table.getSelectionModel().getSelectedItem().getBillQuantity();
        long billNo = Long.parseLong(bill_no.getText());
        int piece = 0;
        int pp_item = 0;
        int items = 0;
        int ip_unit = 0;
        int unit = 0;
        int medicine_info_id = 0;

        // Code to accordingly update all the quantities after the sale

        //  Getting Max Quantities from database
        try {
            Connection dbConnection = JDBC.databaseConnect();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT quantity.piece, quantity.pp_item, quantity.items, quantity.ip_unit, quantity.unit FROM medicine JOIN medicine_info ON medicine.medicine_id=medicine_info.medicine_id JOIN quantity ON medicine_info.medicine_info_id=quantity.medicine_info_id WHERE medicine.name=? AND medicine_info.batch_number=?");
            preparedStatement.setString(1, getItem);
            preparedStatement.setString(2, getBatch);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                piece = rs.getInt("piece");
                pp_item = rs.getInt("pp_item");
                items = rs.getInt("items");
                ip_unit = rs.getInt("ip_unit");
                unit = rs.getInt("unit");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // All quantities fetched from database


        // Check whether the entry already exists in the database
        try {
            Connection dbConnection = JDBC.databaseConnect();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT retailer_sale_bill_info.rs_bill_info_id FROM retailer_sale_bill JOIN retailer_sale_bill_info ON retailer_sale_bill.rs_bill_id = retailer_sale_bill_info.rs_bill_id WHERE retailer_sale_bill.user_access_id=? AND retailer_sale_bill.bill_no=? AND retailer_sale_bill_info.item=? AND retailer_sale_bill_info.batch_number=?");
            preparedStatement.setInt(1, LoginController.userAccessId);
            preparedStatement.setLong(2, billNo);
            preparedStatement.setString(3, getItem);
            preparedStatement.setString(4, getBatch);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // calculating number of piece left after sale
                piece = piece + getQuantity;

                // calculating number of items left after sale
                if (piece % pp_item == 0)
                    items = piece / pp_item;
                else
                    items = (piece / pp_item) + 1;

                // calculating number of units left after sale
                if (items % ip_unit == 0)
                    unit = items / ip_unit;
                else
                    unit = (items / ip_unit) + 1;

                // Deleting selected item from database

                int rs_bill_info_id = resultSet.getInt("rs_bill_info_id");
                preparedStatement = dbConnection.prepareStatement("DELETE FROM retailer_sale_bill_info WHERE rs_bill_info_id=?");
                preparedStatement.setInt(1, rs_bill_info_id);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Connection dbConnection = JDBC.databaseConnect();

            PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT medicine_info.medicine_info_id FROM medicine JOIN  medicine_info ON medicine.medicine_id=medicine_info.medicine_id WHERE medicine.name=? AND medicine_info.batch_number=?");
            preparedStatement.setString(1, getItem);
            preparedStatement.setString(2, getBatch);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                medicine_info_id = resultSet.getInt("medicine_info_id");
            }

            preparedStatement = dbConnection.prepareStatement("UPDATE quantity SET piece=?, items=?, unit=? WHERE medicine_info_id=?");
            preparedStatement.setInt(1, piece);
            preparedStatement.setInt(2, items);
            preparedStatement.setInt(3, unit);
            preparedStatement.setInt(4, medicine_info_id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Now removing selected item from table

        bill_table.getItems().remove(selectedIndex);
        display_amount.setText("");

        if (bill_table.getItems().size() == 0) {
            discount.setDisable(true);
            save_bill.setDisable(true);
        }
    }

    public void onSaveBill(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Resources/Layouts/alert_stage.fxml"));

        String patientName = patient_name.getText();
        String Company = company.getText();
        String Doctor = doctor.getText();
        String Mode = mode.getSelectionModel().getSelectedItem().toString();
        String Discount = discount.getText();
        float floatDiscount;
        long billNo = Long.parseLong(bill_no.getText());
        long rsBillId = 0;
        int flag = 0;
        String Free = "";
        int piece = 0;
        int pp_item = 0;
        int items = 0;
        int ip_unit = 0;
        int unit = 0;
        int medicine_info_id = 0;

        if (Discount.equals("")) {
            floatDiscount = 0;
        } else floatDiscount = Float.parseFloat(discount.getText());

        if (patientName.equals("") || Doctor.equals("") || bill_date.getValue() == null || Company.equals("") || display_amount.getText().equals("")) {
            if (patientName.equals(""))
                patient_name.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Doctor.equals(""))
                doctor.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (bill_date.getValue() == null)
                bill_date.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (Company.equals(""))
                company.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            if (display_amount.getText().equals(""))
                new AlertBox(currentStage, fxmlLoader, false, "Please Enter Amount !!");
        } else {
            String billDate = bill_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Float Amount = Float.parseFloat(display_amount.getText());
            ObservableList<Billing> previousItemList=FXCollections.observableArrayList();
           try {
                Connection dbconnect=JDBC.databaseConnect();
                Statement sqlStatement=dbconnect.createStatement();
                ResultSet inventory=sqlStatement.executeQuery("select item,batch_number,quantity from retailer_sale_bill_info JOIN retailer_sale_bill ON retailer_sale_bill_info.rs_bill_id=retailer_sale_bill.rs_bill_id where retailer_sale_bill.bill_no='"+bill_no.getText()+"' AND  retailer_sale_bill.user_access_id='"+UserInfo.accessId+"'");
                while (inventory.next()) {
                    previousItemList.add(new Billing(inventory.getString("item"),inventory.getString("batch_number"),inventory.getInt("quantity"),false));
                }
            }catch(Exception e){}
            // Code to accordingly update all the quantities after the sale

            Iterator<Billing> it = bill_data.iterator();
            while (it.hasNext()) {
                Billing temp = it.next();

                //  Getting Max Quantities from database
                try {
                    Connection dbConnection = JDBC.databaseConnect();
                    PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT quantity.piece, quantity.pp_item, quantity.items, quantity.ip_unit, quantity.unit FROM medicine JOIN medicine_info ON medicine.medicine_id=medicine_info.medicine_id JOIN quantity ON medicine_info.medicine_info_id=quantity.medicine_info_id WHERE medicine.name=? AND medicine_info.batch_number=?");
                    preparedStatement.setString(1, temp.getBillItem());
                    preparedStatement.setString(2, temp.getBillBatch());
                    ResultSet rs = preparedStatement.executeQuery();
                    if (rs.next()) {
                        piece = rs.getInt("piece");
                        pp_item = rs.getInt("pp_item");
                        items = rs.getInt("items");
                        ip_unit = rs.getInt("ip_unit");
                        unit = rs.getInt("unit");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // All quantities fetched from database

                // Check whether the entry already exists in the database
                try {
                    Connection dbConnection = JDBC.databaseConnect();
                    PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT retailer_sale_bill_info.rs_bill_info_id FROM retailer_sale_bill JOIN retailer_sale_bill_info ON retailer_sale_bill.rs_bill_id = retailer_sale_bill_info.rs_bill_id WHERE retailer_sale_bill.user_access_id=? AND retailer_sale_bill.bill_no=? AND retailer_sale_bill_info.item=? AND retailer_sale_bill_info.batch_number=?");
                    preparedStatement.setInt(1, LoginController.userAccessId);
                    preparedStatement.setLong(2, billNo);
                    preparedStatement.setString(3, temp.getBillItem());
                    preparedStatement.setString(4, temp.getBillBatch());
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next() == false) {
                        // calculating number of piece left after sale
                        piece = piece - temp.getBillQuantity();

                        // calculating number of items left after sale
                        if (piece % pp_item == 0)
                            items = piece / pp_item;
                        else
                            items = (piece / pp_item) + 1;

                        // calculating number of units left after sale
                        if (items % ip_unit == 0)
                            unit = items / ip_unit;
                        else
                            unit = (items / ip_unit) + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    Connection dbConnection = JDBC.databaseConnect();

                    PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT medicine_info.medicine_info_id FROM medicine JOIN  medicine_info ON medicine.medicine_id=medicine_info.medicine_id WHERE medicine.name=? AND medicine_info.batch_number=?");
                    preparedStatement.setString(1, temp.getBillItem());
                    preparedStatement.setString(2, temp.getBillBatch());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        medicine_info_id = resultSet.getInt("medicine_info_id");
                    }

                    preparedStatement = dbConnection.prepareStatement("UPDATE quantity SET piece=?, items=?, unit=? WHERE medicine_info_id=?");
                    preparedStatement.setInt(1, piece);
                    preparedStatement.setInt(2, items);
                    preparedStatement.setInt(3, unit);
                    preparedStatement.setInt(4, medicine_info_id);
                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Code for saving or updating the data into the database

            try {
                Connection dbConnection = JDBC.databaseConnect();

                PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT bill_no FROM retailer_sale_bill WHERE user_access_id=?");
                preparedStatement.setInt(1, LoginController.userAccessId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    if (billNo == resultSet.getLong("bill_no")) {
                        flag = 1;
                        break;
                    } else flag = 0;
                }

                if (flag == 0) {
                    preparedStatement = dbConnection.prepareStatement("INSERT INTO retailer_sale_bill VALUES (?,?,?,?,?,?,?,?,?,DEFAULT) ");
                    preparedStatement.setInt(1, LoginController.userAccessId);
                    preparedStatement.setLong(2, billNo);
                    preparedStatement.setString(3, patientName);
                    preparedStatement.setString(4, billDate);
                    preparedStatement.setString(5, Company);
                    preparedStatement.setString(6, Doctor);
                    preparedStatement.setString(7, Mode);
                    preparedStatement.setFloat(8, floatDiscount);
                    preparedStatement.setFloat(9, Amount);
                    preparedStatement.executeUpdate();

                    Statement statement = dbConnection.createStatement();
                    ResultSet resultSet1 = statement.executeQuery("SELECT MAX(rs_bill_id) FROM retailer_sale_bill");
                    if (resultSet1.next()) {
                        rsBillId = resultSet1.getLong(1);
                    }

                    preparedStatement = dbConnection.prepareStatement("INSERT INTO retailer_sale_bill_info VALUES (?,?,?,?,?,?,DEFAULT )");

                    Iterator<Billing> it1 = bill_data.iterator();
                    while (it1.hasNext()) {
                        Billing temp = it1.next();
                        if (temp.getBillFree().equals(""))
                            Free = "NA";
                        else
                            Free = temp.getBillFree();
                        preparedStatement.setLong(1, rsBillId);
                        preparedStatement.setString(2, temp.getBillItem());
                        preparedStatement.setString(3, temp.getBillBatch());
                        preparedStatement.setInt(4, temp.getBillQuantity());
                        preparedStatement.setString(5, Free);
                        preparedStatement.setFloat(6, temp.getBillRate());
                        preparedStatement.executeUpdate();

                        Iterator<Medicine> medicineIterator=InventoryController.medicines.iterator();
                        while(medicineIterator.hasNext())
                        {
                            Medicine medicine=medicineIterator.next();
                            if(temp.getBillItem().equals(medicine.getName()) && temp.getBillBatch().equals(medicine.getBatch()))
                            {
                                medicine.setQuantity(medicine.getQuantity()-temp.getBillQuantity());
                                if(medicine.getQuantity()==0)
                                {
                                    medicineIterator.remove();
                                }
                                break;
                            }
                        }
                    }
                    ViewSaleController.saleList.add(new Sale(billDate, billNo, patientName, Doctor, Company, Mode, Amount));
                    ObservableList<Medicine> temp_medicine=FXCollections.observableArrayList(InventoryController.medicines);
                    InventoryController.medicines.clear();
                    InventoryController.medicines.addAll(temp_medicine);
                    for (int i = 0; i < 7; i++) {
                        int chk = viewSaleEvent(i, billDate, Amount);
                        if (chk == 1)
                            break;
                    }

                    String date="";
                    try {
                        Connection conn=JDBC.databaseConnect();
                        Statement sqlStatement=conn.createStatement();
                        ResultSet saleProfitResultSet=sqlStatement.executeQuery("SELECT date from retailer_sale_bill where bill_no='"+billNo+"' AND user_access_id='"+UserInfo.accessId+"'");
                        while(saleProfitResultSet.next())
                        {
                            date=saleProfitResultSet.getString(1);
                        }
                    }catch (Exception e)
                    {e.printStackTrace();}

                    Iterator<ProfitLoss> plIterator = ProfitLossController.profitList.iterator();
                    while (plIterator.hasNext()) {
                        ProfitLoss profitLoss=plIterator.next();
                        if(date.equals(profitLoss.getDate()))
                        {
                            profitLoss.setTotalSale(Amount+profitLoss.getTotalSale());
                            profitLoss.setProfit(profitLoss.getProfit()+Amount);
                            for (int i = 0; i < 7; i++) {
                                int chk = viewSaleProfitEvent(i, date,Amount);
                                if (chk == 1)
                                    break;
                            }
                        }
                    }
                    ObservableList<ProfitLoss> temporary_profit_data = FXCollections.observableArrayList(ProfitLossController.profitList);
                    ProfitLossController.profitList.clear();
                    ProfitLossController.profitList.addAll(temporary_profit_data);

                    search_bill_table_list.add(new Billing_history(billNo, billDate, Amount));
                }
                else {
                    preparedStatement = dbConnection.prepareStatement("UPDATE retailer_sale_bill SET patient_name=?, date=?, company=?, doctor_name=?, mode=?, discount=?, total_amount=? WHERE user_access_id=? AND bill_no=?");
                    preparedStatement.setString(1, patientName);
                    preparedStatement.setString(2, billDate);
                    preparedStatement.setString(3, Company);
                    preparedStatement.setString(4, Doctor);
                    preparedStatement.setString(5, Mode);
                    preparedStatement.setFloat(6, floatDiscount);
                    preparedStatement.setFloat(7, Amount);
                    preparedStatement.setInt(8, UserInfo.accessId);
                    preparedStatement.setLong(9, billNo);
                    preparedStatement.executeUpdate();

                    preparedStatement = dbConnection.prepareStatement("SELECT rs_bill_id FROM retailer_sale_bill WHERE user_access_id=? AND bill_no=?");
                    preparedStatement.setInt(1, UserInfo.accessId);
                    preparedStatement.setLong(2, billNo);
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    if (resultSet1.next()) {
                        rsBillId = resultSet1.getLong(1);
                    }

                    preparedStatement = dbConnection.prepareStatement("DELETE FROM retailer_sale_bill_info WHERE rs_bill_id=?");
                    preparedStatement.setLong(1, rsBillId);
                    preparedStatement.executeUpdate();

                    preparedStatement = dbConnection.prepareStatement("INSERT INTO retailer_sale_bill_info VALUES (?,?,?,?,?,?,DEFAULT )");

                    Iterator<Billing> previousIterator=previousItemList.iterator();
                    while(previousIterator.hasNext())
                    {
                        Billing previousItem=previousIterator.next();
                        Iterator<Medicine> medicineIterator=InventoryController.medicines.iterator();
                        while(medicineIterator.hasNext())
                        {
                            Medicine medicine=medicineIterator.next();
                            if(medicine.getName().equals(previousItem.getBillItem()) && medicine.getBatch().equals(previousItem.getBillBatch()))
                            {
                                System.out.println(medicine.getBatch());
                                medicine.setQuantity(medicine.getQuantity()+previousItem.getBillQuantity());
                                break;
                            }
                        }
                    }

                    Iterator<Billing> it1 = bill_data.iterator();
                    while (it1.hasNext()) {
                        Billing temp = it1.next();
                        if (temp.getBillFree().equals(""))
                            Free = "NA";
                        else
                            Free = temp.getBillFree();
                        preparedStatement.setLong(1, rsBillId);
                        preparedStatement.setString(2, temp.getBillItem());
                        preparedStatement.setString(3, temp.getBillBatch());
                        preparedStatement.setInt(4, temp.getBillQuantity());
                        preparedStatement.setString(5, Free);
                        preparedStatement.setFloat(6, temp.getBillRate());
                        preparedStatement.executeUpdate();

                        Iterator<Medicine> medicineIterator=InventoryController.medicines.iterator();
                        while(medicineIterator.hasNext())
                        {
                            Medicine medicine=medicineIterator.next();
                            if(temp.getBillItem().equals(medicine.getName()) && temp.getBillBatch().equals(medicine.getBatch()))
                            {
                                medicine.setQuantity(medicine.getQuantity()-temp.getBillQuantity());
                                if(medicine.getQuantity()==0)
                                {
                                    medicineIterator.remove();
                                }
                                break;
                            }
                        }

                    }

                    ObservableList<Medicine> temp_medicine=FXCollections.observableArrayList(InventoryController.medicines);
                    InventoryController.medicines.clear();
                    InventoryController.medicines.addAll(temp_medicine);

                    float amt=0;
                    Iterator<Sale> saleIterator = ViewSaleController.saleList.iterator();
                    while (saleIterator.hasNext()) {
                        Sale sale = saleIterator.next();
                        String date;
                        if (sale.getBillNumber() == billNo) {
                            amt = Amount - sale.getAmount();
                            sale.setAmount(Amount);
                            date = sale.getDate();
                            for (int i = 0; i < 7; i++) {
                                int chk = viewSaleEvent(i, date, amt);
                                if (chk == 1)
                                    break;
                            }
                        }
                    }
                    ObservableList<Sale> temporary_view_sale_data = FXCollections.observableArrayList(ViewSaleController.saleList);
                    ViewSaleController.saleList.clear();
                    ViewSaleController.saleList.addAll(temporary_view_sale_data);

                    String date="";
                    try {
                        Connection conn=JDBC.databaseConnect();
                        Statement sqlStatement=conn.createStatement();
                        ResultSet saleProfitResultSet=sqlStatement.executeQuery("SELECT date from retailer_sale_bill where bill_no='"+billNo+"' AND user_access_id='"+UserInfo.accessId+"'");
                        while(saleProfitResultSet.next())
                        {
                            date=saleProfitResultSet.getString(1);
                        }
                    }catch (Exception e)
                    {e.printStackTrace();}

                    Iterator<ProfitLoss> plIterator = ProfitLossController.profitList.iterator();
                    while (plIterator.hasNext()) {
                        ProfitLoss profitLoss=plIterator.next();
                      if(date.equals(profitLoss.getDate()))
                      {
                          profitLoss.setTotalSale(profitLoss.getTotalSale()+amt);
                          profitLoss.setProfit(profitLoss.getProfit()+amt);
                          for (int i = 0; i < 7; i++) {
                              int chk = viewSaleProfitEvent(i, date, amt);
                              if (chk == 1)
                                  break;
                          }
                      }
                    }
                    ObservableList<ProfitLoss> temporary_profit_data = FXCollections.observableArrayList(ProfitLossController.profitList);
                    ProfitLossController.profitList.clear();
                    ProfitLossController.profitList.addAll(temporary_profit_data);

                    Iterator<Billing_history> billingHistoryIterator = search_bill_table_list.iterator();
                    while (billingHistoryIterator.hasNext()) {
                        Billing_history bill = billingHistoryIterator.next();
                        if (billNo == bill.getSearchBillNo()) {
                            bill.setSearchAmount(Amount);
                        }
                    }
                    ObservableList<Billing_history> temporary_add_sale_data = FXCollections.observableArrayList(search_bill_table_list);
                    search_bill_table_list.clear();
                    search_bill_table_list.addAll(temporary_add_sale_data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            new AlertBox(currentStage, fxmlLoader, true, "Saved Successfully !!");
            patient_name.setStyle(null);
            doctor.setStyle(null);
            bill_date.setStyle(null);
            company.setStyle(null);

        }
    }

    public int viewSaleEvent(int i, String billDate, Float Amount) {
        long date = ViewSaleController.day[i];
        long month = ViewSaleController.month[i];
        long year = ViewSaleController.year[i];
        String datechk = year + "-" + month + "-" + date;
        if (datechk.equals(billDate)) {
            ViewSaleController.sum[i] = ViewSaleController.sum[i] + Amount;
            try {
                mainFeaturesTabSceneController.setSaleLabel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
        return 0;
    }

    public int viewSaleProfitEvent(int i, String billDate, Float Amount) {
        long date = ProfitLossController.day[i];
        long month = ProfitLossController.month[i];
        long year = ProfitLossController.year[i];
        String datechk = year + "-" + month + "-" + date;
        if (datechk.equals(billDate)) {
            ProfitLossController.sum[i] = ProfitLossController.sum[i] + Amount;
            ProfitLossController.totalSale[i]=ProfitLossController.totalSale[i]+Amount;
            try {
                mainFeaturesTabSceneController.setProfitLabel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }
        return 0;
    }



    public void onNewBill(ActionEvent actionEvent) {
        save_bill.setDisable(true);
        initializeBillNo();
        patient_name.setText(null);
        company.setText(null);
        doctor.setText(null);
        display_amount.setText("");
        discount.setText("");
        bill_table.getItems().clear();
        patient_name.setStyle(null);
        doctor.setStyle(null);
        bill_date.setStyle(null);
        company.setStyle(null);
    }

    public void initializeBillNo() {
        long billNo = 0;
        try {
            Connection dbConnection = JDBC.databaseConnect();
            PreparedStatement pstmt = dbConnection.prepareStatement("SELECT MAX(bill_no) FROM retailer_sale_bill WHERE user_access_id=?");
            pstmt.setInt(1, LoginController.userAccessId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                billNo = resultSet.getLong(1) + 1;
            } else {
                billNo = 1;
            }
            bill_no.setText(String.valueOf(billNo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchKeyReleaseEvent() {
        search_bill.setOnKeyReleased((KeyEvent keyEvent) -> {
            long bill_no = 0;
            String date = "";
            float total_amount = 0;

            ObservableList<Billing_history> search_bill_data = FXCollections.observableArrayList();

            search_bill_table.setItems(search_bill_data);
            String search = search_bill.getText();

            if (search.equals("")) {
                search_bill_table.setItems(search_bill_table_list);
            } else {
                Iterator<Billing_history> iterator = search_bill_table_list.iterator();
                while (iterator.hasNext()) {
                    Billing_history temp = iterator.next();
                    bill_no = temp.getSearchBillNo();
                    if (String.valueOf(bill_no).contains(search)) {
                        date = temp.getSearchDate();
                        total_amount = temp.getSearchAmount();
                        search_bill_data.add(new Billing_history(bill_no, date, total_amount));
                    }

                }

            }

        });
    }

    public void searchTableClickEvent() {
        search_bill_table.setRowFactory(tv -> {
            TableRow<Billing_history> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    bill_data.clear();
                    Billing_history rowData = row.getItem();
                    long billNo = rowData.getSearchBillNo();
                    String Date = rowData.getSearchDate();
                    float totalAmount = rowData.getSearchAmount();

                    try {
                        Connection dbConnection = JDBC.databaseConnect();
                        PreparedStatement preparedStatement = dbConnection.prepareStatement("SELECT patient_name, mode, company, doctor_name, discount, rs_bill_id FROM retailer_sale_bill WHERE user_access_id=? AND bill_no=? AND date=? AND total_amount=?");
                        preparedStatement.setInt(1, LoginController.userAccessId);
                        preparedStatement.setLong(2, billNo);
                        preparedStatement.setString(3, Date);
                        preparedStatement.setFloat(4, totalAmount);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            String patientName = resultSet.getString("patient_name");
                            String Mode = resultSet.getString("mode");
                            String Company = resultSet.getString("company");
                            String Doctor = resultSet.getString("doctor_name");
                            float Discount = resultSet.getFloat("discount");
                            long rs_bill_id = resultSet.getLong("rs_bill_id");

                            patient_name.setText(patientName);
                            mode.getSelectionModel().select(Mode);
                            company.setText(Company);
                            doctor.setText(Doctor);
                            discount.setText(String.valueOf(Discount));
                            bill_date.setValue(LocalDate.parse(Date));
                            bill_no.setText(String.valueOf(billNo));
                            display_amount.setText(String.valueOf(totalAmount));

                            preparedStatement = dbConnection.prepareStatement("SELECT item, batch_number, quantity, free, rate FROM retailer_sale_bill_info WHERE rs_bill_id=?");
                            preparedStatement.setLong(1, rs_bill_id);
                            ResultSet resultSet1 = preparedStatement.executeQuery();
                            while (resultSet1.next()) {
                                String item = resultSet1.getString("item");
                                String batchNumber = resultSet1.getString("batch_number");
                                int quantity = resultSet1.getInt("quantity");
                                String free = resultSet1.getString("free");
                                float rate = resultSet1.getFloat("rate");
                                float amount = rate * quantity;
                                bill_data.add(new Billing(item, batchNumber, quantity, free, rate, amount));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    public void setSaleLabel() throws Exception {
        FXMLLoader viewSaleLoader = new FXMLLoader();
        viewSaleLoader.setLocation(getClass().getResource("../../Resources/Layouts/Retailers/view_sale.fxml"));
        viewSaleLoader.load();
        ViewSaleController viewSaleController = viewSaleLoader.getController();
        viewSaleController.setSaleLabel();
    }

    public void setSaleProfitLabel() throws Exception {
        FXMLLoader profitLossLoader = new FXMLLoader();
        profitLossLoader.setLocation(getClass().getResource("../../Resources/Layouts/Retailers/profit_loss.fxml"));
        profitLossLoader.load();
        ProfitLossController profitLossController = profitLossLoader.getController();
        profitLossController.setProfitLabel();
    }

    public void init(MainFeaturesTabSceneController mainFeaturesTabSceneController) {
        this.mainFeaturesTabSceneController = mainFeaturesTabSceneController;
    }
}
