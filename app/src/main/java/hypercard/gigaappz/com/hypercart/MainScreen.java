package hypercard.gigaappz.com.hypercart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    List<Item> item = new ArrayList<Item>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        /*GridView gridView = (GridView) findViewById(R.id.grid_view);
        item.add(new Item("1", "file:///android_asset/search.png"));
        item.add(new Item("2", "file:///android_asset/barcode.png"));
        item.add(new Item("3", "file:///android_asset/shoponline.png"));
        item.add(new Item("4", "file:///android_asset/history.png"));
        item.add(new Item("5", "file:///android_asset/register1.png"));
        item.add(new Item("6", "file:///android_asset/register2.png"));
        CustomGridAdapter myAdapter=new CustomGridAdapter(MainScreen.this,item);
        gridView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();*/

    }
}
