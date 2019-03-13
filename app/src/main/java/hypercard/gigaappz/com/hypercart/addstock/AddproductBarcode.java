package hypercard.gigaappz.com.hypercart.addstock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.interfaces.RemoveClickListner;
import info.androidhive.barcode.BarcodeReader;

public class AddproductBarcode extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener,RemoveClickListner {
    BarcodeReader barcodeReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct_barcode);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
    }

    @Override
    public void OnRemoveClick(int index) {

    }

    @Override
    public void onScanned(Barcode barcode) {
        barcodeReader.playBeep();
        Intent barcodescan=new Intent(AddproductBarcode.this, AddProduct.class);
        barcodescan.putExtra("barcode",barcode.displayValue);
        startActivity(barcodescan);
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();

    }
}
