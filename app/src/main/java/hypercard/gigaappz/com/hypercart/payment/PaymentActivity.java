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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.Cart;
import hypercard.gigaappz.com.hypercart.model_class.OnlineOrder;
import hypercard.gigaappz.com.hypercart.model_class.Product;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;
import hypercard.gigaappz.com.hypercart.model_class.Wallet;
import hypercard.gigaappz.com.hypercart.shop.OnlineShopPendingAdapter;
import hypercard.gigaappz.com.hypercart.user.MainScreen1;
import hypercard.gigaappz.com.hypercart.user.ShopOnline;

public class PaymentActivity extends Activity implements PaymentResultListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    TextView price,date;
    int qtyneed,qtyleft=0;
    private DatabaseReference mFirebaseDatabase,mFirebaseDatabase1,mFirebaseDatabase2,mFirebaseDatabase3,remtemp,myref2,walletref;
    private FirebaseDatabase mFirebaseInstance;
    DatabaseReference ordershop,userdetailref;
    ArrayList<String> barcodelist,qtylist;
    List<Product> list = new ArrayList<Product>();
    int walletbal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
        barcodelist = getIntent().getStringArrayListExtra("barcode");
        qtylist = getIntent().getStringArrayListExtra("qtyleft");
        walletbal= Integer.parseInt(getIntent().getStringExtra("walletbal"));
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("salesshop").child(getIntent().getStringExtra("shop"));
        mFirebaseDatabase1 = mFirebaseInstance.getReference("salesuser").child(getIntent().getStringExtra("mobile"));

        /*walletref = mFirebaseInstance.getReference("wallet").child(getIntent().getStringExtra("mobile"));
        walletref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                walletbal=wallet.getWalletbalance();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout
        Button button = (Button) findViewById(R.id.btn_pay);
        price = (TextView) findViewById(R.id.price);
        date = (TextView) findViewById(R.id.date);
        if (walletbal>Integer.parseInt(getIntent().getStringExtra("price"))){
            price.setText("₹"+(walletbal-Integer.parseInt(getIntent().getStringExtra("price"))));
        }else {
            price.setText("₹"+(Integer.parseInt(getIntent().getStringExtra("price"))-walletbal));
        }
       // price.setText("₹"+getIntent().getStringExtra("price"));
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
            options.put("description", "Purchase products");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            if (walletbal>Integer.parseInt(getIntent().getStringExtra("price"))){
                options.put("amount", (walletbal-Integer.parseInt(getIntent().getStringExtra("price")))*100);
            }else {
                options.put("amount", (Integer.parseInt(getIntent().getStringExtra("price"))-walletbal)*100);
            }
            //options.put("amount", Integer.parseInt(getIntent().getStringExtra("price"))*100);

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
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            shopsale(razorpayPaymentID);
            usersale(razorpayPaymentID);
            userdetailref = mFirebaseInstance.getReference("userdetails").child(getIntent().getStringExtra("shop")).child(getIntent().getStringExtra("mobile"));
            Map<String, Object> updates = new HashMap<>();
            updates.put("status", "success");
            userdetailref.updateChildren(updates);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
            startActivity(new Intent(PaymentActivity.this,MainScreen1.class));
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
            startActivity(new Intent(PaymentActivity.this,MainScreen1.class));
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
            startActivity(new Intent(PaymentActivity.this,MainScreen1.class));
            finish();
        }
    }
    private void shopsale(String bill) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
       // Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        SalesDetails product = new SalesDetails(bill, getIntent().getStringExtra("price"),getIntent().getStringExtra("date"),getIntent().getStringExtra("mobile"),getIntent().getStringExtra("shopname"));

        mFirebaseDatabase.child(timeStamp).setValue(product);

        //startActivity(new Intent(AddProduct.this,Mainpage.class));

        //addUserChangeListener();
    }
    private void usersale(String bill) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth

        //Long tsLong = System.currentTimeMillis()/1000;
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
        final SalesDetails product = new SalesDetails(bill, getIntent().getStringExtra("price"),getIntent().getStringExtra("date"),getIntent().getStringExtra("shop"),getIntent().getStringExtra("shopname"));

        mFirebaseDatabase1.child(timeStamp).setValue(product);

        Toasty.success(PaymentActivity.this, "Payment Success", Toast.LENGTH_SHORT, true).show();
        //startActivity(new Intent(AddProduct.this,Mainpage.class));
        for ( int i=0;i<barcodelist.size();i++){
            final int pos=i;
            qtyleft=0;
            qtyneed=Integer.parseInt(qtylist.get(i));
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase3=mFirebaseInstance.getReference("product").child(getIntent().getStringExtra("shop")).child(barcodelist.get(i));
            mFirebaseDatabase3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Product product1=dataSnapshot.getValue(Product.class);
                        qtyleft=Integer.parseInt(product1.getQty());
                        int qty=qtyleft-qtyneed;
                        mFirebaseDatabase2 = mFirebaseInstance.getReference("product").child(getIntent().getStringExtra("shop")).child(barcodelist.get(pos));
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("qty", String.valueOf(qty));
                        mFirebaseDatabase2.updateChildren(updates);
                    }
                    else {
                        Toast.makeText(PaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PaymentActivity.this, "Data Error", Toast.LENGTH_SHORT).show();
                }

            });

            ordershop = mFirebaseInstance.getReference("onlineordershop").child(getIntent().getStringExtra("mobile")).child(getIntent().getStringExtra("shop")).child(barcodelist.get(i));
            myref2 = mFirebaseInstance.getReference("cart").child(getIntent().getStringExtra("mobile")).child(getIntent().getStringExtra("shop")).child(barcodelist.get(i));

            myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        moveRecord(myref2,ordershop);
                        myref2.removeValue();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PaymentActivity.this, ""+databaseError, Toast.LENGTH_SHORT).show();
                }
            });



        }

        startActivity(new Intent(PaymentActivity.this,MainScreen1.class));
        finish();

        //addUserChangeListener();
    }

    private void moveRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*Cart value = dataSnapshot.getValue(Cart.class);
                Product product=new Product();
                product.setBarcode("");
                product.setCompany(value.getCompany());
                product.setName(value.getName());
                product.setPrice(value.getPrice());
                product.setQty(value.getQuantity());
                product.setShop("");*/

                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d(TAG, "Success!");
                        } else {
                            Log.d(TAG, "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PaymentActivity.this,MainScreen1.class));
        finish();
    }
}
