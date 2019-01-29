package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 27-Sep-18.
 */

public class Product {

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String barcode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String name;
    public String company;
    public String price;
    public String qty;
    public String shop;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Product() {
    }

    public Product(String barcode, String name, String company, String price,String qty,String shop) {
        this.barcode = barcode;
        this.name = name;
        this.company = company;
        this.price = price;
        this.qty=qty;
        this.shop=shop;
    }


}
