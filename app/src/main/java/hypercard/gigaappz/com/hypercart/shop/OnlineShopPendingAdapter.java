package hypercard.gigaappz.com.hypercart.shop;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.OnlineOrder;
import hypercard.gigaappz.com.hypercart.model_class.Shop;

public class OnlineShopPendingAdapter extends RecyclerView.Adapter<OnlineShopPendingAdapter.MyViewHolder> {

    private List<OnlineOrder> onlineOrders;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText name, place, time,mobile;
        Button accept, viewitems,viewlocation;

        public MyViewHolder(View view) {
            super(view);
            name = (EditText) view.findViewById(R.id.name);
            place = (EditText) view.findViewById(R.id.place);
            time = (EditText) view.findViewById(R.id.time);
            mobile = (EditText) view.findViewById(R.id.mobile_text);
            accept = (Button) view.findViewById(R.id.accept);
            viewitems = (Button) view.findViewById(R.id.viewitems);
            viewlocation = (Button) view.findViewById(R.id.viewlocation);


        }
    }


    public OnlineShopPendingAdapter(List<OnlineOrder> onlineOrders) {
        this.onlineOrders = onlineOrders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_order, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OnlineOrder order = onlineOrders.get(position);

        holder.name.setText(order.mobile);
        holder.place.setText(order.place);
        holder.mobile.setText(order.mobile);
        holder.time.setText(order.time);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("onlineordershop").child(order.mobile);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "active");
                mFirebaseDatabase.updateChildren(updates);
            }
        });
        holder.viewitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "reject");
                mFirebaseDatabase.updateChildren(updates);
            }
        });
        holder.viewlocation.setOnClickListener(new View.OnClickListener() {
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
        return onlineOrders.size();
    }
}