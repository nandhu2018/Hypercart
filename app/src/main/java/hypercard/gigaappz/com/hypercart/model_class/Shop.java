package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 08-Nov-18.
 */

public class Shop {
    public String mobile;
    public String name;
    public String place;
    public String contact;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Shop() {
    }

    public Shop(String mobile,String name,String place,String contact) {
        this.mobile = mobile;
        this.contact = contact;
        this.name = name;
        this.place = place;
    }
}
