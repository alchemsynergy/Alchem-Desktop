package Main.Extras;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Billing {
    private final SimpleStringProperty billItem;
    private final SimpleStringProperty billBatch;
    private final SimpleIntegerProperty billQuantity;
    private final SimpleStringProperty billFree;
    private final SimpleFloatProperty billRate;
    private final SimpleFloatProperty billAmount;

    public Billing(String billItem, String billBatch, int billQuantity, String billFree, float billRate, float billAmount) {
        this.billItem = new SimpleStringProperty(billItem);
        this.billBatch = new SimpleStringProperty(billBatch);
        this.billQuantity = new SimpleIntegerProperty(billQuantity);
        this.billFree = new SimpleStringProperty(billFree);
        this.billRate = new SimpleFloatProperty(billRate);
        this.billAmount = new SimpleFloatProperty(billAmount);
    }

    public String getBillItem() {
        return billItem.get();
    }

    public String getBillBatch() {
        return billBatch.get();
    }

    public Integer getBillQuantity() {
        return billQuantity.get();
    }

    public String getBillFree() {
        return billFree.get();
    }

    public Float getBillRate() {
        return billRate.get();
    }

    public Float getBillAmount() {
        return billAmount.get();
    }

}