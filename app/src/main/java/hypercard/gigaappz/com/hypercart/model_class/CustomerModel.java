package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 06-Nov-18.
 */

public class CustomerModel {
    private String customerName;
    private String customerMobile;
    private boolean isEnabled;

    public CustomerModel() {
    }

    public CustomerModel(String customerName, String customerMobile, boolean isEnabled) {
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.isEnabled = isEnabled;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
