package hypercard.gigaappz.com.hypercart.login;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by DELL on 19-Nov-18.
 */

public class Hypercart extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
