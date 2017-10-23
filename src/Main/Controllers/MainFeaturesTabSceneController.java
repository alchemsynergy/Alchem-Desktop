package Main.Controllers;


import Main.Controllers.Retailers.ViewSaleController;
import javafx.fxml.FXML;

public class MainFeaturesTabSceneController {

    @FXML
    ViewSaleController viewSaleController;

    @FXML
    AddSaleController addSaleController;

    public void initialize()
    {
        viewSaleController.init(this);
        addSaleController.init(this);
    }

    public void setSaleLabel()
    {
        viewSaleController.todaySaleLabel.setText("Today's Total Sale is Rs."+ViewSaleController.sum[0]);
        viewSaleController.yesterdaySaleLabel.setText("Yesterday's Total Sale was Rs."+ViewSaleController.sum[1]);
        viewSaleController.day3SaleLabel.setText("Total Sale on "+ViewSaleController.day[2] +"-"+ViewSaleController.month[2]+"-"+ViewSaleController.year[2]+" was Rs."+ViewSaleController.sum[2]);
        viewSaleController.day4SaleLabel.setText("Total Sale on "+ViewSaleController.day[3] +"-"+ViewSaleController.month[3]+"-"+ViewSaleController.year[3]+" was Rs."+ViewSaleController.sum[3]);
        viewSaleController.day5SaleLabel.setText("Total Sale on "+ViewSaleController.day[4] +"-"+ViewSaleController.month[4]+"-"+ViewSaleController.year[4]+" was Rs."+ViewSaleController.sum[4]);
        viewSaleController.day6SaleLabel.setText("Total Sale on "+ViewSaleController.day[5] +"-"+ViewSaleController.month[5]+"-"+ViewSaleController.year[5]+" was Rs."+ViewSaleController.sum[5]);
        viewSaleController.day7SaleLabel.setText("Total Sale on "+ViewSaleController.day[6] +"-"+ViewSaleController.month[6]+"-"+ViewSaleController.year[6]+" was Rs."+ViewSaleController.sum[6]);
    }
}
