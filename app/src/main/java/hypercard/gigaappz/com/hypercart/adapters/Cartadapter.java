package hypercard.gigaappz.com.hypercart.adapters;

/**
 * Created by DELL on 15-Nov-18.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hypercard.gigaappz.com.hypercart.CustomOnClick;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.Cart;

/**
 * Created by csa on 3/7/2017.
 */

public class Cartadapter extends RecyclerView.Adapter<Cartadapter.MyHoder>{

    List<Cart> list;
    Context context;
    private static CustomOnClick customOnClick;
    public Cartadapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        MyHoder myHoder = new MyHoder(view);


        return myHoder;
    }

    @Override
    public void onBindViewHolder(MyHoder holder, int position) {
        Cart mylist = list.get(position);
        holder.name.setText(mylist.getName());
        holder.company.setText(mylist.getCompany());
        if (mylist.getQuantity().equalsIgnoreCase("Quantity: 0")){
            holder.qty.setTextColor(Color.RED);
            holder.qty.setText("Out Of Stock");
        }else {
            holder.qty.setText(mylist.getQuantity());
        }
        holder.price.setText(mylist.getPrice());
    }
    public void setOnItemClickListener(CustomOnClick customOnClick){
        Cartadapter.customOnClick=customOnClick;
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;

            }
            else{

                arr=list.size();
            }



        }catch (Exception e){



        }

        return arr;

    }


    class MyHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,company,qty,price;
        ImageButton remove;
        LinearLayout linearLayout;


        public MyHoder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            company= (TextView) itemView.findViewById(R.id.company);
            qty = (TextView) itemView.findViewById(R.id.qty);
            price = (TextView) itemView.findViewById(R.id.price);
            remove = (ImageButton) itemView.findViewById(R.id.remove);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            remove.setOnClickListener(this);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            customOnClick.onItemClick(v,getAdapterPosition());
        }
    }

}
