package hypercard.gigaappz.com.hypercart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Nandhu on 18-03-2018.
 */

public class CustomGridAdapter extends BaseAdapter {
    Context context;
    String logos[];
    List<Item> items;
    LayoutInflater inflter;
    public CustomGridAdapter(Context applicationContext, List<Item> items) {
        this.context = applicationContext;
        this.items=items;
        inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.gridcustom, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon);// get the reference of ImageView

            /*Glide.with(context)
                    .load(items.get(i).getbirdImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(icon);*/

        return view;
    }
}