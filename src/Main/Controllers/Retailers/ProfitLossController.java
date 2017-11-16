package Main.Controllers.Retailers;

import Main.Controllers.MainFeaturesTabSceneController;
import Main.Helpers.Retailers.ProfitLoss;
import Main.Helpers.UserInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import Main.JdbcConnection.JDBC;


public class ProfitLossController {
    @FXML
    private TableView<ProfitLoss> profitTableView;

    @FXML
    private TableColumn<ProfitLoss,String> dateColumn;
    @FXML
    private TableColumn<ProfitLoss,Float> totalSaleColumn;
    @FXML
    private TableColumn<ProfitLoss,Float> totalPurchaseColumn;
    @FXML
    private TableColumn<ProfitLoss,Float> profitColumn;

    @FXML
    private HBox dateHBox,datePickedHBox;

    @FXML
    public Label todayProfitLabel,yesterdayProfitLabel,day3ProfitLabel,day4ProfitLabel,day5ProfitLabel,day6ProfitLabel,day7ProfitLabel,dateSelectedLabel;

    @FXML
    private DatePicker profitDatePicker;

    @FXML
    private Button particularDateProfitButton;

    @FXML
    private LineChart<?,?> profitLineChart;

    @FXML
    private CategoryAxis dateAxis;

    @FXML
    private NumberAxis profitAxis;

    MainFeaturesTabSceneController mainFeaturesTabSceneController;
    public static ObservableList<ProfitLoss> profitList,profitParticularDayList;
    private long user_id= UserInfo.accessId;
    static boolean profitCheck=true;
    public static long[] year=new long[7];
    public static long[] month=new long[7];
    public static long[] day=new long[7];
    public static double[] sum=new double[7];
    public static float[] totalSale=new float[7];
    public static float[] totalPurchase=new float[7];

    public void initialize()
    {
        initializeDatePicker();
        initializeTable();
        calculateTotalProfit();
        addingToolTip();
        graphGenerator();
    }

    public void initializeDatePicker(){
        dateHBox.setVisible(false);
        datePickedHBox.setVisible(false);
    }

    public void initializeTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<ProfitLoss,String>("date"));
        totalSaleColumn.setCellValueFactory(new PropertyValueFactory<ProfitLoss, Float>("totalSale"));
        totalPurchaseColumn.setCellValueFactory(new PropertyValueFactory<ProfitLoss, Float>("totalPurchase"));
        profitColumn.setCellValueFactory(new PropertyValueFactory<ProfitLoss,Float>("profit"));
        profitColumn.setCellFactory(new Callback<TableColumn<ProfitLoss,Float>, TableCell<ProfitLoss,Float>>() {
            public TableCell<ProfitLoss,Float> call(TableColumn<ProfitLoss,Float> param) {
                return new TableCell<ProfitLoss,Float>() {
                    @Override
                    public void updateItem(Float item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item!=null) {
                            if(item<0f) {
                                item=item*-1;
                                Label label=new Label(Float.toString(item));
                                label.setTextFill(Color.RED);
                                setGraphic(label);
                            }
                            else if(item==0f)
                            {
                                Label label=new Label(Float.toString(item));
                                label.setTextFill(Color.WHITE);
                                setGraphic(label);
                            }
                            else
                            {
                                Label label=new Label(Float.toString(item));
                                label.setTextFill(Color.LIGHTGREEN);
                                setGraphic(label);
                            }
                        }
                        if(item==null)
                        {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        profitList=getProfit();
        profitTableView.setItems(profitList);

    }

    public ObservableList<ProfitLoss> getProfit()
    {
        profitList= FXCollections.observableArrayList();
        profitList= FXCollections.observableArrayList();
        profitList=getList(profitList);
        return profitList;
    }

    public void profitParticularDay()
    {
        if(profitCheck)
        {
            particularDateProfitButton.setText("See Profit/Loss of Particular Day v");
            dateHBox.setVisible(true);
            profitCheck=false;
        }
        else
        {
            particularDateProfitButton.setText("See Profit/Loss of Particular Day >");
            dateHBox.setVisible(false);
            datePickedHBox.setVisible(false);
            profitDatePicker.getEditor().clear();
            profitTableView.setItems(profitList);
            profitCheck=true;
        }
    }

    public ObservableList<ProfitLoss> getList(ObservableList<ProfitLoss> profitListDetails)
    {
        Float totalSale=0f,totalPurchase=0f,profit;
        Calendar dateToday= Calendar.getInstance();
        int year,day,month,dateCheck;
        year = dateToday.get(Calendar.YEAR);
        month= dateToday.get(Calendar.MONTH)+1;
        day= dateToday.get(Calendar.DAY_OF_MONTH);
        String todayDate;
        try
        {
            Connection dbConnection= JDBC.databaseConnect();
            Statement sqlStatement=dbConnection.createStatement();
            for(int i=0;i<30;i++)
            {
                todayDate=year+"-"+month+"-"+day;
                totalSale=0f;
                totalPurchase=0f;
                profit=0f;
                ResultSet totalSaleResultSet=sqlStatement.executeQuery("SELECT SUM(total_amount) from retailer_sale_bill where date='"+todayDate+"' AND user_access_id='" + user_id + "'");
                while (totalSaleResultSet.next())
                {
                  totalSale=totalSaleResultSet.getFloat("sum");
                }
                ResultSet totalPurchaseResultSet=sqlStatement.executeQuery("SELECT SUM(total_amount) from retailer_purchase_bill where date='"+todayDate+"' AND user_access_id='" + user_id + "'");
                while (totalPurchaseResultSet.next())
                {
                  totalPurchase=totalPurchaseResultSet.getFloat("sum");
                }
                profit=totalSale-totalPurchase;
                profitListDetails.add(new ProfitLoss(todayDate,totalSale,totalPurchase,profit));
                dateCheck = day- 1;
                if (dateCheck == 0)
                {
                    if (month== 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11) {
                        day= 31;
                        month=month- 1;
                    } else if (month == 5 || month == 7 || month == 10 || month == 12) {
                        day= 30;
                        month= month- 1;
                    } else if (month == 1) {
                        day= 31;
                        month= 12;
                        year= year- 1;
                    } else {
                        if (year% 4 == 0) {
                            if (year% 100 == 0) {
                                if (year% 400 == 0)
                                    day= 29;
                                else
                                    day= 28;
                            } else
                                day= 29;
                        } else
                            day= 28;
                        month= month - 1;
                    }
                } else {
                    day= day- 1;
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return profitListDetails;
    }

    public void calculateTotalProfit()
    {
        Calendar dateToday= Calendar.getInstance();
        long dateCheck;
        year[0] = dateToday.get(Calendar.YEAR);
        month[0] = dateToday.get(Calendar.MONTH)+1;
        day[0] = dateToday.get(Calendar.DAY_OF_MONTH);
        String date;
        try {
            Connection dbConnection = JDBC.databaseConnect();
            Statement sqlStatement = dbConnection.createStatement();
            for (int i = 0; i < 7; i++) {
                date = year[i] + "-" + month[i] + "-" + day[i];
                ResultSet totalSaleResultSet=sqlStatement.executeQuery("SELECT SUM(total_amount) from retailer_sale_bill where date='"+date+"' AND user_access_id='" + user_id + "'");
                while (totalSaleResultSet.next())
                {
                    totalSale[i]=totalSaleResultSet.getFloat("sum");
                }
                ResultSet totalPurchaseResultSet=sqlStatement.executeQuery("SELECT SUM(total_amount) from retailer_purchase_bill where date='"+date+"' AND user_access_id='" + user_id + "'");
                while (totalPurchaseResultSet.next())
                {
                    totalPurchase[i]=totalPurchaseResultSet.getFloat("sum");
                }
                sum[i]=totalSale[i]-totalPurchase[i];
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
            setProfitLabel();
        }catch (Exception e )
        {
            e.printStackTrace();
        }

    }

    public void setProfitLabel()
    {
        if(sum[0]>=0) {
            if(sum[0]==0)
                labelColor(todayProfitLabel,1);
            else
                labelColor(todayProfitLabel,3);
            todayProfitLabel.setText("Today's Total Profit is Rs." + sum[0]);
        }
        else
        {
            labelColor(todayProfitLabel,2);
            todayProfitLabel.setText("Today's Total Loss is Rs." + (sum[0]*-1));
        }
        if(sum[1]>=0) {
            if(sum[1]==0)
                labelColor(yesterdayProfitLabel,1);
            else
                labelColor(yesterdayProfitLabel,3);
            yesterdayProfitLabel.setText("Yesterday's Total Profit was Rs." + sum[1]);
        }else
        {
            labelColor(yesterdayProfitLabel,2);
            yesterdayProfitLabel.setText("Yesterday's Total Loss was Rs." + (sum[1]*-1));
        }
        if(sum[2]>=0) {
            if(sum[2]==0)
                labelColor(day3ProfitLabel,1);
            else
                labelColor(day3ProfitLabel,3);
            day3ProfitLabel.setText("Total Profit on " + day[2] + "-" + month[2] + "-" + year[2] + " was Rs." + sum[2]);
        }
        else
        {
            labelColor(day3ProfitLabel,2);
            day3ProfitLabel.setText("Total Loss on " + day[2] + "-" + month[2] + "-" + year[2] + " was Rs." + (sum[2]*-1));
        }
        if(sum[3]>=0) {
            if(sum[3]==0)
                labelColor(day4ProfitLabel,1);
            else
                labelColor(day4ProfitLabel,3);
            day4ProfitLabel.setText("Total Profit on " + day[3] + "-" + month[3] + "-" + year[3] + " was Rs." + sum[3]);
        }
        else
        {
            labelColor(day4ProfitLabel,2);
            day4ProfitLabel.setText("Total Loss on " + day[3] + "-" + month[3] + "-" + year[3] + " was Rs." + (sum[3]*-1));
        }
        if(sum[4]>=0) {
            if(sum[4]==0)
                labelColor(day5ProfitLabel,1);
            else
                labelColor(day5ProfitLabel,3);
            day5ProfitLabel.setText("Total Profit on " + day[4] + "-" + month[4] + "-" + year[4] + " was Rs." + sum[4]);
        }
        else
        {
            labelColor(day5ProfitLabel,2);
            day5ProfitLabel.setText("Total Loss on " + day[4] + "-" + month[4] + "-" + year[4] + " was Rs." + (sum[4]*-1));
        }
        if(sum[5]>=0) {
            if(sum[5]==0)
                labelColor(day6ProfitLabel,1);
            else
                labelColor(day6ProfitLabel,3);
            day6ProfitLabel.setText("Total Profit on " + day[5] + "-" + month[5] + "-" + year[5] + " was Rs." + sum[5]);
        }
        else
        {
            labelColor(day6ProfitLabel,2);
            day6ProfitLabel.setText("Total Loss on " + day[5] + "-" + month[5] + "-" + year[5] + " was Rs." + (sum[5]*-1));
        }
        if(sum[6]>=0) {
            if(sum[6]==0)
                labelColor(day7ProfitLabel,1);
            else
                labelColor(day7ProfitLabel,3);
            day7ProfitLabel.setText("Total Profit on " + day[6] + "-" + month[6] + "-" + year[6] + " was Rs." + sum[6]);
        }
        else
        {
            labelColor(day7ProfitLabel,2);
            day7ProfitLabel.setText("Total Loss on " + day[6] + "-" + month[6] + "-" + year[6] + " was Rs." + (sum[6]*-1));
        }
    }

    public void labelColor(Label l1, int value)
    {
        if(value==1)
          l1.setTextFill(Color.WHITE);
        else if(value==2)
            l1.setTextFill(Color.RED);
        else
            l1.setTextFill(Color.LIGHTGREEN);
    }

    public void addingToolTip()
    {
        profitTableView.setRowFactory(tv -> new TableRow<ProfitLoss>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(ProfitLoss profitLoss, boolean empty) {
                super.updateItem(profitLoss, empty);
                if (profitLoss == null) {
                    setTooltip(null);
                }
                else {
                    String tooltipText="";
                    if (profitLoss.getTotalPurchase()== 0 && profitLoss.getTotalSale()==0) {
                        tooltip.setText("Sales- NA\nPurchase-NA");
                        setTooltip(tooltip);
                    }
                    else
                    {
                        float value=0;
                        try {
                            Connection dbConnection = JDBC.databaseConnect();
                            Statement sqlStatement = dbConnection.createStatement();
                            String date=profitLoss.getDate();
                            if(profitLoss.getTotalSale()!=0) {
                                ResultSet saleCardResultSet = sqlStatement.executeQuery("SELECT SUM(total_amount) FROM retailer_sale_bill where user_access_id='" + user_id + "' AND date='" + date + "' AND mode='Debit Card' OR  mode='Credit Card'");
                                while (saleCardResultSet.next()) {
                                    value = saleCardResultSet.getFloat(1);
                                }
                                tooltipText = tooltipText + "Sales-\nThrough Card - " + value;
                                ResultSet saleCashResultSet = sqlStatement.executeQuery("SELECT SUM(total_amount) FROM retailer_sale_bill where mode='Cash' AND date='" + date + "' AND  user_access_id='" + user_id + "'");
                                while (saleCashResultSet.next()) {
                                    value = saleCashResultSet.getFloat(1);
                                }
                                tooltipText = tooltipText + "   Through Cash- " + value;
                            }
                            else
                            tooltipText=tooltipText+"Sales- NA";
                            if(profitLoss.getTotalPurchase()!=0)
                            {
                                ResultSet purchaseCardResultSet = sqlStatement.executeQuery("SELECT SUM(total_amount) FROM retailer_purchase_bill where user_access_id='" + user_id + "' AND date='" + date + "' AND mode='Debit Card' OR  mode='Credit Card'");
                                while (purchaseCardResultSet.next()) {
                                    value = purchaseCardResultSet.getFloat(1);
                                }
                                tooltipText = tooltipText + "\n\nPurchase-\nThrough Card - " + value;
                                ResultSet purchaseCashResultSet = sqlStatement.executeQuery("SELECT SUM(total_amount) FROM retailer_purchase_bill where mode='Cash' AND date='" + date + "' AND  user_access_id='" + user_id + "'");
                                while (purchaseCashResultSet.next()) {
                                    value = purchaseCashResultSet.getFloat(1);
                                }
                                tooltipText = tooltipText + "   Through Cash- " + value;
                            }
                            else
                                tooltipText=tooltipText+"\n\nPurchase- NA";
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
        String date=profitDatePicker.getValue().toString();
        profitParticularDayList=FXCollections.observableArrayList();
        float totalSale=0,totalPurchase=0,profit=0.0f;
        try
        {
            Connection dbConnection = JDBC.databaseConnect();
            Statement sqlStatement = dbConnection.createStatement();
            ResultSet totalSaleResultSet=sqlStatement.executeQuery("SELECT SUM(total_amount) from retailer_sale_bill where date='"+date+"' AND user_access_id='" + user_id + "'");
            while (totalSaleResultSet.next())
            {
                totalSale=totalSaleResultSet.getFloat("sum");
            }
            ResultSet totalPurchaseResultSet=sqlStatement.executeQuery("SELECT SUM(total_amount) from retailer_purchase_bill where date='"+date+"' AND user_access_id='" + user_id + "'");
            while (totalPurchaseResultSet.next())
            {
                totalPurchase=totalPurchaseResultSet.getFloat("sum");
            }
            profit=totalSale-totalPurchase;
            profitParticularDayList.add(new ProfitLoss(date,totalSale,totalPurchase,profit));
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

        profitTableView.setItems(profitParticularDayList);
        if(profit<0) {
            dateSelectedLabel.setText("Total Loss on " + date + " " + tense + " Rs." + profit);
            dateSelectedLabel.setTextFill(Color.RED);
        }
        else if(profit==0)
        {
            dateSelectedLabel.setText("No Profit or Loss on " + date);
            dateSelectedLabel.setTextFill(Color.WHITE);
        }
        else
        {
            dateSelectedLabel.setText("Total Profit on " + date + " " + tense + " Rs." + profit);
            dateSelectedLabel.setTextFill(Color.LIGHTGREEN);
        }
        datePickedHBox.setVisible(true);

    }

    public void graphGenerator()
    {
        profitLineChart.getData().clear();
        /*double max=totalSale[0],min=totalPurchase[0];
        for(int i=1;i<7;i++)
        {
         if(max<totalSale[i])
             max=totalSale[i];
         if(min<totalPurchase[i])
             min=totalPurchase[i];
        }
        profitAxis.setAutoRanging(false);
        profitAxis.setUpperBound(max+100);
        profitAxis.setLowerBound(-(min+100));
        profitAxis.setTickUnit(200);*/
        String date;
        XYChart.Series profit=new XYChart.Series();
        XYChart.Series sale=new XYChart.Series();
        XYChart.Series purchase=new XYChart.Series();
        for(int i=6;i>=0;i--)
        {
            date=day[i]+"-"+month[i]+"-"+year[i];
            profit.getData().add(new XYChart.Data(date,sum[i]));
            sale.getData().add(new XYChart.Data(date,totalSale[i]));
            purchase.getData().add(new XYChart.Data(date,(totalPurchase[i]*-1)));
        }
        profit.setName("Profit");
        sale.setName("Sale");
        purchase.setName("Purchase");
        profitLineChart.getData().addAll(purchase,sale,profit);
    }

    public void init(MainFeaturesTabSceneController mainFeaturesTabSceneController) {
        this.mainFeaturesTabSceneController = mainFeaturesTabSceneController;
    }
}
