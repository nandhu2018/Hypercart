package hypercard.gigaappz.com.hypercart.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hypercard.gigaappz.com.hypercart.CustomOnClick;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.RecyclerData;
import hypercard.gigaappz.com.hypercart.adapters.Cartadapter;
import hypercard.gigaappz.com.hypercart.model_class.Cart;

public class ShopOnline extends AppCompatActivity implements CustomOnClick {
    ArrayList<RecyclerData> myList = new ArrayList<>();
    RecyclerView listView;
    FirebaseDatabase database;
    DatabaseReference myRef, myref1,myref2,myref3,productlistref,userdetailref;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    List<String> barcodelist = new ArrayList<>();
    List<Cart> list = new ArrayList<Cart>();
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_online);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (RecyclerView) findViewById(R.id.recyclerview);
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("message");
        sharedPreferences=getSharedPreferences("logged", Context.MODE_PRIVATE);
        myRef = database.getReference("product").child(getIntent().getStringExtra("shop"));
        productlistref = database.getReference("productlist").child(getIntent().getStringExtra("shop"));

        myref3 = database.getReference("cart").child(sharedPreferences.getString("mobile","")).child(getIntent().getStringExtra("shop"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    barcodelist.add(dataSnapshot1.getKey());
                    Cart value = dataSnapshot1.getValue(Cart.class);

                    String name = value.getName();
                    String company = value.getCompany();
                    String qty = value.getQuantity();
                    String price = value.getPrice();
                    Cart fire = new Cart();
                    fire.setName("Product: " + name);
                    fire.setCompany("Company: " + company);
                    fire.setQuantity("Quantity: " + qty);
                    fire.setPrice("₹ " + price);
                    list.add(fire);
                }
                Cartadapter recyclerAdapter = new Cartadapter(list, ShopOnline.this);
                recyclerAdapter.setOnItemClickListener(ShopOnline.this);
                RecyclerView.LayoutManager recyce = new GridLayoutManager(ShopOnline.this, 2);
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
        myref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCartItemCount=0;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    mCartItemCount=mCartItemCount+1;
                    setupBadge();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shoponline, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intent=new Intent(ShopOnline.this,CartView.class);
            intent.putExtra("shop",getIntent().getStringExtra("shop"));
            intent.putExtra("shopname",getIntent().getStringExtra("shopname"));
            startActivity(intent);

            //showalert();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onItemClick(View view, final int position) {
        switch (view.getId()) {
            case R.id.mainLayout:
                myref1 = database.getReference("product").child(getIntent().getStringExtra("shop")).child(barcodelist.get(position));
                myref1.addListenerForSingleValueEvent(new ValueEventListener() {
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
                if (quantityneed.getEditText().getText().toString().equalsIgnoreCase("")){
                    quantityneed.getEditText().setError("Enter Quantity");
                }else {
                    int qtyneed = Integer.parseInt(quantityneed.getEditText().getText().toString());
                    if (qtyneed > Integer.parseInt(qty) || qtyneed < 1) {
                        quantityneed.getEditText().setError("Not available");
                    } else {


                        Cart fire = new Cart();
                        fire.setName(product);
                        fire.setCompany(company);
                        fire.setQuantity(quantityneed.getEditText().getText().toString());
                        fire.setPrice("" + Integer.parseInt(price) * qtyneed);
                        myref2 = database.getReference("cart").child(sharedPreferences.getString("mobile", "")).child(getIntent().getStringExtra("shop"));
                        myref2.child(barcode).setValue(fire);
                        myref2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mCartItemCount = 0;
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    mCartItemCount = mCartItemCount + 1;
                                    setupBadge();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("Hello", "Failed to read value.", error.toException());
                            }
                        });
                        dialog.dismiss();

                    }
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
