package hypercard.gigaappz.com.hypercart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hypercard.gigaappz.com.hypercart.adapters.Cartadapter;
import hypercard.gigaappz.com.hypercart.interfaces.RemoveClickListner;
import hypercard.gigaappz.com.hypercart.model_class.Cart;
import hypercard.gigaappz.com.hypercart.model_class.Product;
import hypercard.gigaappz.com.hypercart.model_class.Shop;
import info.androidhive.barcode.BarcodeReader;

import static com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils.dpToPx;

public class BarcodeScan extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener, RemoveClickListner {
    BarcodeReader barcodeReader;
    List<String> barcodeval = new ArrayList<>();
    List<String> samelist = new ArrayList<>();
    TextView totalprice;
    ArrayAdapter<String> adapter;
    private RecyclerAdapter mRecyclerAdapter;
    ArrayList<RecyclerData> myList = new ArrayList<>();
    RecyclerView listView;

    ArrayList<String> productlist = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRef, myref1;
    List<Cart> list = new ArrayList<Cart>();

    boolean containsval = false;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
        listView = (RecyclerView) findViewById(R.id.cartlist);
        totalprice = (TextView) findViewById(R.id.total);
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


    }

    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        if (barcodeval.size() == 0) {
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

            /*RecyclerData mLog1 = new RecyclerData();
            mLog1.setTitle(barcode.displayValue);
            mLog1.setDescription("Description");
            myList.add(mLog1);
            mRecyclerAdapter.notifyData(myList);*/


        }


        /*if (myList.size()==0){
            RecyclerData mLog = new RecyclerData();
            mLog.setTitle(barcode.displayValue);
            mLog.setDescription("Description");
            myList.add(mLog);
            mRecyclerAdapter.notifyData(myList);
        }*/


        /*for(int i=0;i<myList.size();i++)
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
        }*/


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
    public void addtocart(final String product, final String company, final String price, final String qty) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final TextView name = dialog.findViewById(R.id.product);
        final TextView company1 = dialog.findViewById(R.id.company);
        final TextView quantity = dialog.findViewById(R.id.quantityleft);
        final TextView price1 = dialog.findViewById(R.id.price);
        final TextInputLayout quantityneed = dialog.findViewById(R.id.quantityneed);

        name.setText("Product: "+product);
        company1.setText("Company: "+company);
        quantity.setText("Quantity left: "+qty);
        price1.setText("Price per item: "+price);
        Button okButton = (Button) dialog.findViewById(R.id.addtocart);




        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtyneed=Integer.parseInt(quantityneed.getEditText().getText().toString());
                if (qtyneed>Integer.parseInt(qty)||qtyneed<1){
                    quantityneed.getEditText().setError("Not available");
                }else {
                    int pricenter=Integer.parseInt(price);
                    int total=(qtyneed*pricenter);
                    Cart fire = new Cart();
                    fire.setName("Product: "+product);
                    fire.setCompany("Company: "+company);
                    fire.setQuantity("Quantity: "+qtyneed);
                    fire.setPrice("Price: "+total);
                    list.add(fire);
                    Cartadapter recyclerAdapter = new Cartadapter(list, BarcodeScan.this);
                    RecyclerView.LayoutManager recyce = new GridLayoutManager(BarcodeScan.this, 2);
                    //RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                    //recyce.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                    listView.setLayoutManager(recyce);
                    listView.setItemAnimator(new DefaultItemAnimator());
                    listView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();

                    totalprice.setText(String.valueOf(Integer.parseInt(totalprice.getText().toString())+total));
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }
}
