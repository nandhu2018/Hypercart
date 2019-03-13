package hypercard.gigaappz.com.hypercart.shop;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;

public class PurchasedDetails extends AppCompatActivity {
    RecyclerView recyclerview;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    ImageView back;
    ProgressBar progressBar;
    List<String> invoicelist = new ArrayList<>();
    List<SalesDetails> salesDetailsList = new ArrayList<>();
    ;
    Shoppurchaseadapter shoppurchaseadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_details);

        recyclerview = (RecyclerView) findViewById(R.id.listview);
        back=(ImageView)findViewById(R.id.back);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        recyclerview.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("salesshop").child(getIntent().getStringExtra("mobile"));
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                salesDetailsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    invoicelist.add(dataSnapshot1.getKey());
                    /*String timeStamptoday = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    long timestamp = Long.parseLong(dataSnapshot1.getKey()) * 1000L;
                    Toast.makeText(PurchasedDetails.this, "" + getDate(timestamp) + " " + (getDate(Long.parseLong(timeStamptoday) * 1000L)), Toast.LENGTH_SHORT).show();
                    if (getDate(Long.parseLong(timeStamptoday) * 1000L).equalsIgnoreCase(getDate(timestamp))) {
                        invoicelist.add(dataSnapshot1.getKey());
                        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {*/


                                    SalesDetails sales = dataSnapshot1.getValue(SalesDetails.class);
                                    SalesDetails salesDetails = new SalesDetails();
                                    salesDetails.setMobile(sales.getMobile());
                                    salesDetails.setBillDate(sales.getBillDate());
                                    salesDetails.setBillNumber(sales.getBillNumber());
                                    salesDetails.setTotalCost(sales.getTotalCost());
                                    salesDetails.setShopname(sales.getShopname());
                                    salesDetailsList.add(salesDetails);
                                }
                                shoppurchaseadapter = new Shoppurchaseadapter(salesDetailsList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                mLayoutManager.setReverseLayout(true);
                                mLayoutManager.setStackFromEnd(true);
                                recyclerview.setLayoutManager(mLayoutManager);
                                recyclerview.setItemAnimator(new DefaultItemAnimator());
                                recyclerview.setAdapter(shoppurchaseadapter);
                                shoppurchaseadapter.notifyDataSetChanged();
                                recyclerview.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);




                    /*billuser.add(dataSnapshot1.getKey());
                    SalesDetails sales=dataSnapshot1.getValue(SalesDetails.class);
                    SalesDetails salesDetails=new SalesDetails();
                    salesDetails.setMobile(sales.getMobile());
                    salesDetails.setBillDate(sales.getBillDate());
                    salesDetails.setBillNumber(sales.getBillNumber());
                    salesDetails.setTotalCost(sales.getTotalCost());
                    salesDetailsList.add(salesDetails);*/

               /* ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, invoicelist);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();*/
                /*shoppurchaseadapter=new Shoppurchaseadapter(salesDetailsList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(shoppurchaseadapter);
                shoppurchaseadapter.notifyDataSetChanged();*/

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
