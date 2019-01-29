package hypercard.gigaappz.com.hypercart;

/**
 * Created by DELL on 27-Sep-18.
 */

public class User {

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
    }

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
