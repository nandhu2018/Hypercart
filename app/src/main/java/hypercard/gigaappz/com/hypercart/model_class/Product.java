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
