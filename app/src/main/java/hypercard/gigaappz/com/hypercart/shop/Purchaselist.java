package hypercard.gigaappz.com.hypercart.shop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.andexert.expandablelayout.library.ExpandableLayoutListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;

public class Purchaselist extends AppCompatActivity {
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    ExpandableLayoutListView expandableLayoutListView;
    EditText mobile, price, date, invoice;
    List<String> invoicelist = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private final String[] array = {"Hello", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome", "World", "Android", "is", "Awesome"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchaselist);
        mobile = (EditText) findViewById(R.id.mobile_text);
        price = (EditText) findViewById(R.id.price);
        date = (EditText) findViewById(R.id.date);
        invoice = (EditText) findViewById(R.id.invoice);
        expandableLayoutListView = (ExpandableLayoutListView) findViewById(R.id.listview);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("salesshop").child(getIntent().getStringExtra("mobile"));
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    invoicelist.add(dataSnapshot1.getKey());
                    /*billuser.add(dataSnapshot1.getKey());
                    SalesDetails sales=dataSnapshot1.getValue(SalesDetails.class);
                    SalesDetails salesDetails=new SalesDetails();
                    salesDetails.setMobile(sales.getMobile());
                    salesDetails.setBillDate(sales.getBillDate());
                    salesDetails.setBillNumber(sales.getBillNumber());
                    salesDetails.setTotalCost(sales.getTotalCost());
                    salesDetailsList.add(salesDetails);*/
                }
                arrayAdapter = new ArrayAdapter<String>(Purchaselist.this, R.layout.view_row, R.id.header_text, invoicelist);

                expandableLayoutListView.setAdapter(arrayAdapter);

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

        expandableLayoutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Purchaselist.this, "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                mFirebaseDatabase.child(String.valueOf(parent.getItemAtPosition(position))).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        SalesDetails sales = dataSnapshot.getValue(SalesDetails.class);
                        mobile.setText(sales.getMobile());
                        price.setText(sales.getTotalCost());
                        date.setText(sales.getBillDate());
                        invoice.setText(sales.getBillNumber());
                        String mob=sales.getMobile();
                        Toast.makeText(Purchaselist.this, ""+mob, Toast.LENGTH_SHORT).show();
                        arrayAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
