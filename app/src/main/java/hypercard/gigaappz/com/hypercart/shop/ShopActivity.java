package hypercard.gigaappz.com.hypercart.shop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hypercard.gigaappz.com.hypercart.addstock.AddProduct;
import hypercard.gigaappz.com.hypercart.Mainpage;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;

public class ShopActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    List<SalesDetails> salesDetailsList = new ArrayList<>();
    ;
    Shoppurchaseadapter shoppurchaseadapter;
    private LinearLayout addproduct, sales, stock,onlineshoping;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerview;
    List<String> userlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        addproduct = (LinearLayout) findViewById(R.id.addproduct);
        sales = (LinearLayout) findViewById(R.id.purchases);
        stock = (LinearLayout) findViewById(R.id.viewstock);
        onlineshoping = (LinearLayout) findViewById(R.id.onlineshop);
        setSupportActionBar(toolbar);

        //recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerview = (RecyclerView) findViewById(R.id.list);
        sharedPreferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
        //mFirebaseInstance = FirebaseDatabase.getInstance();
        //mFirebaseDatabase = mFirebaseInstance.getReference("salesshop").child(sharedPreferences.getString("mobile",""));


        getpendinglist();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addproduct.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        addproduct.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        startActivity(new Intent(ShopActivity.this, AddProduct.class));
                        //set color back to default
                        addproduct.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        sales.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sales.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(ShopActivity.this, PurchasedDetails.class);
                        intent.putExtra("mobile", sharedPreferences.getString("mobile", ""));
                        startActivity(intent);
                        //set color back to default
                        sales.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        stock.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stock.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(ShopActivity.this, StockList.class);
                        intent.putExtra("mobile", sharedPreferences.getString("mobile", ""));
                        startActivity(intent);
                        //set color back to default
                        stock.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

        //TODO track order code
        onlineshoping.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onlineshoping.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(ShopActivity.this, OnlineShopingActivity.class);
                        intent.putExtra("mobile", sharedPreferences.getString("mobile", ""));
                        startActivity(intent);
                        //set color back to default
                        onlineshoping.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
        /*sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addproduct.setBackgroundColor(getResources().getColor(R.color.white));
                sales.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                stock.setBackgroundColor(getResources().getColor(R.color.white));
                createDummyHistoryList();
                SalesHistoryRecyclerAdapter historyAdapter = new SalesHistoryRecyclerAdapter(ShopActivity.this, salesDetailsList);
                LinearLayoutManager historyManager = new LinearLayoutManager(ShopActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(historyManager);
                recyclerView.setAdapter(historyAdapter);
            }
        });
        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addproduct.setBackgroundColor(getResources().getColor(R.color.white));
                sales.setBackgroundColor(getResources().getColor(R.color.white));
                stock.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                createDummyStockList();
                ViewStockRecyclerAdapter stockAdapter    = new ViewStockRecyclerAdapter(ShopActivity.this, itemStockList);
                LinearLayoutManager stockManager = new LinearLayoutManager(ShopActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(stockManager);
                recyclerView.setAdapter(stockAdapter);
            }
        });*/


    }

    private void getpendinglist() {
        String mobile = sharedPreferences.getString("mobile", "");
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("salesshop").child(mobile);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                salesDetailsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    /*String timeStamptoday = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    long timestamp = Long.parseLong(dataSnapshot1.getKey()) * 1000L;
                    if (getDate(Long.parseLong(timeStamptoday) * 1000L).equalsIgnoreCase(getDate(timestamp))) {
                        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                salesDetailsList.clear();
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

            }




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


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            showalert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showalert() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ShopActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ShopActivity.this);
        }
        builder.setTitle("LogOut")
                .setMessage("Are you sure you want to Logout")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        SharedPreferences preferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(ShopActivity.this, Mainpage.class));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.add_stock:
                // TODO: 06-Nov-18
                startActivity(new Intent(ShopActivity.this, AddProduct.class));
                break;
            case R.id.view_stock:
                Intent intent = new Intent(ShopActivity.this, StockList.class);
                intent.putExtra("mobile", sharedPreferences.getString("mobile", ""));
                startActivity(intent);
                //createDummyStockList();
               /* ViewStockRecyclerAdapter stockAdapter    = new ViewStockRecyclerAdapter(ShopActivity.this, itemStockList);
                LinearLayoutManager stockManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(stockManager);
                recyclerView.setAdapter(stockAdapter);*/
                break;
            case R.id.sale_history:
                Intent intent1 = new Intent(ShopActivity.this, PurchasedDetails.class);
                intent1.putExtra("mobile", sharedPreferences.getString("mobile", ""));
                startActivity(intent1);
                // TODO: 06-Nov-18
                //createDummyHistoryList();
               /* SalesHistoryRecyclerAdapter historyAdapter = new SalesHistoryRecyclerAdapter(ShopActivity.this, salesDetailsList);
                LinearLayoutManager historyManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(historyManager);
                recyclerView.setAdapter(historyAdapter);*/
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // private List<SalesDetails> salesDetailsList;
    /*private void createDummyHistoryList() {
        salesDetailsList    = new ArrayList<>();
        for (int i=0; i<10; i++){
            SalesDetails salesDetails   = new SalesDetails(1000+i, 650+i, i+"/11/2018");
            salesDetailsList.add(salesDetails);
        }
    }

    private List<ItemStock> itemStockList;
    private void createDummyStockList() {
        itemStockList   = new ArrayList<>();
        for (int i=0; i<10; i++){
            ItemStock itemStock = new ItemStock("item "+i, "company "+i, i+1, 100+i, 99+i);
            itemStockList.add(itemStock);
        }
    }*/
}
