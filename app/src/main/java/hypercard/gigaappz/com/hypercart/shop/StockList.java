package hypercard.gigaappz.com.hypercart.shop;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hypercard.gigaappz.com.hypercart.CustomOnClick;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.adapters.Cartadapter;
import hypercard.gigaappz.com.hypercart.model_class.Cart;
import hypercard.gigaappz.com.hypercart.model_class.Product;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;
import hypercard.gigaappz.com.hypercart.user.ShopOnline;

public class StockList extends AppCompatActivity implements CustomOnClick {
    RecyclerView listView;
    private DatabaseReference mFirebaseDatabase,mFirebaseDatabase1,mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance;
    ImageView back;
    ProgressBar progressBar;
    List<String> barcodelist = new ArrayList<>();
    List<Product> list = new ArrayList<Product>();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);
        listView = (RecyclerView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        back = (ImageView) findViewById(R.id.back);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        //myRef = database.getReference("message");
        sharedPreferences=getSharedPreferences("logged", Context.MODE_PRIVATE);
        mFirebaseDatabase = mFirebaseInstance.getReference("product").child(getIntent().getStringExtra("mobile"));
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    barcodelist.add(dataSnapshot1.getKey());
                    Product value = dataSnapshot1.getValue(Product.class);

                    String name = value.getName();
                    String company = value.getCompany();
                    String qty = value.getQty();
                    String price = value.getPrice();
                    Product fire = new Product();
                    fire.setName("Product: " + name);
                    fire.setCompany("Company: " + company);
                    fire.setQty("Quantity: " + qty);
                    fire.setPrice("₹ " + price);
                    list.add(fire);
                }
                if (list.size()>0){
                    progressBar.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                Productadapter recyclerAdapter = new Productadapter(list, StockList.this);
                recyclerAdapter.setOnItemClickListener(StockList.this);
                RecyclerView.LayoutManager recyce = new GridLayoutManager(StockList.this, 2);
                //RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                //recyce.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                listView.setLayoutManager(recyce);
                listView.setItemAnimator(new DefaultItemAnimator());
                listView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View view, final int position) {
        switch (view.getId()){
            case R.id.mainLayout:
                mFirebaseDatabase1 = mFirebaseInstance.getReference("product").child(getIntent().getStringExtra("mobile")).child(barcodelist.get(position));
                mFirebaseDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Cart value = dataSnapshot.getValue(Cart.class);

                        String name = value.getName();
                        String company = value.getCompany();
                        String qty = value.getQuantity();
                        String price = value.getPrice();
                        addtocart(name, company, price, qty,barcodelist.get(position));

                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Hello", "Failed to read value.", error.toException());
                    }
                });

                break;
        }


    }
    public void addtocart(final String product, final String company, final String price, final String qty,final String barcode) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.custom_dialog1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final TextView name = dialog.findViewById(R.id.product);
        final TextView company1 = dialog.findViewById(R.id.company);
        final TextView quantity = dialog.findViewById(R.id.quantityleft);
        final TextView price1 = dialog.findViewById(R.id.price);
        final TextInputLayout quantityneed = dialog.findViewById(R.id.quantityneed);
        name.setText("Product: " + product);
        company1.setText("Company: " + company);
        quantity.setText("Quantity left: " + qty);
        price1.setText("Price per item: " + price);
        Button okButton = (Button) dialog.findViewById(R.id.addtocart);



        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityneed.getEditText().getText().toString().equalsIgnoreCase("")||Integer.parseInt(quantityneed.getEditText().getText().toString())==0){
                    quantityneed.getEditText().setError("Enter Valid Quantity");
                }else {

                    int totalqty = Integer.parseInt(qty) + Integer.parseInt(quantityneed.getEditText().getText().toString());
                    mFirebaseDatabase2 = mFirebaseInstance.getReference("product").child(getIntent().getStringExtra("mobile")).child(barcode);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("qty", String.valueOf(totalqty));
                    mFirebaseDatabase2.updateChildren(updates);
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }
}
