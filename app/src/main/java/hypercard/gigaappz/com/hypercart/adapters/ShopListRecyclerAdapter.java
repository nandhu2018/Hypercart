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
import hypercard.gigaappz.com.hypercart.model_class.ShopModel;

/**
 * Created by DELL on 06-Nov-18.
 */

public class ShopListRecyclerAdapter extends RecyclerView.Adapter<ShopListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<ShopModel> shopModelList;

    public ShopListRecyclerAdapter(Context context, List<ShopModel> shopModelList) {
        this.context = context;
        this.shopModelList = shopModelList;
    }

    @NonNull
    @Override
    public ShopListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(context).inflate(R.layout.sales_history_single_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShopModel shopModel   = shopModelList.get(position);
        holder.shopName.setText("Name : "+shopModel.getShopName());
        holder.shopEmail.setText("Email : "+shopModel.getShopEmail());
       // holder.price.setText("Total Cost : "+salesDetails.getTotalCost());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
/*
    @Override
    public int getItemCount() {
        return salesDetailsList.size();
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView shopName, shopEmail, price;
        public ViewHolder(View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.bill_date_sales_history_card);
            shopEmail = itemView.findViewById(R.id.bill_no_sales_history_card);
            price   = itemView.findViewById(R.id.total_price_sales_history_card);
        }
    }
}
