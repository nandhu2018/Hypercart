package hypercard.gigaappz.com.hypercart.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;
import hypercard.gigaappz.com.hypercart.shop.Shoppurchaseadapter;

public class PurchasedDetailsUser extends AppCompatActivity {
    RecyclerView recyclerview;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    ImageView back;
    ProgressBar progressBar;
    List<String> invoicelist = new ArrayList<>();
    List<SalesDetails> salesDetailsList = new ArrayList<>();
    ;
    Shoppurchaseadapter1 shoppurchaseadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_details);

        recyclerview = (RecyclerView) findViewById(R.id.listview);
        back = (ImageView) findViewById(R.id.back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerview.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("salesuser").child(getIntent().getStringExtra("mobile"));
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    SalesDetails sales = dataSnapshot1.getValue(SalesDetails.class);
                    SalesDetails salesDetails = new SalesDetails();
                    salesDetails.setMobile(sales.getMobile());
                    salesDetails.setBillDate(sales.getBillDate());
                    salesDetails.setBillNumber(sales.getBillNumber());
                    salesDetails.setTotalCost(sales.getTotalCost());
                    salesDetails.setShopname(sales.getShopname());
                    salesDetailsList.add(salesDetails);
                }
                shoppurchaseadapter = new Shoppurchaseadapter1(salesDetailsList);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                recyclerview.setLayoutManager(mLayoutManager);
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(shoppurchaseadapter);
                shoppurchaseadapter.notifyDataSetChanged();
                recyclerview.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timeStamp));
            String date = sdf.format(netDate);
            return date;
        } catch (Exception ex) {
            return "xx";
        }
    }

}
