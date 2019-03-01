package hypercard.gigaappz.com.hypercart.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.customfonts.MyTextView;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;
import hypercard.gigaappz.com.hypercart.model_class.Wallet;
import hypercard.gigaappz.com.hypercart.payment.PaymentActivity;
import hypercard.gigaappz.com.hypercart.payment.WalletPaymentActivity;

public class WalletActivity extends AppCompatActivity {
    Button addmoney;
    MyTextView mobile,amount,totalpurchase,totalshop;
    DatabaseReference walletref,purchaseref;
    FirebaseDatabase instance;
    ArrayList<String> shopcount=new ArrayList<>();
    HashSet<String> hashSet = new HashSet<String>();
    ImageView back;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        addmoney=(Button)findViewById(R.id.addmoney);
        mobile=(MyTextView) findViewById(R.id.mobile);
        amount=(MyTextView) findViewById(R.id.amount);
        totalpurchase=(MyTextView) findViewById(R.id.totalpurchase);
        totalshop=(MyTextView) findViewById(R.id.totalshops);
        back=(ImageView) findViewById(R.id.back);
        sharedPreferences= getSharedPreferences("logged", Context.MODE_PRIVATE);
        instance = FirebaseDatabase.getInstance();

        walletref = instance.getReference("wallet");
        purchaseref = instance.getReference("salesuser");
        walletref.child(sharedPreferences.getString("mobile", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                amount.setText(wallet.getWalletbalance());
                mobile.setText(wallet.getUser());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        purchaseref.child(sharedPreferences.getString("mobile", "")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Toast.makeText(WalletActivity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                totalpurchase.setText(""+dataSnapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SalesDetails sales = dataSnapshot1.getValue(SalesDetails.class);
                    shopcount.add(sales.getShopname());
                    hashSet.addAll(shopcount);
                    shopcount.clear();
                    shopcount.addAll(hashSet);



                }
                totalshop.setText(""+shopcount.size());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            addwallet();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void addwallet(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_wallet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        final EditText walletamount = dialog.findViewById(R.id.walletamount);
        Button okButton = (Button) dialog.findViewById(R.id.addtowallet);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletamount.getText().toString().isEmpty()|Integer.parseInt(walletamount.getText().toString())<1) {
                    walletamount.setError("Enter Valid Amount");
                }else {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    Intent intent = new Intent(WalletActivity.this, WalletPaymentActivity.class);
                    intent.putExtra("mobile", sharedPreferences.getString("mobile", ""));
                    intent.putExtra("date", formattedDate);
                    intent.putExtra("price", walletamount.getText().toString());
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
