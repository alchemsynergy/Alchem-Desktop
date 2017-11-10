package Main.Helpers;


import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Add_purchase {
    private final SimpleStringProperty purchaseHsn;
    private final SimpleStringProperty purchaseItem;
    private final SimpleStringProperty purchaseBatch;
    private final SimpleIntegerProperty purchaseQuantity;
    private final SimpleFloatProperty purchaseCost;
    private final SimpleFloatProperty purchaseMrp;
    private final SimpleStringProperty purchaseSalt;
    private final SimpleStringProperty purchaseCompany;
    private final SimpleStringProperty purchaseType;
    private final SimpleStringProperty purchaseExpiry;
    private final SimpleStringProperty purchaseMfd;
    private final SimpleIntegerProperty purchaseSgst;
    private final SimpleIntegerProperty purchaseCgst;
    private final SimpleIntegerProperty purchaseIgst;
    private final SimpleIntegerProperty purchaseIpunit;
    private final SimpleIntegerProperty purchasePpitem;

    public Add_purchase(String purchaseHsn, String purchaseItem, String purchaseBatch, Integer purchaseQuantity, Float purchaseCost, Float purchaseMrp, String purchaseSalt, String purchaseCompany, String purchaseType, String purchaseExpiry, String purchaseMfd, Integer purchaseSgst, Integer purchaseCgst, Integer purchaseIgst, Integer purchaseIpunit, Integer purchasePpitem) {
        this.purchaseHsn = new SimpleStringProperty(purchaseHsn);
        this.purchaseItem = new SimpleStringProperty(purchaseItem);
        this.purchaseBatch = new SimpleStringProperty(purchaseBatch);
        this.purchaseQuantity = new SimpleIntegerProperty(purchaseQuantity);
        this.purchaseCost = new SimpleFloatProperty(purchaseCost);
        this.purchaseMrp = new SimpleFloatProperty(purchaseMrp);
        this.purchaseSalt = new SimpleStringProperty(purchaseSalt);
        this.purchaseCompany = new SimpleStringProperty(purchaseCompany);
        this.purchaseType = new SimpleStringProperty(purchaseType);
        this.purchaseExpiry = new SimpleStringProperty(purchaseExpiry);
        this.purchaseMfd = new SimpleStringProperty(purchaseMfd);
        this.purchaseSgst = new SimpleIntegerProperty(purchaseSgst);
        this.purchaseCgst = new SimpleIntegerProperty(purchaseCgst);
        this.purchaseIgst = new SimpleIntegerProperty(purchaseIgst);
        this.purchaseIpunit = new SimpleIntegerProperty(purchaseIpunit);
        this.purchasePpitem = new SimpleIntegerProperty(purchasePpitem);
    }

    public String getPurchaseHsn() {
        return purchaseHsn.get();
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

    public String getPurchaseSalt() {
        return purchaseSalt.get();
    }

    public String getPurchaseCompany() {
        return purchaseCompany.get();
    }

    public String getPurchaseType() {
        return purchaseType.get();
    }

    public String getPurchaseExpiry() {
        return purchaseExpiry.get();
    }

    public String getPurchaseMfd() {
        return purchaseMfd.get();
    }

    public int getPurchaseSgst() {
        return purchaseSgst.get();
    }

    public int getPurchaseCgst() {
        return purchaseCgst.get();
    }

    public int getPurchaseIgst() {
        return purchaseIgst.get();
    }

    public int getPurchaseIpunit() {
        return purchaseIpunit.get();
    }

    public int getPurchasePpitem() {
        return purchasePpitem.get();
    }

}
