package Main.Helpers.Retailers;


import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProfitLoss {
    SimpleStringProperty date;
    SimpleFloatProperty totalSale,totalPurchase,profit;

    public ProfitLoss(String date, Float totalSale, Float totalPurchase, Float profit)
    {
        this.date=new SimpleStringProperty(date);
        this.totalSale=new SimpleFloatProperty(totalSale);
        this.totalPurchase=new SimpleFloatProperty(totalPurchase);
        this.profit=new SimpleFloatProperty(profit);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setProfit(float profit) {
        this.profit.set(profit);
    }

    public void setTotalPurchase(float totalPurchase) {
        this.totalPurchase.set(totalPurchase);
    }

    public void setTotalSale(float totalSale) {
        this.totalSale.set(totalSale);
    }

    public String getDate() {
        return date.get();
    }

    public float getProfit() {
        return profit.get();
    }

    public float getTotalPurchase() {
        return totalPurchase.get();
    }

    public float getTotalSale() {
        return totalSale.get();
    }
}

