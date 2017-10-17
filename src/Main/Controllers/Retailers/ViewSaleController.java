package Main.Controllers.Retailers;


import Main.Helpers.Retailers.Sale;
import Main.Helpers.UserInfo;
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

public class ViewSaleController {

    @FXML
    private TableView<Sale> saleTableView;

    @FXML
    private TableColumn<Sale,String> dateColumn;
    @FXML
    private TableColumn<Sale,Long> billNumberColumn;
    @FXML
    private TableColumn<Sale,String> patientNameColumn;
    @FXML
    private TableColumn<Sale,String> doctorNameColumn;
    @FXML
    private TableColumn<Sale,String> companyNameColumn;
    @FXML
    private TableColumn<Sale,String> modeColumn;
    @FXML
    private TableColumn<Sale,Float> amountColumn;

    @FXML
    private Label todaySaleLabel,yesterdaySaleLabel,day3SaleLabel,day4SaleLabel,day5SaleLabel,day6SaleLabel,day7SaleLabel,dateSelectedLabel;

    @FXML
    private Button particularDateSaleButton;

    @FXML
    private DatePicker saleDatePicker;

    @FXML
    private HBox dateHBox,datePickedHBox;

    private static double drawableWidth;

    ObservableList<Sale> saleList,saleParticularDayList;
    Long user_id=(long) UserInfo.accessId;
    static boolean saleCheck=true;

 public void initialize()
 {
     initializeDatePicker();
     initializeTable();
     addingToolTip();
     calculateTotalSale();
 }

 public void initializeDatePicker(){
     dateHBox.setVisible(false);
     datePickedHBox.setVisible(false);
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
     try
     {
         Connection dbConnection = JDBC.databaseConnect();
         Statement sqlStatement = dbConnection.createStatement();
         for(int i=0;i<7;i++)
         {
             date= year[i]+"-"+month[i]+"-"+day[i];
             String sqlQuery="SELECT sum(total_amount) from retailer_sale_bill where user_access_id='"+user_id+"' and date='"+date+"'" ;
             ResultSet saleResultSet = sqlStatement.executeQuery(sqlQuery);
             while (saleResultSet.next()) {
                 sum[i]= saleResultSet.getFloat(1);
                 break;
             }
             if(i!=6)
             {
                 dateCheck=day[i]-1;
                 if (dateCheck == 0) {
                     if (month[i] == 2 || month[i] == 4 || month[i] == 6 || month[i] == 8 || month[i] == 9 || month[i] == 11) {
                         day[i + 1] = 31;
                         month[i + 1] = month[i] - 1;
                         year[i+1]=year[i];
                     } else if (month[i] == 5 || month[i] == 7 || month[i] == 10 || month[i] == 12) {
                         day[i + 1] = 30;
                         month[i + 1] = month[i] - 1;
                         year[i+1]=year[i];
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
                         month[i+1]=month[i]-1;
                         year[i+1]=year[i];
                     }
                 }
                 else{
                     day[i+1]=day[i]-1;
                     month[i+1]=month[i];
                     year[i+1]=year[i];
                 }
             }
         }
         todaySaleLabel.setText("Today's Total Sale is Rs."+sum[0]);
         yesterdaySaleLabel.setText("Yesterday's Total Sale was Rs."+sum[1]);
         day3SaleLabel.setText("Total Sale on "+day[2] +"-"+month[2]+"-"+year[2]+" was Rs."+sum[2]);
         day4SaleLabel.setText("Total Sale on "+day[3] +"-"+month[3]+"-"+year[3]+" was Rs."+sum[3]);
         day5SaleLabel.setText("Total Sale on "+day[4] +"-"+month[4]+"-"+year[4]+" was Rs."+sum[4]);
         day6SaleLabel.setText("Total Sale on "+day[5] +"-"+month[5]+"-"+year[5]+" was Rs."+sum[5]);
         day7SaleLabel.setText("Total Sale on "+day[6] +"-"+month[6]+"-"+year[6]+" was Rs."+sum[6]);

     }
     catch (Exception e)
     {
         e.printStackTrace();
     }

 }

    public void addingToolTip()
    {
        saleTableView.setRowFactory(tv -> new TableRow<Sale>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Sale sale, boolean empty) {
                super.updateItem(sale, empty);
                if (sale == null) {
                    setTooltip(null);
                }
                else {
                    String tooltipText="";
                    long rsBillId=0;
                    if (sale.getBillNumber() == 0) {
                        tooltip.setText("NA");
                        setTooltip(tooltip);
                    }
                    else
                    {
                        try {
                            Connection dbConnection = JDBC.databaseConnect();
                            Statement sqlStatement = dbConnection.createStatement();
                            ResultSet saleResultSet = sqlStatement.executeQuery("SELECT rs_bill_id,discount FROM retailer_sale_bill WHERE user_access_id='"+user_id+"' AND bill_no='"+sale.getBillNumber()+"'");
                            while(saleResultSet.next())
                            {
                                rsBillId=saleResultSet.getLong("rs_bill_id");
                                tooltipText="Discount - "+saleResultSet.getFloat("discount")+"%\n";
                                break;
                            }
                            saleResultSet.close();
                            ResultSet itemResultSet=sqlStatement.executeQuery("SELECT * FROM retailer_sale_bill_info where rs_bill_id='"+rsBillId+"'");
                            while(itemResultSet.next())
                            {
                                int quantity=itemResultSet.getInt("quantity");
                                float amt=quantity*itemResultSet.getFloat("rate");
                                String item=itemResultSet.getString("item");
                                tooltipText=tooltipText+"Item- "+item+"  Quantity- "+quantity+"  Amount- "+amt+"\n";
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
     String date=saleDatePicker.getValue().toString();
     float sum=0.0f;
     saleParticularDayList=getParticularSaleDate(date);
     if(saleParticularDayList.size()<1)
     {
       saleParticularDayList.add(new Sale(date, (long) 0,"-","-","-","-",0.0f));
     }
     try
     {
         Connection dbConnection = JDBC.databaseConnect();
         Statement sqlStatement = dbConnection.createStatement();
         String sqlQuery = "SELECT sum(total_amount) from retailer_sale_bill where user_access_id='" + user_id + "' and date='" + date + "'";
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
     saleTableView.setItems(saleParticularDayList);
     dateSelectedLabel.setText("Total Sale on "+date+" "+tense+" Rs."+sum);
     datePickedHBox.setVisible(true);

 }

 public void saleParticularDay()
 {
        if(saleCheck)
        {
            particularDateSaleButton.setText("See Sale of Particular Day v");
            dateHBox.setVisible(true);
            saleCheck=false;
        }
        else
        {
            particularDateSaleButton.setText("See Sale of Particular Day >");
            dateHBox.setVisible(false);
            datePickedHBox.setVisible(false);
            saleDatePicker.getEditor().clear();
            saleTableView.setItems(saleList);
            saleCheck=true;
        }
 }

 public void initializeTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("date"));
        billNumberColumn.setCellValueFactory(new PropertyValueFactory<Sale,Long>("billNumber"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<Sale, String>("patientName"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<Sale, String>("doctorName"));
        companyNameColumn.setCellValueFactory(new PropertyValueFactory<Sale, String>("companyName"));
        modeColumn.setCellValueFactory(new PropertyValueFactory<Sale, String>("mode"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Sale, Float>("amount"));
        saleList=getSale();
        if(saleList.size()<1)
        {
            Calendar dateToday= Calendar.getInstance();
            int year,day,month;
            year = dateToday.get(Calendar.YEAR);
            month= dateToday.get(Calendar.MONTH)+1;
            day= dateToday.get(Calendar.DAY_OF_MONTH);
            String date=year+"-"+month+"-"+day;
            saleList.add(new Sale(date, (long) 0,"-","-","-","-",0.0f));
        }
        saleTableView.setItems(saleList);

    }

    public ObservableList<Sale> getSale()
    {
       String sqlQuery="SELECT * from retailer_sale_bill where user_access_id='"+user_id+"'";
       saleList= FXCollections.observableArrayList();
       saleList=getList(sqlQuery,saleList);
       return saleList;
    }
    public ObservableList<Sale> getParticularSaleDate(String particularDate)
    {
        String sqlQuery="SELECT * from retailer_sale_bill where user_access_id='"+user_id+"' and date='"+particularDate+"'";
        saleParticularDayList= FXCollections.observableArrayList();
        saleParticularDayList=getList(sqlQuery,saleParticularDayList);
        return saleParticularDayList;
    }
    public ObservableList<Sale> getList(String sqlQuery,ObservableList<Sale> saleListDetails)
    {
        String date,patientName,doctorName,companyName,mode;
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
                patientName=saleResultSet.getString("patient_name");
                companyName=saleResultSet.getString("company");
                doctorName=saleResultSet.getString("doctor_name");
                mode=saleResultSet.getString("mode");
                billNumber=saleResultSet.getLong("bill_no");
                amount=saleResultSet.getFloat("total_amount");
                saleListDetails.add(new Sale(date,billNumber,patientName,doctorName,companyName,mode,amount));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return saleListDetails;
    }

    public static void setDrawableWidth(double width) {
        drawableWidth = width;
    }
}
