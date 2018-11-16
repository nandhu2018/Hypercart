package hypercard.gigaappz.com.hypercart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hypercard.gigaappz.com.hypercart.AddProduct;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.adapters.SalesHistoryRecyclerAdapter;
import hypercard.gigaappz.com.hypercart.adapters.ViewStockRecyclerAdapter;
import hypercard.gigaappz.com.hypercart.model_class.ItemStock;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;

public class ShopActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private TextView addproduct,sales,stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        addproduct=(TextView)findViewById(R.id.addproduct);
        sales=(TextView)findViewById(R.id.sales);
        stock=(TextView)findViewById(R.id.stock);

        setSupportActionBar(toolbar);

        recyclerView    = findViewById(R.id.recycler_shop_activity);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopActivity.this, AddProduct.class));
               /* addproduct.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                sales.setBackgroundColor(getResources().getColor(R.color.white));
                stock.setBackgroundColor(getResources().getColor(R.color.white));*/
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
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
        });


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.add_stock:
                // TODO: 06-Nov-18

                break;
            case R.id.view_stock:
                // TODO: 06-Nov-18
                createDummyStockList();
                ViewStockRecyclerAdapter stockAdapter    = new ViewStockRecyclerAdapter(ShopActivity.this, itemStockList);
                LinearLayoutManager stockManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(stockManager);
                recyclerView.setAdapter(stockAdapter);
                break;
            case R.id.sale_history:
                // TODO: 06-Nov-18
                createDummyHistoryList();
                SalesHistoryRecyclerAdapter historyAdapter = new SalesHistoryRecyclerAdapter(ShopActivity.this, salesDetailsList);
                LinearLayoutManager historyManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(historyManager);
                recyclerView.setAdapter(historyAdapter);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<SalesDetails> salesDetailsList;
    private void createDummyHistoryList() {
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
    }
}
