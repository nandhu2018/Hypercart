package hypercard.gigaappz.com.hypercart.payment;

/**
 * Created by DELL on 17-Nov-18.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.Product;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;
import hypercard.gigaappz.com.hypercart.model_class.Wallet;
import hypercard.gigaappz.com.hypercart.user.MainScreen1;
import hypercard.gigaappz.com.hypercart.user.WalletActivity;

public class WalletPaymentActivity extends Activity implements PaymentResultListener {
    private static final String TAG = WalletPaymentActivity.class.getSimpleName();
    TextView price,date;
    int qtyneed,qtyleft=0;
    private DatabaseReference walletref;
    private FirebaseDatabase mFirebaseInstance;
    ArrayList<String> barcodelist,qtylist;
    String walletbal="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
        //barcodelist = getIntent().getStringArrayListExtra("barcode");
        //qtylist = getIntent().getStringArrayListExtra("qtyleft");
        mFirebaseInstance = FirebaseDatabase.getInstance();
        walletref = mFirebaseInstance.getReference("wallet");
        walletref.child(getIntent().getStringExtra("mobile")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                walletbal=wallet.getWalletbalance();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // get reference to 'users' node



        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout
        Button button = (Button) findViewById(R.id.btn_pay);
        price = (TextView) findViewById(R.id.price);
        date = (TextView) findViewById(R.id.date);
        price.setText("₹"+getIntent().getStringExtra("price"));
        date.setText(getIntent().getStringExtra("date"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "HyperCart");
            options.put("description", "Add Money To Wallet");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(getIntent().getStringExtra("price"))*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@hypercart.com");
            preFill.put("contact", getIntent().getStringExtra("mobile"));

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Wallet wal=new Wallet(getIntent().getStringExtra("mobile"),String.valueOf(Integer.parseInt(getIntent().getStringExtra("price"))+Integer.parseInt(walletbal)));
            walletref.child(getIntent().getStringExtra("mobile")).setValue(wal);
            Toast.makeText(this, "Money Added ", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
            startActivity(new Intent(WalletPaymentActivity.this,WalletActivity.class));
            finish();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WalletPaymentActivity.this,WalletActivity.class));
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
            startActivity(new Intent(WalletPaymentActivity.this,WalletActivity.class));
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(WalletPaymentActivity.this,WalletActivity.class));
        finish();
    }
}
