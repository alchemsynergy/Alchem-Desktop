package Main.Helpers.Retailers;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sale {
    SimpleStringProperty date, patientName, doctorName, companyName, mode;
    SimpleLongProperty billNumber;
    SimpleFloatProperty amount;

    public Sale(String date, Long billNumber, String patientName, String doctorName, String companyName, String mode, Float amount) {
        this.date = new SimpleStringProperty(date);
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.companyName = new SimpleStringProperty(companyName);
        this.mode = new SimpleStringProperty(mode);
        this.billNumber = new SimpleLongProperty(billNumber);
        this.amount = new SimpleFloatProperty(amount);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getDoctorName() {
        return doctorName.get();
    }

    public void setDoctorName(String doctorName) {
        this.doctorName.set(doctorName);
    }

    public String getPatientName() {
        return patientName.get();
    }

    public void setPatientName(String patientName) {
        this.patientName.set(patientName);
    }

    public String getMode() {
        return mode.get();
    }

    public void setMode(String mode) {
        this.mode.set(mode);
    }

    public long getBillNumber() {
        return billNumber.get();
    }

    public void setBillNumber(long billNumber) {
        this.billNumber.set(billNumber);
    }

    public float getAmount() {
        return amount.get();
    }

    public void setAmount(float amount) {
        this.amount.set(amount);
    }
}
