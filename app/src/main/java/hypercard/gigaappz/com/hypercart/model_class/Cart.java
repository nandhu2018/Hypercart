package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 15-Nov-18.
 */

public class Cart {
    public String name;
    public String company;
    public String qty;
    public String price;



    public String getName() {
        return name;
    }
    public String getCompany() {
        return company;
    }
    public String getQuantity() {
        return qty;
    }
    public String getPrice() {
        return price;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public void setQuantity(String quantity) {
        this.qty = quantity;
    }
    public void setPrice(String price) {
        this.price = price;
    }


}
