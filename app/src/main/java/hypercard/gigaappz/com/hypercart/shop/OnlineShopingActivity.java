package hypercard.gigaappz.com.hypercart.shop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.User;
import hypercard.gigaappz.com.hypercart.admin.Userpendingadapter;
import hypercard.gigaappz.com.hypercart.model_class.DeliveryDetails;
import hypercard.gigaappz.com.hypercart.model_class.OnlineOrder;
import hypercard.gigaappz.com.hypercart.model_class.Product;
import hypercard.gigaappz.com.hypercart.model_class.Shop;

public class OnlineShopingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<DeliveryDetails> orders= new ArrayList<>();;
    OnlineShopPendingAdapter orderpendingadapter;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_shoping);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        getpendinglist();
    }

    private void getpendinglist() {
        //  TODO pending shop request
        mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference("userdetails").child(getIntent().getStringExtra("mobile"));
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();


                    /*User onlineOrder = dataSnapshot.getValue(User.class);

                    *//*if (onlineOrder.getStatus().equalsIgnoreCase("pending"))
                    {
                        OnlineOrder order=new OnlineOrder();
                        order.setName(onlineOrder.getName());
                        order.setMobile(onlineOrder.getMobile());
                        order.setPlace(onlineOrder.getPlace());
                        order.setTime("");
                        order.setStatus(onlineOrder.getStatus());
                        orders.add(order);
                    }
*//*
                    OnlineOrder order=new OnlineOrder();
                    order.setName(onlineOrder.getName());
                    order.setMobile(onlineOrder.getMobile());
                    order.setPlace(onlineOrder.getPlace());
                    order.setTime("");
                    order.setStatus("");
                    orders.add(order);*/
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DeliveryDetails deliveryDetails=dataSnapshot1.getValue(DeliveryDetails.class);
                    DeliveryDetails details=new DeliveryDetails();
                    details.setName(deliveryDetails.getName());
                    details.setHouse(deliveryDetails.getHouse());
                    details.setPostoffice(deliveryDetails.getPostoffice());
                    details.setCity(deliveryDetails.getCity());
                    details.setPhone(deliveryDetails.getPhone());
                    details.setPincode(deliveryDetails.getPincode());
                    details.setDate(deliveryDetails.getDate());
                    details.setTime(deliveryDetails.getTime());
                    orders.add(details);
                }



                
                if (orders.size()>0){
                    recyclerView.setVisibility(View.VISIBLE);
                    //nocontent.setVisibility(View.GONE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    //nocontent.setVisibility(View.VISIBLE);
                }
                orderpendingadapter=new OnlineShopPendingAdapter(orders);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OnlineShopingActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(orderpendingadapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
