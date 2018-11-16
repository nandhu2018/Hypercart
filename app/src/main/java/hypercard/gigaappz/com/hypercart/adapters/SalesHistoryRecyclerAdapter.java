package hypercard.gigaappz.com.hypercart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.ItemStock;
import hypercard.gigaappz.com.hypercart.model_class.SalesDetails;

/**
 * Created by DELL on 06-Nov-18.
 */

public class SalesHistoryRecyclerAdapter extends RecyclerView.Adapter<SalesHistoryRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<SalesDetails> salesDetailsList;

    public SalesHistoryRecyclerAdapter(Context context, List<SalesDetails> salesDetailsList) {
        this.context = context;
        this.salesDetailsList = salesDetailsList;
    }

    @NonNull
    @Override
    public SalesHistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(context).inflate(R.layout.sales_history_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalesDetails salesDetails   = salesDetailsList.get(position);
        holder.date.setText(""+salesDetails.getBillDate());
        holder.bill.setText("Bill : "+salesDetails.getBillNumber());
        holder.price.setText("Total Cost : "+salesDetails.getTotalCost());
    }

    @Override
    public int getItemCount() {
        return salesDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date, bill, price;
        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.bill_date_sales_history_card);
            bill = itemView.findViewById(R.id.bill_no_sales_history_card);
            price   = itemView.findViewById(R.id.total_price_sales_history_card);
        }
    }
}
