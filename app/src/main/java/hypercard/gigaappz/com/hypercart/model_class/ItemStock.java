package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 06-Nov-18.
 */

public class ItemStock {
    private String item;
    private String company;
    private int quantity;
    private double mrp;
    private double price;

    public ItemStock() {
    }

    public ItemStock(String item, String company, int quantity, double mrp, double price) {
        this.item = item;
        this.company = company;
        this.quantity = quantity;
        this.mrp = mrp;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
