package hypercard.gigaappz.com.hypercart.shop;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.Cart;
import hypercard.gigaappz.com.hypercart.model_class.Cart1;
import hypercard.gigaappz.com.hypercart.model_class.DeliveryDetails;
import hypercard.gigaappz.com.hypercart.model_class.OnlineOrder;
import hypercard.gigaappz.com.hypercart.model_class.Shop;

public class OnlineShopPendingAdapter extends RecyclerView.Adapter<OnlineShopPendingAdapter.MyViewHolder> {

    private List<DeliveryDetails> onlineOrders;
    private DatabaseReference mFirebaseDatabase,userdetail,orderproducts;
    private FirebaseDatabase mFirebaseInstance;
    private android.content.Context context;
    private SharedPreferences sharedPreferences;
    private ArrayList<String>orderitems=new ArrayList<>();

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


    public OnlineShopPendingAdapter(List<DeliveryDetails> onlineOrders) {
        this.onlineOrders = onlineOrders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_order, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DeliveryDetails order = onlineOrders.get(position);

        holder.name.setText(order.getName());
        holder.place.setText(order.getPostoffice());
        holder.mobile.setText(order.getPhone());
        holder.time.setText(order.getTime());
        sharedPreferences=context.getSharedPreferences("logged", android.content.Context.MODE_PRIVATE);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        orderproducts = mFirebaseInstance.getReference("onlineordershop").child(order.getPhone()).child(sharedPreferences.getString("mobile",""));
        mFirebaseDatabase = mFirebaseInstance.getReference("onlineorderuser").child(order.getPhone()).child(sharedPreferences.getString("mobile",""));
        orderproducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderitems.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Cart order1 = dataSnapshot1.getValue(Cart.class);
                    orderitems.add("Product: "+order1.getName() + "\nCompany: " + order1.getCompany() + "\nQty: " + order1.getQuantity() + "\nPrice: " + order1.getPrice()+"\n");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog_viewitems);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                ListView lv = (ListView) dialog.findViewById(R.id.list);
                Button ok=(Button)dialog.findViewById(R.id.ok_button);

                /*final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                LayoutInflater inflater = context.getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom, null);
                //builderSingle.setIcon(R.drawable.);
                builderSingle.setTitle("Order Items");
                ListView lv = (ListView) convertView.findViewById(R.id.lv);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);*/


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,orderitems);
               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    lv.setDivider(context.getDrawable(R.drawable.divider));
                }*/
                lv.setAdapter(adapter);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                /*if (adapter.getCount()>0){

                }*/
                dialog.show();
               /* arrayAdapter.add("Hardik");
                arrayAdapter.add("Archit");
                arrayAdapter.add("Jignesh");
                arrayAdapter.add("Umang");
                arrayAdapter.add("Gatti");*/

                /*builderSingle.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      *//*  String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();*//*

                    }
                });
                builderSingle.show();*/
            }
        });
        holder.viewlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userdetail = mFirebaseInstance.getReference("userdetails").child(sharedPreferences.getString("mobile","")).child(order.getPhone());
                userdetail.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DeliveryDetails deliveryDetails=dataSnapshot.getValue(DeliveryDetails.class);
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                        builderSingle.setIcon(R.drawable.location);
                        builderSingle.setTitle("Delivery Details");
                        builderSingle.setMessage(deliveryDetails.getName()+"\n\n"+deliveryDetails.getHouse()+"(H),\n"+deliveryDetails.getPostoffice()+" P O,\n"+deliveryDetails.getCity()+"\nPin: "+deliveryDetails.getPincode());
                        builderSingle.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builderSingle.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return onlineOrders.size();
    }
}