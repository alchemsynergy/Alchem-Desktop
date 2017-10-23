package Main.Controllers.Retailers;


import Main.Helpers.Retailers.Purchase;
import Main.JdbcConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class ViewPurchaseController {

    @FXML
    private TableView<Purchase> purchaseTableView;

    @FXML
    private TableColumn<Purchase,String> dateColumn;
    @FXML
    private TableColumn<Purchase,Long> billNumberColumn;
    @FXML
    private TableColumn<Purchase,String> wholesalerNameColumn;
    @FXML
    private TableColumn<Purchase,String> modeColumn;
    @FXML
    private TableColumn<Purchase,Float> amountColumn;

    @FXML
    private HBox dateHBox,datePickedHBox;

    @FXML
    private Label todayPurchaseLabel,yesterdayPurchaseLabel,day3PurchaseLabel,day4PurchaseLabel,day5PurchaseLabel,day6PurchaseLabel,day7PurchaseLabel,dateSelectedLabel;

    @FXML
    private DatePicker purchaseDatePicker;

    @FXML
    private Button particularDatePurchaseButton;

    ObservableList<Purchase> purchaseList,purchaseParticularDayList;
    private long user_id=(long)2;
    static boolean purchaseCheck=true;


    public void initialize()
    {
     initializeDatePicker();
     initializeTable();
     calculateTotalSale();
     addingToolTip();
    }

    public void addingToolTip()
    {
        purchaseTableView.setRowFactory(tv -> new TableRow<Purchase>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Purchase purchase, boolean empty) {
                super.updateItem(purchase, empty);
                if (purchase == null) {
                    setTooltip(null);
                }
                else {
                    String tooltipText="";
                    long rpBillId=0;
                    if (purchase.getBillNumber() == 0) {
                        tooltip.setText("NA");
                        setTooltip(tooltip);
                    }
                    else
                    {
                        try {
                            Connection dbConnection = JDBC.databaseConnect();
                            Statement sqlStatement = dbConnection.createStatement();
                            ResultSet purchaseResultSet = sqlStatement.executeQuery("SELECT rp_bill_id FROM retailer_purchase_bill WHERE user_access_id='"+user_id+"' AND bill_no='"+purchase.getBillNumber()+"'");
                            while(purchaseResultSet.next())
                            {
                                rpBillId=purchaseResultSet.getLong("rp_bill_id");
                                break;
                            }
                            purchaseResultSet.close();
                            ResultSet itemResultSet=sqlStatement.executeQuery("SELECT medicine.name,medicine.hsn_number,medicine_info.batch_number,medicine_info.cost_price,quantity.piece,retailer_purchase_bill_info.medicine_info_id FROM retailer_purchase_bill_info JOIN medicine_info ON retailer_purchase_bill_info.medicine_info_id=medicine_info.medicine_info_id JOIN medicine ON medicine.medicine_id=medicine_info.medicine_id JOIN quantity ON quantity.medicine_info_id=medicine_info.medicine_info_id where retailer_purchase_bill_info.rp_bill_id='"+rpBillId+"'");
                            while(itemResultSet.next())
                            {
                                String name=itemResultSet.getString("name");
                                String hsn=itemResultSet.getString("hsn_number");
                                String batch=itemResultSet.getString("batch_number");
                                int quantity=itemResultSet.getInt("piece");
                                float amt=quantity*itemResultSet.getFloat("cost_price");
                                tooltipText=tooltipText+"Item- "+name+"  Batch No.- "+batch+"  HSN No.-"+hsn+"  Quantity- "+quantity+"  Amount- "+amt+"\n";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tooltip.setText(tooltipText);
                        setTooltip(tooltip);
                    }
                }
            }
        });
    }


    public void datePicked()
    {
        String date=purchaseDatePicker.getValue().toString();
        float sum=0.0f;
        purchaseParticularDayList=getParticularPurchaseDate(date);
        if(purchaseParticularDayList.size()<1)
        {
            purchaseParticularDayList.add(new Purchase(date, (long) 0,"-","-",0.0f));
        }
        try
        {
            Connection dbConnection = JDBC.databaseConnect();
            Statement sqlStatement = dbConnection.createStatement();
            String sqlQuery = "SELECT sum(total_amount) from retailer_purchase_bill where user_access_id='" + user_id + "' and date='" + date + "'";
            ResultSet saleResultSet = sqlStatement.executeQuery(sqlQuery);
            while (saleResultSet.next()) {
                sum = saleResultSet.getFloat(1);
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Calendar dateToday= Calendar.getInstance();
        int year,day,month;
        String dateChk,tense;
        year = dateToday.get(Calendar.YEAR);
        month= dateToday.get(Calendar.MONTH)+1;
        day= dateToday.get(Calendar.DAY_OF_MONTH);
        dateChk=year+"-"+month+"-"+day;
        if(dateChk.equals(date))
            tense="is";
        else
            tense="was";
        purchaseTableView.setItems(purchaseParticularDayList);
        dateSelectedLabel.setText("Total Purchase on "+date+" "+tense+" Rs."+sum);
        datePickedHBox.setVisible(true);

    }

    public ObservableList<Purchase> getParticularPurchaseDate(String particularDate)
    {
        String sqlQuery="SELECT * from retailer_purchase_bill where user_access_id='"+user_id+"' and date='"+particularDate+"'";
        purchaseParticularDayList= FXCollections.observableArrayList();
        purchaseParticularDayList=getList(sqlQuery,purchaseParticularDayList);
        return purchaseParticularDayList;
    }

    public void purchaseParticularDay()
    {
        if(purchaseCheck)
        {
            particularDatePurchaseButton.setText("See Purchase of Particular Day v");
            dateHBox.setVisible(true);
            purchaseCheck=false;
        }
        else
        {
            particularDatePurchaseButton.setText("See Purchase of Particular Day >");
            dateHBox.setVisible(false);
            datePickedHBox.setVisible(false);
            purchaseDatePicker.getEditor().clear();
            purchaseTableView.setItems(purchaseList);
            purchaseCheck=true;
        }
    }

    public void calculateTotalSale()
    {
        Calendar dateToday= Calendar.getInstance();
        long[] year=new long[7];
        long[] month=new long[7];
        long[] day=new long[7];
        long dateCheck;
        year[0] = dateToday.get(Calendar.YEAR);
        month[0] = dateToday.get(Calendar.MONTH)+1;
        day[0] = dateToday.get(Calendar.DAY_OF_MONTH);
        double[] sum=new double[7];
        String date;
        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement sqlStatement = dbConnection.createStatement();
            for (int i = 0; i < 7; i++) {
                date = year[i] + "-" + month[i] + "-" + day[i];
                String sqlQuery = "SELECT sum(total_amount) from retailer_purchase_bill where user_access_id='" + user_id + "' and date='" + date + "'";
                ResultSet saleResultSet = sqlStatement.executeQuery(sqlQuery);
                while (saleResultSet.next()) {
                    sum[i] = saleResultSet.getFloat(1);
                    break;
                }
                if (i != 6) {
                    dateCheck = day[i] - 1;
                    if (dateCheck == 0) {
                        if (month[i] == 2 || month[i] == 4 || month[i] == 6 || month[i] == 8 || month[i] == 9 || month[i] == 11) {
                            day[i + 1] = 31;
                            month[i + 1] = month[i] - 1;
                            year[i + 1] = year[i];
                        } else if (month[i] == 5 || month[i] == 7 || month[i] == 10 || month[i] == 12) {
                            day[i + 1] = 30;
                            month[i + 1] = month[i] - 1;
                            year[i + 1] = year[i];
                        } else if (month[i] == 1) {
                            day[i + 1] = 31;
                            month[i + 1] = 12;
                            year[i + 1] = year[i] - 1;
                        } else {
                            if (year[i] % 4 == 0) {
                                if (year[i] % 100 == 0) {
                                    if (year[i] % 400 == 0)
                                        day[i + 1] = 29;
                                    else
                                        day[i + 1] = 28;
                                } else
                                    day[i + 1] = 29;
                            } else
                                day[i + 1] = 28;
                            month[i + 1] = month[i] - 1;
                            year[i + 1] = year[i];
                        }
                    } else {
                        day[i + 1] = day[i] - 1;
                        month[i + 1] = month[i];
                        year[i + 1] = year[i];
                    }
                }
            }
            todayPurchaseLabel.setText("Today's Total Purchase is Rs." + sum[0]);
            yesterdayPurchaseLabel.setText("Yesterday's Total Purchase was Rs." + sum[1]);
            day3PurchaseLabel.setText("Total Purchase on " + day[2] + "-" + month[2] + "-" + year[2] + " was Rs." + sum[2]);
            day4PurchaseLabel.setText("Total Purchase on " + day[3] + "-" + month[3] + "-" + year[3] + " was Rs." + sum[3]);
            day5PurchaseLabel.setText("Total Purchase on " + day[4] + "-" + month[4] + "-" + year[4] + " was Rs." + sum[4]);
            day6PurchaseLabel.setText("Total Purchase on " + day[5] + "-" + month[5] + "-" + year[5] + " was Rs." + sum[5]);
            day7PurchaseLabel.setText("Total Purchase on " + day[6] + "-" + month[6] + "-" + year[6] + " was Rs." + sum[6]);
        }catch (Exception e )
        {
            e.printStackTrace();
        }

        }

    public void initializeDatePicker(){
        dateHBox.setVisible(false);
        datePickedHBox.setVisible(false);
    }

    public void initializeTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<Purchase,String>("date"));
        billNumberColumn.setCellValueFactory(new PropertyValueFactory<Purchase,Long>("billNumber"));
        wholesalerNameColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("wholesalerName"));
        modeColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("mode"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Float>("amount"));
        purchaseList=getPurchase();
        if(purchaseList.size()<1)
        {
            Calendar dateToday= Calendar.getInstance();
            int year,day,month;
            year = dateToday.get(Calendar.YEAR);
            month= dateToday.get(Calendar.MONTH)+1;
            day= dateToday.get(Calendar.DAY_OF_MONTH);
            String date=year+"-"+month+"-"+day;
            purchaseList.add(new Purchase(date, (long) 0,"-","-",0.0f));
        }
        purchaseTableView.setItems(purchaseList);

    }

    public ObservableList<Purchase> getPurchase()
    {
        purchaseList= FXCollections.observableArrayList();
        String sqlQuery="SELECT * from retailer_purchase_bill where user_access_id='"+user_id+"'";
        purchaseList= FXCollections.observableArrayList();
        purchaseList=getList(sqlQuery,purchaseList);
        return purchaseList;
    }

    public ObservableList<Purchase> getList(String sqlQuery,ObservableList<Purchase> purchaseListDetails)
    {
        String date,wholesalerName,mode;
        Long billNumber;
        Float amount;
        try
        {
            Connection dbConnection= JDBC.databaseConnect();
            Statement sqlStatement=dbConnection.createStatement();
            ResultSet saleResultSet=sqlStatement.executeQuery(sqlQuery);
            while(saleResultSet.next())
            {
                date=saleResultSet.getString("date");
                wholesalerName=saleResultSet.getString("wholesaler_name");
                mode=saleResultSet.getString("mode");
                billNumber=saleResultSet.getLong("bill_no");
                amount=saleResultSet.getFloat("total_amount");
                purchaseListDetails.add(new Purchase(date,billNumber,wholesalerName,mode,amount));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return purchaseListDetails;
    }



}
