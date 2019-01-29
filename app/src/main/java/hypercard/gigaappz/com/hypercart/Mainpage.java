package hypercard.gigaappz.com.hypercart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import hypercard.gigaappz.com.hypercart.login.Login;
import hypercard.gigaappz.com.hypercart.registration.Registration;

public class Mainpage extends AppCompatActivity {
    Button login,register;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        login=(Button)findViewById(R.id.start_login_btn);
        register=(Button)findViewById(R.id.start_reg_btn);
        sharedPreferences=getSharedPreferences("logged", Context.MODE_PRIVATE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Mainpage.this,Login.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Mainpage.this,Registration.class));
                finish();
            }
        });
    }


}
