package hypercard.gigaappz.com.hypercart.buy;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hypercard.gigaappz.com.hypercart.CustomOnClick;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.RecyclerAdapter;
import hypercard.gigaappz.com.hypercart.RecyclerData;
import hypercard.gigaappz.com.hypercart.interfaces.RemoveClickListner;
import hypercard.gigaappz.com.hypercart.model_class.Cart;
import hypercard.gigaappz.com.hypercart.model_class.Product;
import hypercard.gigaappz.com.hypercart.payment.PaymentActivity;
import hypercard.gigaappz.com.hypercart.user.Cartadapter2;
import info.androidhive.barcode.BarcodeReader;

public class BarcodeScan extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener, RemoveClickListner, CustomOnClick {
    BarcodeReader barcodeReader;
    List<String> barcodeval = new ArrayList<>();
    List<String> samelist = new ArrayList<>();
    List<String> remlist = new ArrayList<>();
    TextView totalprice;
    ArrayAdapter<String> adapter;
    private RecyclerAdapter mRecyclerAdapter;
    ArrayList<RecyclerData> myList = new ArrayList<>();
    RecyclerView listView;
    boolean barcodeexist = false;
    Button proceed;
    ArrayList<String> productlist = new ArrayList<>();
    List<String> qtylist = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef, myref1, remtemp;
    List<Cart> list = new ArrayList<Cart>();
    SharedPreferences preferences;
    boolean containsval = false;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        listView = (RecyclerView) findViewById(R.id.cartlist);
        totalprice = (TextView) findViewById(R.id.total);
        proceed = (Button) findViewById(R.id.proceed);
        preferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
        proceed.setEnabled(false);
        database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("message");
        myref1 = database.getReference("product").child(getIntent().getStringExtra("shop"));
        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Cart value = dataSnapshot1.getValue(Cart.class);
                    Cart fire = new Cart();
                    String name = value.getName();
                    String company = value.getCompany();
                    String qty = value.getQuantity();
                    String price = value.getPrice();
                    fire.setName(name);
                    fire.setCompany(company);
                    fire.setQuantity(qty);
                    fire.setPrice(price);
                    list.add(fire);

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });*/

        myref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    Product value = dataSnapshot1.getValue(Product.class);
                    productlist.add(value.getBarcode());

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });
     /*   mRecyclerAdapter = new RecyclerAdapter(myList,this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(mRecyclerAdapter);*/
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalprice.getText().toString().equalsIgnoreCase("0")){
                    Toast.makeText(BarcodeScan.this, "No items in cart", Toast.LENGTH_SHORT).show();
                }else {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate = df.format(c.getTime());

                    Intent intent = new Intent(BarcodeScan.this, PaymentActivity.class);
                    intent.putExtra("mobile", preferences.getString("mobile", ""));
                    intent.putExtra("date", formattedDate);
                    intent.putExtra("price", totalprice.getText().toString());
                    intent.putExtra("shop", getIntent().getStringExtra("shop"));
                    intent.putExtra("shopname", getIntent().getStringExtra("shopname"));
                    intent.putStringArrayListExtra("barcode", (ArrayList<String>) samelist);
                    intent.putStringArrayListExtra("qtyleft", (ArrayList<String>) qtylist);
                    startActivity(intent);
                    myRef = database.getReference("tempcart").child(preferences.getString("mobile", ""));
                    myRef.removeValue();
                    finish();
                }

            }
        });

    }

    @Override
    public void onScanned(final Barcode barcode) {
        /*if (barcodeval.size()>0){
            for (int i=0;i<barcodeval.size();i++){
                if (barcode.displayValue.equalsIgnoreCase(barcodeval.get(i))) {
                    barcodeexist = true;
                    break;
                }
            }
        }*/
        if (!containsval) {
            containsval = true;
            barcodeReader.playBeep();
            myRef = database.getReference("product").child(getIntent().getStringExtra("shop")).child(barcode.displayValue);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    if (dataSnapshot.exists()) {
                        barcodeval.add(barcode.displayValue);
                        Cart value = dataSnapshot.getValue(Cart.class);
                        String name = value.getName();
                        String company = value.getCompany();
                        String qty = value.getQuantity();
                        String price = value.getPrice();
                        Toast.makeText(BarcodeScan.this, "" + name, Toast.LENGTH_SHORT).show();
                        addtocart(name, company, price, qty, barcode.displayValue);

                    } else {
                        containsval = false;
                        Toast.makeText(BarcodeScan.this, "Item Not Found", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Hello", "Failed to read value.", error.toException());
                }
            });
        }


       /* if (barcodeval.size() == 0) {
            barcodeval.add(barcode.displayValue);
            containsval = false;
            //mRecyclerAdapter.notifyDataSetChanged();
        } else if (barcodeval.size() == 1) {
            barcodeval.add(barcode.displayValue);
            for (int i = 0; i < barcodeval.size(); i++) {
                if (barcodeval.get(i).equalsIgnoreCase(barcode.displayValue)) {
                    containsval = true;
                    break;
                } else {
                    containsval = false;
                }
                //mRecyclerAdapter.notifyDataSetChanged();
            }
            //  mRecyclerAdapter.notifyDataSetChanged();
        } else {
            barcodeval.add(barcode.displayValue);
            for (int i = 0; i < barcodeval.size() - 1; i++) {
                if (barcodeval.get(i).equalsIgnoreCase(barcode.displayValue)) {
                    containsval = true;
                    break;
                } else {
                    containsval = false;
                }
                //mRecyclerAdapter.notifyDataSetChanged();
            }
            // mRecyclerAdapter.notifyDataSetChanged();
        }
        if (!containsval) {
            samelist.add(barcode.displayValue);
            for (int i = 0; i < productlist.size(); i++) {
                if (barcode.displayValue.equalsIgnoreCase(productlist.get(i))) {
                    myRef = database.getReference("product").child(getIntent().getStringExtra("shop")).child(barcode.displayValue);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.


                            Cart value = dataSnapshot.getValue(Cart.class);

                            String name = value.getName();
                            String company = value.getCompany();
                            String qty = value.getQuantity();
                            String price = value.getPrice();
                            Toast.makeText(BarcodeScan.this, "" + name, Toast.LENGTH_SHORT).show();
                            addtocart(name,company,price,qty);

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Hello", "Failed to read value.", error.toException());
                        }
                    });
                }
            }

            *//*RecyclerData mLog1 = new RecyclerData();
            mLog1.setTitle(barcode.displayValue);
            mLog1.setDescription("Description");
            myList.add(mLog1);
            mRecyclerAdapter.notifyData(myList);*//*


        }


        *//*if (myList.size()==0){
            RecyclerData mLog = new RecyclerData();
            mLog.setTitle(barcode.displayValue);
            mLog.setDescription("Description");
            myList.add(mLog);
            mRecyclerAdapter.notifyData(myList);
        }*//*


        *//*for(int i=0;i<myList.size();i++)
        {
            if (Arrays.asList(myList).contains(barcode.displayValue)) {
                // true
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            }
            if (myList.get(i).getTitle().equalsIgnoreCase(barcode.displayValue))
            {
                Toast.makeText(this, "Item Already in the cart", Toast.LENGTH_SHORT).show();
            }else{

                RecyclerData mLog1 = new RecyclerData();
                mLog1.setTitle(barcode.displayValue);
                mLog1.setDescription("Description");
                myList.add(mLog1);
                mRecyclerAdapter.notifyData(myList);
            }
        }*//*

*/
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        //barcodeReader.playBeep();

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void OnRemoveClick(int index) {
        myList.remove(index);
        //mRecyclerAdapter.notifyData(myList);
    }

    private void tempcart(final String barcode, final String product, final String company, final String price, final String qty) {
        myRef = database.getReference("tempcart").child(preferences.getString("mobile", ""));
        Cart cart = new Cart();
        cart.setName(product);
        cart.setCompany(company);
        cart.setPrice(price);
        cart.setQuantity(qty);
        myRef.child(barcode).setValue(cart);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                int totalprice1 = 0;
                remlist.clear();
                for (DataSnapshot datasnapshot1 : dataSnapshot.getChildren()) {
                    remlist.add(datasnapshot1.getKey());
                    Cart getcart = datasnapshot1.getValue(Cart.class);
                    Cart fire = new Cart();
                    fire.setName("Product: " + getcart.getName());
                    fire.setCompany("Company: " + getcart.getCompany());
                    fire.setQuantity("Quantity: " + getcart.getQuantity());
                    fire.setPrice("₹ " + getcart.getPrice());
                    list.add(fire);
                    totalprice1 = totalprice1 + Integer.parseInt(getcart.getPrice());
                }
                Cartadapter2 recyclerAdapter = new Cartadapter2(list, BarcodeScan.this);
                recyclerAdapter.setOnItemClickListener(BarcodeScan.this);
                RecyclerView.LayoutManager recyce = new GridLayoutManager(BarcodeScan.this, 2);
                //RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                //recyce.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                listView.setLayoutManager(recyce);
                listView.setItemAnimator(new DefaultItemAnimator());
                listView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
                totalprice.setText(String.valueOf(totalprice1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef = database.getReference("tempcart").child(preferences.getString("mobile", ""));
        myRef.removeValue();
    }

    public void addtocart(final String product, final String company, final String price, final String qty, final String barcode) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
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
                if (quantityneed.getEditText().getText().toString().equalsIgnoreCase("")) {
                    quantityneed.getEditText().setError("Enter Quantity");
                } else {
                    int qtyneed = Integer.parseInt(quantityneed.getEditText().getText().toString());
                    if (qtyneed > Integer.parseInt(qty) || qtyneed < 1) {
                        quantityneed.getEditText().setError("Not available");
                    } else {
                        int pricenter = Integer.parseInt(price);
                        int total = (qtyneed * pricenter);
                        tempcart(barcode, product, company, String.valueOf(total), String.valueOf(qtyneed));
                    /*Cart fire = new Cart();
                    fire.setName("Product: "+product);
                    fire.setCompany("Company: "+company);
                    fire.setQuantity("Quantity: "+qtyneed);
                    fire.setPrice("₹ "+total);
                    list.add(fire);
                    Cartadapter recyclerAdapter = new Cartadapter(list, BarcodeScan.this);
                    recyclerAdapter.setOnItemClickListener(BarcodeScan.this);
                    RecyclerView.LayoutManager recyce = new GridLayoutManager(BarcodeScan.this, 2);
                    //RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                    //recyce.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                    listView.setLayoutManager(recyce);
                    listView.setItemAnimator(new DefaultItemAnimator());
                    listView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();*/
                        int qtyleft = Integer.parseInt(qty) - qtyneed;
                        qtylist.add(String.valueOf(qtyleft));
                        samelist.add(barcode);
                        proceed.setEnabled(true);


                        dialog.dismiss();
                        containsval = false;
                    }
                }
            }


        });

        dialog.show();
    }

    @Override
    public void onItemClick(View view, final int position) {
        switch (view.getId()) {
            case R.id.remove:
                myRef = database.getReference("tempcart").child(preferences.getString("mobile", ""));
                myRef.child(remlist.get(position)).removeValue();
                qtylist.remove(position);
                samelist.remove(position);
                break;
        }
    }
}
