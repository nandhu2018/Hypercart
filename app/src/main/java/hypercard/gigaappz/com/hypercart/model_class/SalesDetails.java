package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 06-Nov-18.
 */

public class SalesDetails {
    private int billNumber;
    private double totalCost;
    private String billDate;

    public SalesDetails() {
    }

    public SalesDetails(int billNumber, double totalCost, String billDate) {
        this.billNumber = billNumber;
        this.totalCost = totalCost;
        this.billDate = billDate;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }
}
