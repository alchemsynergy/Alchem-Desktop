package Main.Helpers.Retailers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by VIPUL GOYAL on 4/17/2018.
 */
public class ViewOrder {
    SimpleIntegerProperty order,snumber,quantity,id;
    SimpleStringProperty wname,status,date,mname;
    public ViewOrder(int snumber,String mname,int quantity)
    {
        this.snumber=new SimpleIntegerProperty(snumber);
        this.mname=new SimpleStringProperty(mname);
        this.quantity=new SimpleIntegerProperty(quantity);
    }
    public ViewOrder(String date,int order,String wname,String status,int id)
    {
        this.date=new SimpleStringProperty(date);
        this.order=new SimpleIntegerProperty(order);
        this.wname=new SimpleStringProperty(wname);
        this.status=new SimpleStringProperty(status);
        this.id=new SimpleIntegerProperty(id);
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public void setOrder(int order) {
        this.order.set(order);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setWname(String wname) {
        this.wname.set(wname);
    }

    public int getOrder() {
        return order.get();
    }

    public String getWname() {
        return wname.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getDate() {
        return date.get();
    }

    public void setSnumber(int snumber) {
        this.snumber.set(snumber);
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public void setMname(String mname) {
        this.mname.set(mname);
    }

    public int getSnumber() {
        return snumber.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public String getMname() {
        return mname.get();
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }
}
