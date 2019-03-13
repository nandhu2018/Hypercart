package hypercard.gigaappz.com.hypercart.admin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.Shop;

public class Userpendingadapter extends RecyclerView.Adapter<Userpendingadapter.MyViewHolder> {

    private List<Shop> shopList;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText mobile,shopname,shopplace,shopcontact;
        Button accept,reject;

        public MyViewHolder(View view) {
            super(view);
            mobile = (EditText) view.findViewById(R.id.mobile_text);
            shopname = (EditText) view.findViewById(R.id.shopname);
            shopplace = (EditText) view.findViewById(R.id.shopplace);
            shopcontact = (EditText) view.findViewById(R.id.contact);
            accept = (Button) view.findViewById(R.id.accept);
            reject = (Button) view.findViewById(R.id.reject);


        }
    }


    public Userpendingadapter(List<Shop> shopList) {
        this.shopList = shopList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Shop shop = shopList.get(position);

        holder.mobile.setText(shop.mobile);
        holder.shopname.setText(shop.name);
        holder.shopplace.setText(shop.place);
        holder.shopcontact.setText(shop.contact);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("shop").child(shop.mobile);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "active");
                mFirebaseDatabase.updateChildren(updates);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "reject");
                mFirebaseDatabase.updateChildren(updates);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}