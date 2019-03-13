package hypercard.gigaappz.com.hypercart.admin;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.User;
import hypercard.gigaappz.com.hypercart.model_class.Shop;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.MyViewHolder> {
    int purchasecount=0;
    private List<User> shopList;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText mobile,shopname,shopplace,purchases;
        Button accept,reject;

        public MyViewHolder(View view) {
            super(view);
            mobile = (EditText) view.findViewById(R.id.mobile_text);
            shopname = (EditText) view.findViewById(R.id.username);
            shopplace = (EditText) view.findViewById(R.id.place);
            purchases = (EditText) view.findViewById(R.id.purchases);


        }
    }


    public UsersListAdapter(List<User> shopList) {
        this.shopList = shopList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User shop = shopList.get(position);

        holder.mobile.setText(shop.mobile);
        holder.shopname.setText(shop.name);
        holder.shopplace.setText(shop.place);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("salesuser").child(shop.mobile);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                purchasecount=0;

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    purchasecount=purchasecount+1;
                }
                holder.purchases.setText(String.valueOf(purchasecount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
    private void getpendinglist() {
        //  TODO pending shop request


    }
}