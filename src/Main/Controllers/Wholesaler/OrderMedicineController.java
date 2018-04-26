package Main.Controllers.Wholesaler;


import Main.Helpers.Medicine;
import Main.Helpers.UserInfo;
import Main.JdbcConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Iterator;

public class OrderMedicineController {

    @FXML
    Label dateLabel,orderingToLabel,orderNumberLabel,errorLabel;

    @FXML
    VBox selectWholesalerVBox,orderToVBox,addMedicineVBox,errorLogVBox;

    @FXML
    ComboBox selectWholesalerComboBox;

    @FXML
    TableView orderTableView;

    @FXML
    TableColumn serialNumberColumn,medicineNameColumn,quantityColumn;

    @FXML
    TextField medicineName,quantity;


    static int serialNumber=1;

    ObservableList<Medicine> medicineList=FXCollections.observableArrayList();

    int orderNumber=0;

    public void initialize()
    {

       initializeDate();
       initializeComboBox();
       disableVBox();
       initializeTable();
       setOnKeyPressedListener();
    }

    public void setOnKeyPressedListener() {
        medicineName.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                onMedicineAdd();
            }
        });
        quantity.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                onMedicineAdd();
            }
        });
    }

    void initializeTable()
    {
        serialNumberColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("serialNumber"));
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<Medicine, String>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Medicine, Integer>("quantity"));
        orderTableView.setRowFactory( tv -> {
            TableRow<Medicine> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    int rowNumber= row.getIndex();
                    medicineList.remove(rowNumber);
                    ObservableList<Medicine> medicine=FXCollections.observableArrayList();
                    Iterator<Medicine> iterate=medicineList.iterator();
                    while(iterate.hasNext())
                    {
                        Medicine med=iterate.next();
                        if(med.getSerialNumber()>rowNumber)
                        {
                            med.setSerialNumber(med.getSerialNumber()-1);
                        }
                        medicine.add(med);
                    }
                    orderTableView.getItems().clear();
                    orderTableView.getItems().addAll(medicine);
                    serialNumber--;
                }
            });
            return row ;
        });
    }

    void initializeDate()
    {
        Calendar dateToday = Calendar.getInstance();
        String date="DATE : "+dateToday.get(Calendar.DAY_OF_MONTH)+"-"+(dateToday.get(Calendar.MONTH) + 1)+"-"+dateToday.get(Calendar.YEAR);
        dateLabel.setText(date);
    }
    void disableVBox()
    {
        orderToVBox.setVisible(false);
        addMedicineVBox.setVisible(false);
        orderNumberLabel.setVisible(false);
        errorLogVBox.setVisible(false);
    }

    void initializeComboBox()
    {
      Connection conn=null;
      try
      {
          conn= JDBC.databaseConnect();
          Statement sqlStatement=conn.createStatement();
          ResultSet wholesalerResultSet=sqlStatement.executeQuery("Select username,name from user_access,store_info where user_type_id=1 and user_access.user_access_id=store_info.user_access_id order by name");
          while(wholesalerResultSet.next())
              selectWholesalerComboBox.getItems().add(wholesalerResultSet.getString("name")+"("+wholesalerResultSet.getString("username")+")");
          selectWholesalerComboBox.setValue("Select From Dropdown");
          selectWholesalerComboBox.setButtonCell(new ListCell<String>() {
              @Override
              public void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  if (item != null) {
                      setText(item);
                      setAlignment(Pos.CENTER);
                      Insets old = getPadding();
                      setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
                  }
              }
          });
          selectWholesalerComboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
              @Override
              public ListCell<String> call(ListView<String> list) {
                  return new ListCell<String>() {
                      @Override
                      public void updateItem(String item, boolean empty) {
                          super.updateItem(item, empty);
                          if (item != null) {
                              setText(item);
                              setAlignment(Pos.CENTER);
                          }
                      }
                  };
              }
          });
          selectWholesalerComboBox.setVisibleRowCount(10);
      }catch (Exception e)
      {
          e.printStackTrace();
      }
    }
    public void wholesalerSelected()
    {
        String wholesaler=(String)selectWholesalerComboBox.getSelectionModel().getSelectedItem();
        if(selectWholesalerComboBox.getSelectionModel().getSelectedItem()!=null)
            wholesaler=wholesaler.toUpperCase();
        selectWholesalerVBox.setVisible(false);
        orderingToLabel.setText("ORDERING TO "+wholesaler);
        try
        {
            Connection dbConnection=JDBC.databaseConnect();
            Statement sqlStatement=dbConnection.createStatement();
            ResultSet orderNumberResultSet=sqlStatement.executeQuery("SELECT max(order_number) from retailer_order_wholesaler where retailer_id='"+ UserInfo.accessId+"'");
            while(orderNumberResultSet.next())
            {
                orderNumber=orderNumberResultSet.getInt("max");
            }
            if(orderNumber==0)
                orderNumber=1;
            else
                orderNumber+=1;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        orderNumberLabel.setText("ORDER NO."+orderNumber);
        orderToVBox.setVisible(true);
        addMedicineVBox.setVisible(true);
        orderNumberLabel.setVisible(true);
    }

    public void onMedicineAdd()
    {
      String name=medicineName.getText();
      String quant=quantity.getText();
      String regexQuantity = "^[0-9]*$";
      if(name.equals("") && quant.equals("")) {
          errorLabel.setText("All Field Missing!!");
          errorLogVBox.setVisible(true);
      }
      else if(name.equals("")) {
          errorLabel.setText("Enter Medicine Name!!");
          errorLogVBox.setVisible(true);
      }
      else if(quant.equals("")) {
          errorLabel.setText("Enter Quantity!!");
          errorLogVBox.setVisible(true);
      }
      else if(quant.equals("0")) {
          errorLabel.setText("Quantity can not be 0!!");
          errorLogVBox.setVisible(true);
      }
      else if(!quant.matches(regexQuantity)) {
          errorLogVBox.setVisible(true);
          errorLabel.setText("Quantity must be a number !!");
      }
      else
      {
        medicineList.add(new Medicine(serialNumber,name,Integer.parseInt(quant)));
        orderTableView.getItems().clear();
        orderTableView.getItems().addAll(medicineList);
        serialNumber++;
        errorLabel.setText("");
        medicineName.setText("");
        quantity.setText("");
        errorLogVBox.setVisible(false);
      }
    }

    public void onCancel()
    {
        disableVBox();
        selectWholesalerVBox.setVisible(true);
        orderTableView.getItems().clear();
        errorLabel.setText("");
        medicineName.setText("");
        quantity.setText("");

    }



    public void onOrderMedicine()
    {
        Iterator<Medicine> medicineIterator=medicineList.iterator();
        if(!medicineIterator.hasNext())
        {
            errorLabel.setText("No medicines to order");
            errorLogVBox.setVisible(true);
        }
        else
        {
            try{
                int wholesalerId=0;
                Connection dbConnect=JDBC.databaseConnect();
                Statement sqlStatement=dbConnect.createStatement();
                String select=(String)selectWholesalerComboBox.getSelectionModel().getSelectedItem();
                ResultSet wholesalerResultSet=sqlStatement.executeQuery("Select username,user_access.user_access_id,name from user_access,store_info where user_type_id=1 and user_access.user_access_id=store_info.user_access_id order by name");
                while(wholesalerResultSet.next())
                {
                    if((wholesalerResultSet.getString("name")+"("+wholesalerResultSet.getString("username")+")").equals(select)) {
                        wholesalerId = wholesalerResultSet.getInt("user_access_id");
                        break;
                    }
                }
                Calendar dateToday = Calendar.getInstance();
                String date=dateToday.get(Calendar.DAY_OF_MONTH)+"-"+(dateToday.get(Calendar.MONTH) + 1)+"-"+dateToday.get(Calendar.YEAR);
                PreparedStatement insertDB=dbConnect.prepareStatement("insert into retailer_order_wholesaler values(?,?,?,?,?,?)");
                insertDB.setInt(1,orderNumber);
                insertDB.setInt(2,UserInfo.accessId);
                insertDB.setInt(3,wholesalerId);
                insertDB.setInt(4,0);
                insertDB.setString(5,"NA");
                insertDB.setString(6,date);
                insertDB.executeUpdate();

                ResultSet medicine=sqlStatement.executeQuery("select retailer_order_wholesaler_id from retailer_order_wholesaler where retailer_id='"+UserInfo.accessId+"' and order_number='"+orderNumber+"'");
                int id=0;
                while(medicine.next())
                {
                 id=medicine.getInt("retailer_order_wholesaler_id");
                 break;
                }

                Iterator<Medicine> iterate=medicineList.iterator();
                while(iterate.hasNext())
                {
                    Medicine med=iterate.next();
                    PreparedStatement ps=dbConnect.prepareStatement("insert into retailer_ordered_medicines values(?,?,?)");
                    ps.setInt(1,id);
                    ps.setString(2,med.getName());
                    ps.setInt(3,med.getQuantity());
                    ps.executeUpdate();
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            serialNumber=1;
            medicineList.clear();
            onCancel();
        }
    }

}
