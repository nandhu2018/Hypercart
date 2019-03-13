package hypercard.gigaappz.com.hypercart.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import hypercard.gigaappz.com.hypercart.Mainpage;
import hypercard.gigaappz.com.hypercart.R;

public class Registration extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private Button btnRequestSms, btnVerifyOtp,btnVerifypin;
    private EditText inputName, inputEmail, inputMobile, inputOtp,enterpin,reenterpin;
    private ProgressBar progressBar;
    private PrefManager pref;
    private ImageButton btnEditMobile;
    private TextView txtEditMobile;
    private String verificationCode,otpval;
    private ViewPagerAdapter adapter;
    private LinearLayout layoutEditMobile;
    KProgressHUD hud;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth auth;
    List<String> moblist=new ArrayList<>();
    boolean iscontainmob=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

      /*  //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                inputOtp.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });*/

        //set phone number filter if needed
        //smsVerifyCatcher.setPhoneNumberFilter("59029");


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("mobile=="+snapshot.getKey());
                    User user = snapshot.getValue(User.class);
                    moblist.add(user.mobile);
                }*/
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //System.out.println("mobile=="+snapshot.getKey());
                    //User user = snapshot.getValue(User.class);
                    moblist.add(snapshot.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //Get Firebase auth instance
        viewPager = (ViewPager) findViewById(R.id.viewPagerVertical);
        inputName = (EditText) findViewById(R.id.inputName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputMobile = (EditText) findViewById(R.id.inputMobile);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        btnRequestSms = (Button) findViewById(R.id.btn_request_sms);
        btnVerifyOtp = (Button) findViewById(R.id.btn_verify_otp);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnEditMobile = (ImageButton) findViewById(R.id.btn_edit_mobile);
        txtEditMobile = (TextView) findViewById(R.id.txt_edit_mobile);
        layoutEditMobile = (LinearLayout) findViewById(R.id.layout_edit_mobile);
        StartFirebaseLogin();
        // view click listeners
        btnEditMobile.setOnClickListener(this);
        btnRequestSms.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        pref = new PrefManager(this);
// hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);
// Checking for user session
        // if user is already logged in, take him to main activity
        if (pref.isLoggedIn()) {
            Toast.makeText(this, "already logged in", Toast.LENGTH_SHORT).show();
        }

        /**
         * Checking if the device is waiting for sms
         * showing the user OTP screen
         */
        if (pref.isWaitingForSms()) {
            viewPager.setCurrentItem(1);
            layoutEditMobile.setVisibility(View.VISIBLE);
        }

        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // hiding the edit mobile number
        layoutEditMobile.setVisibility(View.GONE);
    }

    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                 Toast.makeText(Registration.this,"verification completed",Toast.LENGTH_SHORT).show();
                // moving the screen to next pager item i.e otp screen
                // boolean flag saying device is waiting for sms
                pref.setIsWaitingForSms(true);

            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (hud.isShowing()){
                    hud.dismiss();
                }
                Toasty.error(Registration.this, "verification failed"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                // hiding the progress bar
                progressBar.setVisibility(View.GONE);
                viewPager.setCurrentItem(1);
                if (hud.isShowing()){
                    hud.dismiss();
                }
                Toast.makeText(Registration.this,"Code sent", Toast.LENGTH_SHORT).show();
                //Toasty.success(Main2Activity.this, "Success!", Toast.LENGTH_SHORT, true).show();
            }
        };
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request_sms:
                if (inputMobile.getText().toString().equalsIgnoreCase("")||inputMobile.getText().length()<10){
                    inputMobile.setError("Enter correct mobile");
                }else{
                    iscontainmob=false;
                    for (int i=0;i<moblist.size();i++){
                        if (inputMobile.getText().toString().equalsIgnoreCase(moblist.get(i))){
                            iscontainmob=true;
                        }
                    }
                    if (iscontainmob){
                        Toasty.error(Registration.this, "Mobile Number Already Exists", Toast.LENGTH_SHORT, true).show();
                    }else {
                        hud = KProgressHUD.create(Registration.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setCancellable(false)
                                .setLabel("Sending otp")
                                .show();
                        validateForm();
                    }
                }


              /*

                if (acccount>0){

                }else {

                }*/

                break;

            case R.id.btn_verify_otp:
                if (inputOtp.getText().toString().equalsIgnoreCase("")){
                    inputOtp.setError("Enter otp");
                }
                hud = KProgressHUD.create(Registration.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setCancellable(false)
                        .setLabel("Verifying otp")
                        .show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, inputOtp.getText().toString());
                SigninWithPhone(credential);





                break;

            case R.id.btn_edit_mobile:
                viewPager.setCurrentItem(0);
                layoutEditMobile.setVisibility(View.GONE);
                pref.setIsWaitingForSms(false);
                break;

        }
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (hud.isShowing()){
                                hud.dismiss();
                            }
                            //Toasty.success(Registration.this, "Success!", Toast.LENGTH_SHORT, true).show();
                            /*Intent pinreg=new Intent(Main2Activity.this,Pinreg.class);
                            pinreg.putExtra("mobile",inputMobile.getText().toString());
                            startActivity(pinreg);
                            finish();*/
                            SharedPreferences.Editor editor1 = getSharedPreferences("login", MODE_PRIVATE).edit();
                            editor1.putString("mobile",inputMobile.getText().toString());
                            editor1.apply();
                            Intent pinreg=new Intent(Registration.this,RegistrationDetails.class);
                            pinreg.putExtra("mobile",inputMobile.getText().toString());
                            startActivity(pinreg);
                            finish();

                        } else {
                            if (hud.isShowing()){
                                hud.dismiss();
                            }
                            Toasty.error(Registration.this, "Incorrect OTP", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }
    private void validateForm() {
        /*String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();*/
        String mobile = inputMobile.getText().toString().trim();

        /*// validating empty name and email
        if (name.length() == 0 || email.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please enter your details", Toast.LENGTH_SHORT).show();
            return;
        }*/

        // validating mobile number
        // it should be of 10 digits length
        if (isValidPhoneNumber(mobile)) {

            // request for sms
            //progressBar.setVisibility(View.VISIBLE);


            // saving the mobile number in shared preferences
            pref.setMobileNumber(mobile);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91"+inputMobile.getText().toString(),                     // Phone number to verify
                    60,                           // Timeout duration
                    TimeUnit.SECONDS,                // Unit of timeout
                    Registration.this,        // Activity (for callback binding)
                    mCallback);




            txtEditMobile.setText(pref.getMobileNumber());
            layoutEditMobile.setVisibility(View.VISIBLE);
        } else {
            Toasty.error(Registration.this, "Please enter valid mobile number", Toast.LENGTH_SHORT, true).show();
        }
    }

    /**
     * Validating user details form
     */

    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        public Object instantiateItem(View collection, int position) {

            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.layout_sms;
                    break;
                case 1:
                    resId = R.id.layout_otp;
                    break;

            }
            return findViewById(resId);
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Registration.this,Mainpage.class));
        finish();
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }*/
/*
    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }*/

    /**
     * need for Android 6 real time permissions
     */
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
