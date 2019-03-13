package hypercard.gigaappz.com.hypercart.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;

public class Shoppurchaseadapter1 extends RecyclerView.Adapter<Shoppurchaseadapter1.MyViewHolder> {

    private List<SalesDetails> salesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText mobile,price,date,invoice;


        public MyViewHolder(View view) {
            super(view);
            mobile = (EditText) view.findViewById(R.id.mobile_text);
            price = (EditText) view.findViewById(R.id.price);
            date = (EditText) view.findViewById(R.id.date);
            invoice = (EditText) view.findViewById(R.id.invoice);
        }
    }


    public Shoppurchaseadapter1(List<SalesDetails> salesList) {
        this.salesList = salesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.puchases_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SalesDetails salesDetails = salesList.get(position);

        holder.mobile.setText(salesDetails.getShopname()+"\n"+salesDetails.getMobile());
        holder.price.setText("₹"+salesDetails.getTotalCost());
        holder.date.setText(salesDetails.getBillDate());
        holder.invoice.setText(salesDetails.getBillNumber());
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }
}