package Main.Controllers.Retailers;


import Main.Helpers.Medicine;
import Main.Helpers.Retailers.Sale;
import Main.Helpers.Retailers.ViewOrder;
import Main.Helpers.UserInfo;
import Main.JdbcConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.swing.text.View;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;


public class ViewOrderController {
    @FXML
    TableView<ViewOrder> orderTableView,medicineTableView;
    @FXML
    TableColumn<ViewOrder,String> dateColumn;
    @FXML
    TableColumn<ViewOrder,Integer> orderNumberColumn;
    @FXML
    TableColumn<ViewOrder,String> wholesalerNameColumn;
    @FXML
    TableColumn<ViewOrder,String> statusColumn;
    @FXML
    TableColumn<ViewOrder,Integer> snumber;
    @FXML
    TableColumn<ViewOrder,String> mname;
    @FXML
    TableColumn<ViewOrder,Integer> quantity;
    @FXML
    AnchorPane detailedAnchorPane;
    @FXML
    Label statusLabel,orderNoLabel,wnameLabel,dateLabel,rLabel;
    @FXML
    TextField rTextField;
    @FXML
    Button cancelButton;

    static ObservableList<ViewOrder> viewOrders,medicineList;
    static int id;

    public void initialize()
    {
        initializeTable();
        disableAnchor();
        rowclicked();
        setValues();
    }
    void initializeTable()
    {
        dateColumn.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("date"));
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<ViewOrder,Integer>("order"));
        wholesalerNameColumn.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("wname"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("status"));
        snumber.setCellValueFactory(new PropertyValueFactory<ViewOrder,Integer>("snumber"));
        mname.setCellValueFactory(new PropertyValueFactory<ViewOrder,String>("mname"));
        quantity.setCellValueFactory(new PropertyValueFactory<ViewOrder,Integer>("quantity"));
    }
    void disableAnchor()
    {
        detailedAnchorPane.setVisible(false);
    }
    void rowclicked()
    {
        orderTableView.setRowFactory( tv -> {
            TableRow<ViewOrder> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    int k=1,r=1;
                    rTextField.setPromptText("");
                    rTextField.setText("");
                    rTextField.setStyle("-fx-border-color:transparent ; -fx-border-width: 3px ;");
                    detailedAnchorPane.setVisible(true);
                    rLabel.setVisible(true);
                    rTextField.setVisible(true);
                    cancelButton.setVisible(true);
                    int rowNumber= row.getIndex();
                    Iterator<ViewOrder> iterate=viewOrders.iterator();
                    while(iterate.hasNext()) {
                        ViewOrder ord = iterate.next();
                        if(k==(rowNumber+1))
                        {
                           dateLabel.setText("Date : "+ord.getDate());
                           wnameLabel.setText("Wholesaler Name : "+ord.getWname());
                           orderNoLabel.setText("Order Number : "+Integer.toString(ord.getOrder()));
                           statusLabel.setText("Status:"+ord.getStatus());
                           id=ord.getId();
                           if(ord.getStatus().contains("cancelled"))
                           {
                               rLabel.setVisible(false);
                               rTextField.setVisible(false);
                               cancelButton.setVisible(false);
                           }
                           try{
                               medicineList=FXCollections.observableArrayList();
                               Connection conn=JDBC.databaseConnect();
                               Statement stmt=conn.createStatement();
                               ResultSet rs=stmt.executeQuery("select * from retailer_ordered_medicines where retailer_order_wholesaler_id='"+ord.getId()+"'");
                               while(rs.next())
                               {
                                   medicineList.add(new ViewOrder(r,rs.getString("medicine_name"),rs.getInt("quantity")));
                                   r++;
                               }
                               medicineTableView.setItems(medicineList);
                               break;
                           }catch (Exception e){e.printStackTrace();}

                        }
                        k++;
                    }


                }
            });
            return row ;
        });

    }
    void setValues()
    {
        viewOrders= FXCollections.observableArrayList();
        try{
            int k=0;
            Connection dbConnection= JDBC.databaseConnect();
            Statement sqlStatement=dbConnection.createStatement();
            ResultSet rs=sqlStatement.executeQuery("SELECT * FROM retailer_order_wholesaler where retailer_id='"+ UserInfo.accessId+"'");
            while(rs.next())
            {
                k++;
                int status=rs.getInt("status");
                String statusIndication;
                if(status==0)
                    statusIndication="Order Sent by You";
                else if(status==1)
                    statusIndication="Order Received by Wholesaler";
                else if(status==2)
                    statusIndication="Order is being Processed";
                else if(status==3)
                    statusIndication="Order is out for delivery";
                else if(status==4)
                    statusIndication="Order cancelled by you\nREASON:"+rs.getString("reason_fail");
                else if(status==5)
                    statusIndication="Order cancelled by wholesaler\nREASON:"+rs.getString("reason_fail");
                else
                    statusIndication="Order Delivered by wholesaler";
                int orderNo=rs.getInt("order_number");
                String date=rs.getString("date");
                Statement statement=dbConnection.createStatement();
                String sname="-";
                ResultSet rs1=statement.executeQuery("SELECT  name from store_info where user_access_id='"+rs.getInt("wholesaler_id")+"'");
                while(rs1.next())
                {
                    sname=rs1.getString("name");
                    break;
                }
                viewOrders.add(new ViewOrder(date,orderNo,sname,statusIndication,rs.getInt("retailer_order_wholesaler_id")));
            }
            if(k==0)
                viewOrders.add(new ViewOrder("-",0,"-","-",0));
            orderTableView.setItems(viewOrders);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onCancel()
    {
        if(rTextField.getText().equals(""))
        {
            rTextField.setPromptText("Required");
            rTextField.setStyle("-fx-border-color: red ; -fx-border-width: 3px ;");
        }
        else
        {
            try{
                Connection conn=JDBC.databaseConnect();
                PreparedStatement ps=conn.prepareStatement("UPDATE  retailer_order_wholesaler set status=?,reason_fail=? where retailer_order_wholesaler_id=?");
                ps.setInt(1,4);
                ps.setString(2,rTextField.getText());
                ps.setInt(3,id);
                ps.execute();
                ObservableList<ViewOrder> temp=FXCollections.observableArrayList();
                Iterator<ViewOrder> iterate=viewOrders.iterator();
                while(iterate.hasNext())
                {
                    ViewOrder ord=iterate.next();
                    if(ord.getId()==id)
                    {
                        ord.setStatus("Order cancelled by you\nREASON:"+rTextField.getText());
                    }
                    temp.add(new ViewOrder(ord.getDate(),ord.getOrder(),ord.getWname(),ord.getStatus(),ord.getId()));
                }
                viewOrders.clear();
                viewOrders.addAll(temp);
                onClear();
                rTextField.setText("");
            }catch(Exception e){e.printStackTrace();}
        }
    }
    public void onClear()
    {
        detailedAnchorPane.setVisible(false);
    }
}
