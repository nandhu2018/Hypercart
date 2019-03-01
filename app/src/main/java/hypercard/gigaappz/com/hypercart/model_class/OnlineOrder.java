package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 08-Nov-18.
 */

public class OnlineOrder {
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String mobile;
    public String name;
    public String place;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;
    public String status;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public OnlineOrder() {
    }

    public OnlineOrder(String mobile, String name, String place, String time, String status) {
        this.mobile = mobile;
        this.time = time;
        this.name = name;
        this.place = place;
        this.status=status;
    }
}
