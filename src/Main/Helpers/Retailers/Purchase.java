package Main.Helpers.Retailers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchase {

    SimpleStringProperty date,wholesalerName,mode;
    SimpleLongProperty billNumber;
    SimpleFloatProperty amount;

    public Purchase(String date, Long billNumber, String wholesalerName, String mode, Float amount)
    {
        this.date=new SimpleStringProperty(date);
        this.wholesalerName=new SimpleStringProperty(wholesalerName);
        this.mode=new SimpleStringProperty(mode);
        this.billNumber=new SimpleLongProperty(billNumber);
        this.amount=new SimpleFloatProperty(amount);
    }

    public long getBillNumber() {
        return billNumber.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getWholesalerName() {
        return wholesalerName.get();
    }

    public String getMode() {
        return mode.get();
    }

    public float getAmount() {
        return amount.get();
    }

    public void setMode(String mode) {
        this.mode.set(mode);
    }

    public void setBillNumber(long billNumber) {
        this.billNumber.set(billNumber);
    }

    public void setAmount(float amount) {
        this.amount.set(amount);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setWholesalerName(String wholesalerName) {
        this.wholesalerName.set(wholesalerName);
    }
}
