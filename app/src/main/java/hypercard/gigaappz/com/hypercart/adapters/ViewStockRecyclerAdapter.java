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

/**
 * Created by DELL on 06-Nov-18.
 */

public class ViewStockRecyclerAdapter extends RecyclerView.Adapter<ViewStockRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<ItemStock> itemStockList;

    public ViewStockRecyclerAdapter(Context context, List<ItemStock> itemStockList) {
        this.context = context;
        this.itemStockList = itemStockList;
    }

    @NonNull
    @Override
    public ViewStockRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(context).inflate(R.layout.view_stock_recycler_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemStock itemStock = itemStockList.get(position);
        holder.item.setText(itemStock.getItem());
        holder.company.setText(itemStock.getCompany());
        holder.quantity.setText("Quantity : "+itemStock.getQuantity());
        holder.mrp.setText("MRP : "+itemStock.getMrp());
        holder.price.setText("Price : "+itemStock.getPrice());
    }

    @Override
    public int getItemCount() {
        return itemStockList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item, company, quantity, mrp, price;
        public ViewHolder(View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item_view_stock_card);
            company = itemView.findViewById(R.id.company_view_stock_card);
            quantity    = itemView.findViewById(R.id.quantity_view_stock_card);
            mrp = itemView.findViewById(R.id.mrp_view_stock_card);
            price   = itemView.findViewById(R.id.price_view_stock_card);
        }
    }
}
