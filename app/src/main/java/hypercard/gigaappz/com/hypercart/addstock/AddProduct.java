package hypercard.gigaappz.com.hypercart.addstock;

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

import es.dmoral.toasty.Toasty;
import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.model_class.Product;

public class AddProduct extends AppCompatActivity {
    TextInputLayout barcode, productname, company, price, qtyleft;
    Button addproduct;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        barcode = (TextInputLayout) findViewById(R.id.readbarcode);
        productname = (TextInputLayout) findViewById(R.id.prodname);

        company = (TextInputLayout) findViewById(R.id.companyname);
        price = (TextInputLayout) findViewById(R.id.productprice);
        qtyleft = (TextInputLayout) findViewById(R.id.quantityleft);
        sharedPreferences = getSharedPreferences("logged", Context.MODE_PRIVATE);
        addproduct = (Button) findViewById(R.id.addproduct);
        if (getIntent().hasExtra("barcode")) {
            barcode.getEditText().setText(getIntent().getStringExtra("barcode"));
        }
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("product").child(sharedPreferences.getString("mobile", ""));

        barcode.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddProduct.this, AddproductBarcode.class));
                finish();
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barcode.getEditText().getText().toString().equalsIgnoreCase("")) {
                    barcode.getEditText().setError("Scan barcode");
                } else if (productname.getEditText().getText().toString().equalsIgnoreCase("")) {
                    productname.getEditText().setError("Enter product name");
                } else if (company.getEditText().getText().toString().equalsIgnoreCase("")) {
                    company.getEditText().setError("Enter company name");
                } else if (price.getEditText().getText().toString().equalsIgnoreCase("")) {
                    price.getEditText().setError("Enter price");
                } else if (qtyleft.getEditText().getText().toString().equalsIgnoreCase("")) {
                    qtyleft.getEditText().setError("Enter quantity left in stock");
                } else {

                    Addproduct(barcode.getEditText().getText().toString(), productname.getEditText().getText().toString(), company.getEditText().getText().toString(), price.getEditText().getText().toString(), qtyleft.getEditText().getText().toString(), sharedPreferences.getString("mobile", ""));
                }
            }
        });
    }

    private void Addproduct(String barcode, String name, String company, String price, String qtyleft, String shop) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        addproduct.setEnabled(false);

        Product product = new Product(barcode, name, company, price, qtyleft, shop);

        mFirebaseDatabase.child(barcode).setValue(product);

        Toasty.success(AddProduct.this, "Product Added", Toast.LENGTH_SHORT, true).show();
        //startActivity(new Intent(AddProduct.this,Mainpage.class));
        finish();

        //addUserChangeListener();
    }
}
