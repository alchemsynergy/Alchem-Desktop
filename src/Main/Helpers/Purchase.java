package Main.Helpers;


import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchase {
    private final SimpleStringProperty purchaseItem;
    private final SimpleStringProperty purchaseBatch;
    private final SimpleIntegerProperty purchaseQuantity;
    private final SimpleFloatProperty purchaseCost;
    private final SimpleFloatProperty purchaseMrp;

    public Purchase(String purchaseItem, String purchaseBatch, Integer purchaseQuantity, Float purchaseCost, Float purchaseMrp) {
        this.purchaseItem = new SimpleStringProperty(purchaseItem);
        this.purchaseBatch = new SimpleStringProperty(purchaseBatch);
        this.purchaseQuantity = new SimpleIntegerProperty(purchaseQuantity);
        this.purchaseCost = new SimpleFloatProperty(purchaseCost);
        this.purchaseMrp = new SimpleFloatProperty(purchaseMrp);
    }

    public String getPurchaseItem() {
        return purchaseItem.get();
    }

    public String getPurchaseBatch() {
        return purchaseBatch.get();
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity.get();
    }

    public float getPurchaseCost() {
        return purchaseCost.get();
    }

    public float getPurchaseMrp() {
        return purchaseMrp.get();
    }

}
