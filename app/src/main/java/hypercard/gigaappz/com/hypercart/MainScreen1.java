package hypercard.gigaappz.com.hypercart;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hypercard.gigaappz.com.hypercart.model_class.Shop;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MainScreen1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SliderLayout sliderLayout;
    LinearLayout search, barcode, shoponline, history;
    private DatabaseReference mFirebaseDatabase, mFirebaseDatabase1, mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance;
    ArrayList<String> shops = new ArrayList<>();
    EditText locationtext;
    SpinnerDialog spinnerDialog;
    String shopname;
    ArrayList<String> shopselect= new ArrayList<>();
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        search = (LinearLayout) findViewById(R.id.search);
        barcode = (LinearLayout) findViewById(R.id.barcode);
        shoponline = (LinearLayout) findViewById(R.id.shoponline);
        history = (LinearLayout) findViewById(R.id.history);
        locationtext = (EditText) findViewById(R.id.locationview);

        toolbar.setTitle("HyperMart");
        setSupportActionBar(toolbar);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase2 = mFirebaseInstance.getReference("shop");
        mFirebaseDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Shop shop = snapshot.getValue(Shop.class);
                    shops.add(shop.name + ", " + shop.place);
                    locationtext.setText(shops.get(0));
                    shopselect.add(shop.mobile);
                }
                locationtext.setText(shops.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (shops.size()==0) {
            locationtext.setText("No Shops Found");
        } else {
            locationtext.setText(shops.get(0));
        }

        locationtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog = new SpinnerDialog(MainScreen1.this, shops, "Select or Search Shops", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation
                spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        locationtext.setText(item);
                        pos=position;
                    }
                });
                spinnerDialog.showSpinerDialog();
            }
        });

        // get reference to 'users' node

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :

        setSliderViews();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Shop Online", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        search.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        search.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(MainScreen1.this, "search", Toast.LENGTH_SHORT).show();
                        //set color back to default
                        search.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        barcode.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        barcode.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent intent=new Intent(MainScreen1.this, BarcodeScan.class);
                        intent.putExtra("shop",shopselect.get(pos));
                        startActivity(intent);
                        //set color back to default
                        barcode.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        shoponline.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shoponline.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(MainScreen1.this, "Shop Online", Toast.LENGTH_SHORT).show();
                        //set color back to default
                        shoponline.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });
        history.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        history.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(MainScreen1.this, "History", Toast.LENGTH_SHORT).show();
                        //set color back to default
                        history.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });


    }

    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            SliderView sliderView = new SliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.banner1);
                    //sliderView.setImageUrl("http://eshopinoffers.com/wp-content/uploads/2018/01/almanama-offers.png");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.banner2);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.banner3);
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.banner4);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            //sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(MainScreen1.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart) {

            return true;
        }
        if (id == R.id.search) {
            spinnerDialog = new SpinnerDialog(MainScreen1.this, shops, "Select or Search User", R.style.DialogAnimations_SmileWindow, "Close");// With 	Animation
            spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    locationtext.setText(item);
                    pos=position;
                }
            });
            spinnerDialog.showSpinerDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.barcode) {

            Intent intent=new Intent(MainScreen1.this, BarcodeScan.class);
            intent.putExtra("shop",shopselect.get(pos));
            startActivity(intent);
        } else if (id == R.id.shoponline) {
            Toast.makeText(this, "Shop online clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.regasseller) {
            regseller();

            //Toast.makeText(this, "Register clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.contactus) {
            Toast.makeText(this, "Contact us clicked", Toast.LENGTH_SHORT).show();
        }else if (id==R.id.logout){
            showalert();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showalert() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainScreen1.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainScreen1.this);
        }
        builder.setTitle("LogOut")
                .setMessage("Are you sure you want to Logout")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        SharedPreferences preferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(MainScreen1.this, Mainpage.class));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Custom dialog");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void regseller() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.customalert);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final EditText shopname = dialog.findViewById(R.id.shopname);
        final EditText mobile = dialog.findViewById(R.id.contact);
        final EditText place = dialog.findViewById(R.id.place);


        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
                mFirebaseDatabase = mFirebaseInstance.getReference("users").child(sharedPreferences.getString("mobile", ""));
                Map<String, Object> updates = new HashMap<>();
                updates.put("acctype", "shop");
                mFirebaseDatabase.updateChildren(updates);
                Shop shop = new Shop(sharedPreferences.getString("mobile", ""), shopname.getText().toString(), place.getText().toString(), mobile.getText().toString());
                mFirebaseDatabase1 = mFirebaseInstance.getReference("shop");
                mFirebaseDatabase1.child(sharedPreferences.getString("mobile", "")).setValue(shop);

                dialog.dismiss();
                SharedPreferences preferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(MainScreen1.this, Login.class));
                finish();
            }
        });

        dialog.show();
    }
}
