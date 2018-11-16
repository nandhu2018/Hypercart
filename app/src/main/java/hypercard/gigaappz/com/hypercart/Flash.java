package hypercard.gigaappz.com.hypercart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
                if (sharedPreferences.contains("mobile"))
                {
                    startActivity(new Intent(Flash.this,MainScreen1.class));
                    finish();
                }else{
                    startActivity(new Intent(Flash.this,Mainpage.class));
                    finish();
                }

            }
        }, 3000);

    }
}
