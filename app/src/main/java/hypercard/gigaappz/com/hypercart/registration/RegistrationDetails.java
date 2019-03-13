package hypercard.gigaappz.com.hypercart.registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import es.dmoral.toasty.Toasty;
import hypercard.gigaappz.com.hypercart.Mainpage;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.User;
import hypercard.gigaappz.com.hypercart.model_class.Wallet;

public class RegistrationDetails extends AppCompatActivity {
    TextInputLayout mobile,password,name,place,confirm_psw;
    Button register;
    SharedPreferences sharedPreferences;
    KProgressHUD hud;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_details);
        mobile=(TextInputLayout) findViewById(R.id.mobile);
        password=(TextInputLayout) findViewById(R.id.password);
        confirm_psw=(TextInputLayout) findViewById(R.id.confirm_password);
        name=(TextInputLayout) findViewById(R.id.name);
        place=(TextInputLayout) findViewById(R.id.place);
        register=(Button) findViewById(R.id.register);
        sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mobile.getEditText().setText(getIntent().getStringExtra("mobile"));
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Hypermart");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getEditText().getText().toString().equalsIgnoreCase("")||name.getEditText().getText().toString().equalsIgnoreCase("")||place.getEditText().getText().toString().equalsIgnoreCase("")){
                    Toasty.error(RegistrationDetails.this,"All fields are mandatory", Toast.LENGTH_SHORT, true).show();
                }else if (!password.getEditText().getText().toString().equalsIgnoreCase(confirm_psw.getEditText().getText().toString())){
                    Toasty.error(RegistrationDetails.this,"Password Does not match", Toast.LENGTH_SHORT, true).show();
                }
                else {
                    register.setEnabled(false);
                    hud = KProgressHUD.create(RegistrationDetails.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(false)
                            .setLabel("Verifying Pin")
                            .show();

                    String pass=password.getEditText().getText().toString();
                    String nam=name.getEditText().getText().toString();
                    String plac=place.getEditText().getText().toString();
                    // demoRef.push().setValue(getIntent().getStringExtra("mobile"));
                    //demoRef1.push().setValue(pin);
                    String message = "password";
                    String encryptedMsg="";
                    try {
                        encryptedMsg = AESCrypt.encrypt(pass, message);
                        createUser(getIntent().getStringExtra("mobile"),encryptedMsg,nam,plac);
                    }catch (GeneralSecurityException e){
                        register.setEnabled(true);
                        //handle error
                    }

                }
            }
        });


    }

    private void createUser(String mob, String pass,String name,String place) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth


        User user = new User(mob, pass,name,place,"user");

        mFirebaseDatabase.child(mob).setValue(user);
        DatabaseReference walletref;
        walletref = mFirebaseInstance.getReference("wallet");
        Wallet wal=new Wallet(mob,"0");
        walletref.child(mob).setValue(wal);
        if (hud.isShowing()){
            hud.dismiss();
        }

        SharedPreferences.Editor editor1 = getSharedPreferences("login", MODE_PRIVATE).edit();
        editor1.putString("mobile",getIntent().getStringExtra("mobile"));
        editor1.putString("pin",pass);
        editor1.apply();


        Toasty.success(RegistrationDetails.this, "Registration Successful", Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(RegistrationDetails.this,Mainpage.class));
        finish();

        //addUserChangeListener();
    }
}
