package Main.Helpers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Billing_history {
    private final SimpleLongProperty searchBillNo;
    private final SimpleStringProperty searchDate;
    private final SimpleFloatProperty searchAmount;

    public Billing_history(Long searchBillNo, String searchDate, Float searchAmount) {
        this.searchBillNo = new SimpleLongProperty(searchBillNo);
        this.searchDate = new SimpleStringProperty(searchDate);
        this.searchAmount = new SimpleFloatProperty(searchAmount);
    }

    public long getSearchBillNo() {
        return searchBillNo.get();
    }

    public String getSearchDate() {
        return searchDate.get();
    }

    public float getSearchAmount() {
        return searchAmount.get();
    }

    public void setSearchAmount(float searchAmount) {this.searchAmount.set(searchAmount);}

    public void setSearchBillNo(long searchBillNo) {this.searchBillNo.set(searchBillNo);}

    public void setSearchDate(String searchDate) {this.searchDate.set(searchDate);}
}
