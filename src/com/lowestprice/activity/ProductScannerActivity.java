package com.lowestprice.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lowestprice.LocationHelper;
import com.lowestprice.ProductPrice;
import com.lowestprice.R;
import com.lowestprice.Session;
import com.lowestprice.db.ProductPriceOpenHelper;
import com.lowestprice.db.SessionOpenHelper;

import java.util.Date;
import java.util.List;

public class ProductScannerActivity extends Activity implements View.OnClickListener {

    private EditText barcodeEtxt, productNameEtxt, priceEtxt, quantityEtxt,
        barcodeTypeEtxt, productTypeEtxt;
    private Button scanBtn, addBtn, resetBtn, saveBtn;
    private Session session;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scan);

        scanBtn = (Button) findViewById(R.id.scanButton);
        addBtn = (Button) findViewById(R.id.addButton);
        resetBtn = (Button) findViewById(R.id.resetButton);
        saveBtn = (Button) findViewById(R.id.saveButton);

        barcodeEtxt = (EditText) findViewById(R.id.barcode);
        barcodeTypeEtxt = (EditText) findViewById(R.id.barcodeType);
        productNameEtxt = (EditText) findViewById(R.id.productName);
        productTypeEtxt = (EditText) findViewById(R.id.productType);
        priceEtxt = (EditText) findViewById(R.id.price);
        quantityEtxt = (EditText) findViewById(R.id.quantity);

        scanBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        long time = new Date().getTime();
        Location location = new LocationHelper(this).getLocation();
        if (location == null) {
            Toast.makeText(getApplicationContext(),
                "Location isn't available!", Toast.LENGTH_SHORT).show();
        }
        session = new Session(time, getUserName(), location, null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == scanBtn.getId()) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        } else if (view.getId() == addBtn.getId()) {
            SessionOpenHelper sessHelper = new SessionOpenHelper(this);
            SQLiteDatabase sessDB = sessHelper.getWritableDatabase();
            Session lastSession = sessHelper.getLast(sessDB);
            if (lastSession == null || lastSession.getDate() != session.getDate()) {
                sessHelper.save(sessDB, session);
            }
            sessDB.close();
            ProductPriceOpenHelper prodHelper = new ProductPriceOpenHelper(this);
            ProductPrice productPrice = new ProductPrice(
                barcodeEtxt.getText().toString(),
                barcodeTypeEtxt.getText().toString(),
                productNameEtxt.getText().toString(),
                productTypeEtxt.getText().toString(),
                Double.valueOf(priceEtxt.getText().toString()),
                Integer.valueOf(quantityEtxt.getText().toString()),
                session.getDate()
            );
            SQLiteDatabase prodDB = prodHelper.getWritableDatabase();
            prodHelper.save(prodDB, productPrice);
            List<ProductPrice> all = prodHelper.getAll(prodDB);
            Toast.makeText(getApplicationContext(),
                            "Product barcode is " + all.get(0).getBarcode(),Toast.LENGTH_SHORT).show();
            prodDB.close();
        } else {
            Toast.makeText(getApplicationContext(),
                "Not implemented yet!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserName() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType("com.google");
        return accounts[0].name;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && scanningResult.getContents() != null) {
            barcodeTypeEtxt.setText(scanningResult.getFormatName());
            barcodeEtxt.setText(scanningResult.getContents());
        } else {
            Toast.makeText(getApplicationContext(),
                "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }
}