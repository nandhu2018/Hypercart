package hypercard.gigaappz.com.hypercart.model_class;

/**
 * Created by DELL on 21-Feb-19.
 */

public class Wallet {
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWalletbalance() {
        return walletbalance;
    }

    public void setWalletbalance(String walletbalance) {
        this.walletbalance = walletbalance;
    }

    public String user;
    public String walletbalance;
    public Wallet(){

    }
    public Wallet(String user,String wallet){
        this.user=user;
        this.walletbalance=wallet;
    }
}
