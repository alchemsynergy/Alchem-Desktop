package Main.Extras;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Medicine {


    SimpleIntegerProperty code,quantity,sgst,cgst,igst;
    SimpleStringProperty name,salt,company,type,batch,hsn,expiry;
    SimpleFloatProperty mrp,cost;



    public Medicine(int code, String name, String salt, String company, String type, String hsn, String batch, String expiry, int quantity, float mrp, float cost, int sgst, int cgst, int igst)
    {
        this.code=new SimpleIntegerProperty(code);
        this.name=new SimpleStringProperty(name);
        this.salt=new SimpleStringProperty(salt);
        this.company=new SimpleStringProperty(company);
        this.type=new SimpleStringProperty(type);
        this.hsn=new SimpleStringProperty(hsn);
        this.batch=new SimpleStringProperty(batch);
        this.expiry=new SimpleStringProperty(expiry);
        this.quantity=new SimpleIntegerProperty(quantity);
        this.mrp=new SimpleFloatProperty(mrp);
        this.cost=new SimpleFloatProperty(cost);
        this.sgst=new SimpleIntegerProperty(sgst);
        this.cgst=new SimpleIntegerProperty(cgst);
        this.igst=new SimpleIntegerProperty(igst);
    }


    //Setters
    public void setCode(SimpleIntegerProperty code)
    {
        this.code=code;
    }
    public void setQuantity(SimpleIntegerProperty quantity)
    {
        this.quantity=quantity;
    }
    public void setSgst(SimpleIntegerProperty sgst)
    {
        this.sgst=sgst;
    }
    public void setCgst(SimpleIntegerProperty cgst)
    {
        this.cgst=cgst;
    }
    public void setIgst(SimpleIntegerProperty igst)
    {
        this.igst=igst;
    }
    public void setName(SimpleStringProperty name)
    {
        this.name=name;
    }
    public void setHsn(SimpleStringProperty hsn)
    {
        this.hsn=hsn;
    }
    public void setBatch(SimpleStringProperty batch)
    {
        this.batch=batch;
    }
    public void setExpiry(SimpleStringProperty expiry)
    {
        this.expiry=expiry;
    }
    public void setSalt(SimpleStringProperty salt)
    {
        this.salt=salt;
    }
    public void setCompany(SimpleStringProperty company)
    {
        this.company=company;
    }
    public void setType(SimpleStringProperty type)
    {
        this.type=type;
    }
    public void setMrp(SimpleFloatProperty mrp)
    {
        this.mrp=mrp;
    }
    public void setCost(SimpleFloatProperty cost)
    {
        this.cost=cost;
    }


    //Getters
    public int getCode() {
        return code.get();
    }

    public float getCost() {
        return cost.get();
    }

    public int getCgst() {
        return cgst.get();
    }

    public int getIgst() {
        return igst.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public int getSgst() {
        return sgst.get();
    }

    public String getName() {
        return name.get();
    }

    public float getMrp() {
        return mrp.get();
    }

    public String getBatch() {
        return batch.get();
    }

    public String getCompany() {
        return company.get();
    }

    public String getExpiry() {
        return expiry.get();
    }

    public String getHsn() {
        return hsn.get();
    }

    public String getSalt() {
        return salt.get();
    }

    public String getType() {
        return type.get();
    }
}

