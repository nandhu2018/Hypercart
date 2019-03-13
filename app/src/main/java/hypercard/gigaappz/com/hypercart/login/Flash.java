package hypercard.gigaappz.com.hypercart.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hypercard.gigaappz.com.hypercart.Mainpage;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.admin.AdminActivity;
import hypercard.gigaappz.com.hypercart.shop.ShopActivity;
import hypercard.gigaappz.com.hypercart.user.MainScreen1;

public class Flash extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        sharedPreferences=getSharedPreferences("logged", Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (sharedPreferences.contains("acctype"))
                {
                    if (sharedPreferences.getString("acctype","").equalsIgnoreCase("user")){
                        startActivity(new Intent(Flash.this,MainScreen1.class));
                        finish();
                    }else if (sharedPreferences.getString("acctype","").equalsIgnoreCase("shop")){
                        startActivity(new Intent(Flash.this,ShopActivity.class));
                        finish();
                    }else if (sharedPreferences.getString("acctype","").equalsIgnoreCase("admin")){
                        startActivity(new Intent(Flash.this,AdminActivity.class));
                        finish();
                    }

                }else{
                    startActivity(new Intent(Flash.this,Mainpage.class));
                    finish();
                }

            }
        }, 3000);

    }
}
