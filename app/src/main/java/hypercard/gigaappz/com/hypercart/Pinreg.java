package hypercard.gigaappz.com.hypercart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class Pinreg extends AppCompatActivity {
    EditText pin1,pin2;
    Button submit;
    KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinreg);
        pin1=(EditText) findViewById(R.id.pin1);
        pin2=(EditText) findViewById(R.id.pin2);
        submit=findViewById(R.id.btn_verify_pin);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin1.getText().toString().equalsIgnoreCase(pin2.getText().toString())||pin1.getText().length()==4){
                    submit.setEnabled(false);
                    hud = KProgressHUD.create(Pinreg.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setCancellable(false)
                            .setLabel("Registering Pin")
                            .show();
                    SharedPreferences.Editor editor1 = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor1.putString("pin",pin2.getText().toString());
                    editor1.apply();
                  //  makeJsonRequest(getIntent().getStringExtra("mobile"),pin1.getText().toString());
                }else{
                    if (hud.isShowing()){
                        hud.dismiss();
                    }
                    Toasty.error(Pinreg.this, "Enter 4 digit pin correctly", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

/*    public void makeJsonRequest(final String mobile, final String pin) {

        String urlJsonObj = "http://tradewatch.xyz/signUpStep1.php";
        JSONObject obj = new JSONObject();
        try {
            obj.put("auth", "qp^&#ss");
            obj.put("user_id", mobile);
            obj.put("password", pin);


        } catch (JSONException e) {
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    // Parsing json object response
                    // response will be a json object

                    boolean status=response.getBoolean("responseStatus");
                    if (status){
                        *//*SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                        editor.putString("mobile",mobile);
                        editor.putString("pin",pin);
                        editor.apply();*//*
                        if (hud.isShowing()){
                            hud.dismiss();
                        }
                        Toasty.success(Pinreg.this, "Pin Registered", Toast.LENGTH_SHORT, true).show();
                        startActivity(new Intent(Pinreg.this,Login.class));
                        finish();
                    }else {
                        if (hud.isShowing()){
                            hud.dismiss();
                        }
                        Toasty.error(Pinreg.this, "Failed", Toast.LENGTH_SHORT, true).show();
                    }
                    *//*SharedPreferences.Editor editor = getSharedPreferences("login", MODE_PRIVATE).edit();
                    editor.putString("mobile",mobile);
                    editor.putString("pin",pin);
                    editor.apply();*//*
                  *//*  if (hud.isShowing()){
                        hud.dismiss();
                    }
                    Toasty.success(Pinreg.this, "Pin Registered", Toast.LENGTH_SHORT, true).show();
                    startActivity(new Intent(Pinreg.this,Login.class));
                    finish();*//*
                    submit.setEnabled(true);

                    //txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    if (hud.isShowing()){
                        hud.dismiss();
                    }
                    submit.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // hide the progress dialog
                submit.setEnabled(true);
                if (hud.isShowing()){
                    hud.dismiss();
                }
            }

        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }*/
}
