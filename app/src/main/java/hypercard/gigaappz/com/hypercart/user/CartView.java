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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import hypercard.gigaappz.com.hypercart.model_class.Cart;
import hypercard.gigaappz.com.hypercart.model_class.Cart1;
import hypercard.gigaappz.com.hypercart.model_class.DeliveryDetails;
import hypercard.gigaappz.com.hypercart.model_class.OnlineOrder;
import hypercard.gigaappz.com.hypercart.model_class.Product;
import hypercard.gigaappz.com.hypercart.model_class.Wallet;
import hypercard.gigaappz.com.hypercart.payment.PaymentActivity;

public class CartView extends AppCompatActivity implements CustomOnClick {
    RecyclerView listView;
    FirebaseDatabase database;
    DatabaseReference myRef, myref1, myref2,walletref,userdetailref;
    SharedPreferences sharedPreferences;
    int totalprice = 0;
    String walletbal;
    Button checkout;
    List<Cart1> list = new ArrayList<Cart1>();
    List<String> qtylist = new ArrayList<>();
    List<String> barcode = new ArrayList<>();
    List<String> company = new ArrayList<>();
    List<String> price = new ArrayList<>();
    List<String> qty = new ArrayList<>();
    TextView empty;
    boolean isavailable = true;
    int neededqty=0;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        listView = (RecyclerView) findViewById(R.id.recyclerview);
        checkout = (Button) findViewById(R.id.checkout);
        empty = (TextView) findViewById(R.id.empty);
        back = (ImageView) findViewById(R.id.back);
        empty.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        checkout.setEnabled(false);
        sharedPreferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
        String mob = sharedPreferences.getString("mobile", "");
        final String shop = getIntent().getStringExtra("shop");
        database = FirebaseDatabase.getInstance();

        walletref = database.getReference("wallet").child(mob);
        walletref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Wallet wallet=dataSnapshot.getValue(Wallet.class);
                walletbal=wallet.getWalletbalance();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef = database.getReference("cart").child(mob).child(shop);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                totalprice = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    barcode.add(dataSnapshot1.getKey());
                    Cart1 value = dataSnapshot1.getValue(Cart1.class);
                    String name = value.getName();
                    String company = value.getCompany();
                    String qty = value.getQuantity();
                    String price = value.getPrice();
                    Cart1 fire = new Cart1();
                    fire.setName("Product: " + name);
                    fire.setCompany("Company: " + company);
                    fire.setQty("Quantity: " + qty);
                    fire.setPrice("₹ " + price);
                    fire.setQuantity("Quantity: " + qty);
                    totalprice = totalprice + Integer.parseInt(price);
                    qtylist.add(qty);
                    list.add(fire);

                }
                if (list.size() > 0) {
                    empty.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    checkout.setEnabled(true);
                }
                Cartadapter1 recyclerAdapter = new Cartadapter1(list, CartView.this);
                recyclerAdapter.setOnItemClickListener(CartView.this);
                RecyclerView.LayoutManager recyce = new GridLayoutManager(CartView.this, 1);
                //RecyclerView.LayoutManager recyce = new LinearLayoutManager(CartView.this);
                //`recyce.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
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

        /*for (int i = 0; i < barcode.size(); i++) {
            myref1 = database.getReference("product").child(getIntent().getStringExtra("shop")).child(barcode.get(i));
            myref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Product value = dataSnapshot.getValue(Product.class);
                       if (value.getQty().)

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Hello", "Failed to read value.", error.toException());
                }
            });
        }*/

        /*myref2 = database.getReference("cart").child(mob);
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            }
                if (dataSnapshot.hasChild(shop)) {
                    myref2.child(shop).removeValue();

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());
                SharedPreferences preferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
                Intent intent = new Intent(CartView.this, PaymentActivity.class);
                intent.putExtra("mobile", preferences.getString("mobile", ""));
                intent.putExtra("date", formattedDate);
                intent.putExtra("price", String.valueOf(totalprice));
                intent.putExtra("shop", getIntent().getStringExtra("shop"));
                intent.putExtra("shopname", getIntent().getStringExtra("shopname"));
                intent.putExtra("walletbal", walletbal);
                intent.putStringArrayListExtra("barcode", (ArrayList<String>) barcode);
                intent.putStringArrayListExtra("qtyleft", (ArrayList<String>) qtylist);
                startActivity(intent);
                finish();*/
                userdetaildialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CartView.this, MainScreen1.class));
                finish();
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //Toast.makeText(CartView.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(CartView.this, "Item removed ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                myref2 = database.getReference("cart").child(sharedPreferences.getString("mobile", "")).child(shop).child(barcode.get(position));
                myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            myref2.removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listView);
    }


    public void userdetaildialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_deliverydetail);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        final EditText name = dialog.findViewById(R.id.name);
        final EditText house = dialog.findViewById(R.id.house);
        final EditText post = dialog.findViewById(R.id.post_office);
        final EditText city = dialog.findViewById(R.id.city);
        final EditText pincode = dialog.findViewById(R.id.pincode);
        final EditText phone = dialog.findViewById(R.id.phone);


        Button okButton = (Button) dialog.findViewById(R.id.save);


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()|house.getText().toString().isEmpty()|post.getText().toString().isEmpty()|city.getText().toString().isEmpty()|name.getText().toString().isEmpty()|phone.getText().toString().isEmpty())
                {
                    Toast.makeText(CartView.this, "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
                }else {
                    if (phone.getText().toString().length()==10){
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = df.format(c.getTime());
                        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss a");
                        String formattedtime = df1.format(c.getTime());
                        DeliveryDetails details=new DeliveryDetails();
                        details.setName(name.getText().toString());
                        details.setHouse(house.getText().toString());
                        details.setPostoffice(post.getText().toString());
                        details.setCity(city.getText().toString());
                        details.setPhone(phone.getText().toString());
                        details.setPincode(pincode.getText().toString());
                        details.setDate(formattedDate);
                        details.setTime(formattedtime);
                        details.setStatus("pending");
                        userdetailref = database.getReference("userdetails").child(getIntent().getStringExtra("shop")).child(sharedPreferences.getString("mobile",""));
                        userdetailref.setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                    String formattedDate = df.format(c.getTime());
                                    SharedPreferences preferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
                                    Intent intent = new Intent(CartView.this, PaymentActivity.class);
                                    intent.putExtra("mobile", preferences.getString("mobile", ""));
                                    intent.putExtra("date", formattedDate);
                                    intent.putExtra("price", String.valueOf(totalprice));
                                    intent.putExtra("shop", getIntent().getStringExtra("shop"));
                                    intent.putExtra("shopname", getIntent().getStringExtra("shopname"));
                                    intent.putExtra("walletbal", walletbal);
                                    intent.putStringArrayListExtra("barcode", (ArrayList<String>) barcode);
                                    intent.putStringArrayListExtra("qtyleft", (ArrayList<String>) qtylist);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    }else {
                        phone.setError("Enter valid mobile");
                    }
                }





            }
        });

        dialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.mainLayout:

                break;
        }
    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
