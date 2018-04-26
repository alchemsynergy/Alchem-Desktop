package Main.Controllers.Wholesaler;


import Main.Helpers.UserInfo;
import Main.Helpers.Wholesaler.ViewOrder;
import Main.JdbcConnection.JDBC;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

public class ViewOrderController {
    @FXML
    TableView orderTableView,dOrderTableView;
    @FXML
    TableColumn snumber,serialno,rname,status,mname,quantity,date;
    @FXML
    Pane orderSummaryPane;
    @FXML
    Label dateLabel,rnameLabel;
    @FXML
    HBox reasonHBox;
    @FXML
    ComboBox statusComboBox;
    @FXML
    TextField reasonField;
    @FXML
    Label errorLabel;

    static int r=0,idSelected;

    ObservableList<ViewOrder> orders= FXCollections.observableArrayList(),dorders=FXCollections.observableArrayList();

    public void initialize()
    {
        initializeTable();
        setValue();
        disablePane();
    }
    void initializeTable()
    {
        snumber.setCellValueFactory(new PropertyValueFactory<ViewOrder,Integer>("snumber"));
        serialno.setCellValueFactory(new PropertyValueFactory<ViewOrder,Integer>("serial"));
        rname.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("rname"));
        status.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("status"));
        mname.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("medicineName"));
        quantity.setCellValueFactory(new PropertyValueFactory<ViewOrder,Integer>("quantity"));
        date.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("date"));
        orderTableView.setRowFactory( tv -> {
            TableRow<ViewOrder> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    dorders.clear();
                    if(r!=0)
                       statusComboBox.getItems().clear();
                    r=1;
                    int rowNumber= row.getIndex();
                    int k=1,chk=1;
                    orderSummaryPane.setVisible(true);
                    reasonHBox.setVisible(false);
                    statusComboBox.setDisable(false);
                    Iterator<ViewOrder> iterate=orders.iterator();
                    while(iterate.hasNext()) {
                        ViewOrder ord = iterate.next();
                        if (chk == (rowNumber+1)) {
                            dateLabel.setText("Date : "+ord.getDate());
                            rnameLabel.setText("Retailer Name : "+ord.getRname());
                            try {
                                idSelected=ord.getId();
                                Connection conn = JDBC.databaseConnect();
                                Statement stmt = conn.createStatement();
                                ResultSet rs = stmt.executeQuery("SELECT * from retailer_ordered_medicines where retailer_order_wholesaler_id='" + ord.getId() + "'");
                                while (rs.next()) {
                                    dorders.add(new ViewOrder(k, rs.getString("medicine_name"), rs.getInt("quantity")));
                                    k++;
                                }
                                if(ord.getStatus().equals("Order Received"))
                                {
                                    statusComboBox.getItems().add("Order Received");
                                    statusComboBox.getItems().add("Processing Order");
                                    statusComboBox.getItems().add("Order out for delivery");
                                    statusComboBox.getItems().add("Order Delivered");
                                    statusComboBox.getItems().add("Cancel order");
                                }
                                else if(ord.getStatus().equals("Processing Order"))
                                {
                                    statusComboBox.getItems().add("Processing Order");
                                    statusComboBox.getItems().add("Order out for delivery");
                                    statusComboBox.getItems().add("Order Delivered");
                                    statusComboBox.getItems().add("Cancel order");
                                }
                                else if(ord.getStatus().equals("Order out for delivery"))
                                {
                                    statusComboBox.getItems().add("Order out for delivery");
                                    statusComboBox.getItems().add("Order Delivered");
                                    statusComboBox.getItems().add("Cancel order");
                                }
                                else if(ord.getStatus().equals("Order Delivered"))
                                {
                                    statusComboBox.getItems().add("Order Delivered");
                                    statusComboBox.getItems().add("Cancel order");
                                }
                                else
                                {
                                    statusComboBox.getItems().add("Order Cancelled");
                                    statusComboBox.setDisable(true);
                                    try{
                                        Connection conn1=JDBC.databaseConnect();
                                        Statement stmt1=conn.createStatement();
                                        ResultSet rs1=stmt1.executeQuery("SELECT reason_fail from retailer_order_wholesaler where retailer_order_wholesaler_id='"+ord.getId()+"'");
                                        while(rs1.next())
                                        {
                                            reasonField.setText(rs1.getString("reason_fail"));
                                            break;
                                        }
                                    }catch(Exception e){e.printStackTrace();}
                                    reasonHBox.setVisible(true);
                                    reasonHBox.setDisable(true);
                                }
                                statusComboBox.getSelectionModel().selectFirst();
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        chk++;
                    }
                    dOrderTableView.setItems(dorders);
                }
            });
            return row ;
        });
    }
    void disablePane()
    {

        errorLabel.setVisible(false);
        reasonHBox.setVisible(false);
        orderSummaryPane.setVisible(false);
    }
    void setValue()
    {
        int k=0;
        try
        {
            Connection dbConnection= JDBC.databaseConnect();
            PreparedStatement ps=dbConnection.prepareStatement("update retailer_order_wholesaler set status='1' where status='0' and wholesaler_id=?");
            ps.setInt(1,UserInfo.accessId);
            ps.execute();
            Statement stmt=dbConnection.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT  * from retailer_order_wholesaler where wholesaler_id='"+ UserInfo.accessId+"'");
            while(rs.next())
            {
                k++;
                int status=rs.getInt("status");
                String statusIndication;
                if(status==0)
                    statusIndication="Order Sent";
                else if(status==1)
                    statusIndication="Order Received";
                else if(status==2)
                    statusIndication="Processing Order";
                else if(status==3)
                    statusIndication="Order out for delivery";
                else if(status==4)
                    statusIndication="Order cancelled by Retailer";
                else if(status==5)
                    statusIndication="Order cancelled by you";
                else
                    statusIndication="Order Delivered";
                Statement statement=dbConnection.createStatement();
                String sname="-";
                ResultSet rs1=statement.executeQuery("SELECT  name from store_info where user_access_id='"+rs.getInt("retailer_id")+"'");
                while(rs1.next())
                {
                    sname=rs1.getString("name");
                    break;
                }
                String date1=rs.getString("date");
                orders.add(new ViewOrder(k,date1,sname,statusIndication,rs.getInt("retailer_order_wholesaler_id")));
            }
            if(k==0)
                orders.add(new ViewOrder(0,"-","-","-",0));
            orderTableView.setItems(orders);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onComboBoxItemSelected()
    {
        String selected=(String)statusComboBox.getSelectionModel().getSelectedItem();
        if(selected.equals("Cancel order"))
        {
            reasonHBox.setVisible(true);
            reasonField.setDisable(false);
        }
        else if( selected.equals("Order Cancelled"))
        {
            reasonHBox.setVisible(true);
            reasonField.setDisable(true);
        }
        else
        {
            reasonHBox.setVisible(false);
            errorLabel.setVisible(false);
            reasonField.setText("");
        }

    }
    public void onCancel()
    {
        orderSummaryPane.setVisible(false);
        errorLabel.setVisible(false);
    }

    public void onSave()
    {
        int statusValue=0;
        String selected=(String)statusComboBox.getSelectionModel().getSelectedItem();
        if(selected.equals("Order Received"))
            statusValue=1;
        else if(selected.equals("Processing Order"))
            statusValue=2;
        else if(selected.equals("Order out for delivery"))
            statusValue=3;
        else if(selected.equals("Order Delivered"))
            statusValue=6;
        else
            statusValue=5;
        if(statusValue==5 && reasonField.getText().equals(""))
        {
            errorLabel.setVisible(true);
        }
        else {
            if (statusValue == 5) {
                try {
                    Connection conn = JDBC.databaseConnect();
                    PreparedStatement ps = conn.prepareStatement("update retailer_order_wholesaler set status=?,reason_fail=? where retailer_order_wholesaler_id=?");
                    ps.setInt(1,statusValue);
                    ps.setString(2,reasonField.getText());
                    ps.setInt(3,idSelected);
                    ps.execute();
                    errorLabel.setVisible(false);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    Connection conn = JDBC.databaseConnect();
                    PreparedStatement ps = conn.prepareStatement("update retailer_order_wholesaler set status=? where retailer_order_wholesaler_id=?");
                    ps.setInt(1,statusValue);
                    ps.setInt(2,idSelected);
                    ps.execute();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            ObservableList<ViewOrder> temp_orders=FXCollections.observableArrayList();
            Iterator<ViewOrder> iterate=orders.iterator();
            while(iterate.hasNext()) {
                ViewOrder ord = iterate.next();
                if(ord.getId()==idSelected)
                {
                    String statusIndication;
                    if(statusValue==0)
                        statusIndication="Order Sent";
                    else if(statusValue==1)
                        statusIndication="Order Received";
                    else if(statusValue==2)
                        statusIndication="Processing Order";
                    else if(statusValue==3)
                        statusIndication="Order out for delivery";
                    else if(statusValue==4)
                        statusIndication="Order cancelled by Retailer";
                    else if(statusValue==5)
                        statusIndication="Order cancelled by you";
                    else
                        statusIndication="Order Delivered";
                    ord.setStatus(statusIndication);
                }
                temp_orders.add(new ViewOrder(ord.getSnumber(),ord.getDate(),ord.getRname(),ord.getStatus(),ord.getId()));
            }
            orders.clear();
            orders.addAll(temp_orders);
            orderSummaryPane.setVisible(false);
        }
    }
}

