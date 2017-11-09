package Main.Controllers;


import Main.Controllers.Retailers.ProfitLossController;
import Main.Controllers.Retailers.ViewSaleController;
import Main.Helpers.Retailers.ProfitLoss;
import javafx.fxml.FXML;

public class MainFeaturesTabSceneController {

    @FXML
    ViewSaleController viewSaleController;

    @FXML
    AddSaleController addSaleController;

    @FXML
    ProfitLossController profitLossController;

    public void initialize() {
        viewSaleController.init(this);
        addSaleController.init(this);
        profitLossController.init(this);
    }

    public void setSaleLabel() {
        viewSaleController.todaySaleLabel.setText("Today's Total Sale is Rs." + ViewSaleController.sum[0]);
        viewSaleController.yesterdaySaleLabel.setText("Yesterday's Total Sale was Rs." + ViewSaleController.sum[1]);
        viewSaleController.day3SaleLabel.setText("Total Sale on " + ViewSaleController.day[2] + "-" + ViewSaleController.month[2] + "-" + ViewSaleController.year[2] + " was Rs." + ViewSaleController.sum[2]);
        viewSaleController.day4SaleLabel.setText("Total Sale on " + ViewSaleController.day[3] + "-" + ViewSaleController.month[3] + "-" + ViewSaleController.year[3] + " was Rs." + ViewSaleController.sum[3]);
        viewSaleController.day5SaleLabel.setText("Total Sale on " + ViewSaleController.day[4] + "-" + ViewSaleController.month[4] + "-" + ViewSaleController.year[4] + " was Rs." + ViewSaleController.sum[4]);
        viewSaleController.day6SaleLabel.setText("Total Sale on " + ViewSaleController.day[5] + "-" + ViewSaleController.month[5] + "-" + ViewSaleController.year[5] + " was Rs." + ViewSaleController.sum[5]);
        viewSaleController.day7SaleLabel.setText("Total Sale on " + ViewSaleController.day[6] + "-" + ViewSaleController.month[6] + "-" + ViewSaleController.year[6] + " was Rs." + ViewSaleController.sum[6]);
    }

    public void setProfitLabel()
    {
        if(ProfitLossController.sum[0]>=0) {
            if(ProfitLossController.sum[0]==0)
                profitLossController.labelColor(profitLossController.todayProfitLabel,1);
            else
                profitLossController.labelColor(profitLossController.todayProfitLabel,3);
            profitLossController.todayProfitLabel.setText("Today's Total Profit is Rs." + ProfitLossController.sum[0]);
        }
        else
        {
            profitLossController.labelColor(profitLossController.todayProfitLabel,2);
            profitLossController.todayProfitLabel.setText("Today's Total Loss is Rs." + (ProfitLossController.sum[0]*-1));
        }
        if(ProfitLossController.sum[1]>=0) {
            if(ProfitLossController.sum[1]==0)
                profitLossController.labelColor(profitLossController.yesterdayProfitLabel,1);
            else
                profitLossController.labelColor(profitLossController.yesterdayProfitLabel,3);
            profitLossController.yesterdayProfitLabel.setText("Yesterday's Total Profit was Rs." + ProfitLossController.sum[1]);
        }else
        {
            profitLossController.labelColor(profitLossController.yesterdayProfitLabel,2);
            profitLossController.yesterdayProfitLabel.setText("Yesterday's Total Loss was Rs." + (ProfitLossController.sum[1]*-1));
        }
        if(ProfitLossController.sum[2]>=0) {
            if(ProfitLossController.sum[2]==0)
                profitLossController.labelColor(profitLossController.day3ProfitLabel,1);
            else
                profitLossController.labelColor(profitLossController.day3ProfitLabel,3);
            profitLossController.day3ProfitLabel.setText("Total Profit on " + ProfitLossController.day[2] + "-" + ProfitLossController.month[2] + "-" + ProfitLossController.year[2] + " was Rs." + ProfitLossController.sum[2]);
        }
        else
        {
            profitLossController.labelColor(profitLossController.day3ProfitLabel,2);
            profitLossController.day3ProfitLabel.setText("Total Loss on " + ProfitLossController.day[2] + "-" + ProfitLossController.month[2] + "-" + ProfitLossController.year[2] + " was Rs." + (ProfitLossController.sum[2]*-1));
        }
        if(ProfitLossController.sum[3]>=0) {
            if(ProfitLossController.sum[3]==0)
                profitLossController.labelColor(profitLossController.day4ProfitLabel,1);
            else
                profitLossController.labelColor(profitLossController.day4ProfitLabel,3);
            profitLossController.day4ProfitLabel.setText("Total Profit on " + ProfitLossController.day[3] + "-" +ProfitLossController. month[3] + "-" + ProfitLossController.year[3] + " was Rs." + ProfitLossController.sum[3]);
        }
        else
        {
            profitLossController.labelColor(profitLossController.day4ProfitLabel,2);
            profitLossController.day4ProfitLabel.setText("Total Loss on " + ProfitLossController.day[3] + "-" + ProfitLossController.month[3] + "-" + ProfitLossController.year[3] + " was Rs." + (ProfitLossController.sum[3]*-1));
        }
        if(ProfitLossController.sum[4]>=0) {
            if(ProfitLossController.sum[4]==0)
                profitLossController.labelColor(profitLossController.day5ProfitLabel,1);
            else
                profitLossController.labelColor(profitLossController.day5ProfitLabel,3);
            profitLossController.day5ProfitLabel.setText("Total Profit on " + ProfitLossController.day[4] + "-" + ProfitLossController.month[4] + "-" + ProfitLossController.year[4] + " was Rs." + ProfitLossController.sum[4]);
        }
        else
        {
            profitLossController.labelColor(profitLossController.day5ProfitLabel,2);
            profitLossController.day5ProfitLabel.setText("Total Loss on " + ProfitLossController.day[4] + "-" + ProfitLossController.month[4] + "-" + ProfitLossController.year[4] + " was Rs." + (ProfitLossController.sum[4]*-1));
        }
        if(ProfitLossController.sum[5]>=0) {
            if(ProfitLossController.sum[5]==0)
                profitLossController.labelColor(profitLossController.day6ProfitLabel,1);
            else
                profitLossController.labelColor(profitLossController.day6ProfitLabel,3);
            profitLossController.day6ProfitLabel.setText("Total Profit on " + ProfitLossController.day[5] + "-" + ProfitLossController.month[5] + "-" +ProfitLossController. year[5] + " was Rs." + ProfitLossController.sum[5]);
        }
        else
        {
            profitLossController.labelColor(profitLossController.day6ProfitLabel,2);
            profitLossController.day6ProfitLabel.setText("Total Loss on " + ProfitLossController.day[5] + "-" + ProfitLossController.month[5] + "-" + ProfitLossController.year[5] + " was Rs." + (ProfitLossController.sum[5]*-1));
        }
        if(ProfitLossController.sum[6]>=0) {
            if(ProfitLossController.sum[6]==0)
                profitLossController.labelColor(profitLossController.day7ProfitLabel,1);
            else
                profitLossController.labelColor(profitLossController.day7ProfitLabel,3);
            profitLossController.day7ProfitLabel.setText("Total Profit on " + ProfitLossController.day[6] + "-" +ProfitLossController.month[6] + "-" + ProfitLossController.year[6] + " was Rs." + ProfitLossController.sum[6]);
        }
        else
        {
            profitLossController.labelColor(profitLossController.day7ProfitLabel,2);
            profitLossController.day7ProfitLabel.setText("Total Loss on " + ProfitLossController.day[6] + "-" + ProfitLossController.month[6] + "-" + ProfitLossController.year[6] + " was Rs." + (ProfitLossController.sum[6]*-1));
        }
        profitLossController.graphGenerator();
    }

}
