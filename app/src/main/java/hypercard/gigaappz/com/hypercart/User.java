package hypercard.gigaappz.com.hypercart;

/**
 * Created by DELL on 27-Sep-18.
 */

public class User {

    public String mobile;
    public String password;
    public String name;
    public String place;
    public String acctype;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String mobile, String password,String name,String place,String acctype) {
        this.mobile = mobile;
        this.password = password;
        this.name = name;
        this.place = place;
        this.acctype=acctype;
    }
}
