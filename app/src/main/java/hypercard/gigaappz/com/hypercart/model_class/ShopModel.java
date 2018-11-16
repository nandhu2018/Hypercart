package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 06-Nov-18.
 */

public class ShopModel {
    private String shopName;
    private String shopEmail;
    private boolean isEnabled;

    public ShopModel() {
    }

    public ShopModel(String shopName, String shopEmail, boolean isEnabled) {
        this.shopName = shopName;
        this.shopEmail = shopEmail;
        this.isEnabled = isEnabled;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
