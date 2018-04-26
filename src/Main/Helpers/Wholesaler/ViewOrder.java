package Main.Helpers.Wholesaler;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ViewOrder {
    SimpleStringProperty rname,status,date,medicineName;
    SimpleIntegerProperty quantity,snumber,id,serial;
    public ViewOrder(int snumber,String date,String rname,String status,int id)
    {
        this.date=new SimpleStringProperty(date);
        this.rname=new SimpleStringProperty(rname);
        this.status=new SimpleStringProperty(status);
        this.snumber=new SimpleIntegerProperty(snumber);
        this.id=new SimpleIntegerProperty(id);
    }
    public ViewOrder(int serial,String medicineName,int quantity)
    {
        this.serial=new SimpleIntegerProperty(serial);
        this.medicineName=new SimpleStringProperty(medicineName);
        this.quantity=new SimpleIntegerProperty(quantity);
    }

    public void setRname(String rname) {
        this.rname.set(rname);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setMedicineName(String medicineName) {
        this.medicineName.set(medicineName);
    }

    public String getDate() {
        return date.get();
    }

    public String getStatus() {
        return status.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public String getMedicineName() {
        return medicineName.get();
    }

    public String getRname() {
        return rname.get();
    }

    public void setSnumber(int snumber) {
        this.snumber.set(snumber);
    }

    public int getSnumber() {
        return snumber.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getId() {
        return id.get();
    }

    public void setSerial(int serial) {
        this.serial.set(serial);
    }

    public int getSerial() {
        return serial.get();
    }
}
