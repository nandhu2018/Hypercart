package hypercard.gigaappz.com.hypercart.admin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.Shop;
import hypercard.gigaappz.com.hypercart.shop.Shoppurchaseadapter;


public class Shops extends Fragment {

    RecyclerView recyclerView;
    List<Shop> pendinglist= new ArrayList<>();;
    Shoppendingadapter userpendingadapter;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    TextView nocontent;
    public Shops() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shops, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        nocontent=(TextView)view.findViewById(R.id.nocontent);
        recyclerView.setVisibility(View.GONE);
        nocontent.setVisibility(View.VISIBLE);
        getpendinglist();
        return view;
    }
    private void getpendinglist() {
        //  TODO pending shop request
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("shop");
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendinglist.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    Shop shop = dataSnapshot1.getValue(Shop.class);

                        Shop shoppending=new Shop();
                        shoppending.setName(shop.getName());
                        shoppending.setContact(shop.getContact());
                        shoppending.setMobile(shop.getMobile());
                        shoppending.setPlace(shop.getPlace());
                        pendinglist.add(shoppending);



                }
                if (pendinglist.size()>0){
                    recyclerView.setVisibility(View.VISIBLE);
                    nocontent.setVisibility(View.GONE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    nocontent.setVisibility(View.VISIBLE);
                }
                userpendingadapter=new Shoppendingadapter(pendinglist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(userpendingadapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
