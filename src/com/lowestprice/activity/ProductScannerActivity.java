package com.lowestprice.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ProductScannerActivity extends ActionBarActivity implements View
    .OnClickListener {

    private EditText barcodeEtxt, productNameEtxt, quantityEtxt,
        barcodeTypeEtxt, productTypeEtxt;
    private ImageButton scanBtn;
    private Button addBtn;
    private Session session;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product);

        scanBtn = (ImageButton) findViewById(R.id.scanBarcodeButton);
        addBtn = (Button) findViewById(R.id.addToCartButton);

        barcodeEtxt = (EditText) findViewById(R.id.barcode);
        barcodeTypeEtxt = (EditText) findViewById(R.id.barcodeType);
        productNameEtxt = (EditText) findViewById(R.id.productName);
        productTypeEtxt = (EditText) findViewById(R.id.productType);
        quantityEtxt = (EditText) findViewById(R.id.quantity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String searchRequest = extras.getString(MainActivity.SEARCH_REQUEST);
            String barcodeContent = extras.getString(MainActivity.BARCODE_CONTENT);
            String barcodeType = extras.getString(MainActivity.BARCODE_TYPE);

            if (searchRequest != null && !searchRequest.isEmpty()) {
                if (Character.isLetter(searchRequest.charAt(0))) {
                    productNameEtxt.setText(searchRequest);
                } else {
                    barcodeEtxt.setText(searchRequest);
                }
            } else if (barcodeContent != null && !barcodeContent.isEmpty()) {
                barcodeEtxt.setText(barcodeContent);
                barcodeTypeEtxt.setText(barcodeType);
            }
        }

        scanBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        long time = new Date().getTime();
        Location location = new LocationHelper(this).getLocation();
        if (location == null) {
            Toast.makeText(getApplicationContext(),
                "Location isn't available!", Toast.LENGTH_SHORT).show();
        }
        session = new Session(time, getUserName(), location, null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                32.23,
                Integer.valueOf(quantityEtxt.getText().toString()),
                session.getDate()
            );
            SQLiteDatabase prodDB = prodHelper.getWritableDatabase();
            if (prodHelper.save(prodDB, productPrice) > 0) {
                String product = productNameEtxt.getText().toString() != null ?
                    "name is " + productNameEtxt.getText().toString() :
                    "barcode is " + barcodeEtxt.getText().toString();
                Toast.makeText(getApplicationContext(),
                    "Product " + product, Toast.LENGTH_SHORT).show();
            }
            prodDB.close();


            Intent intent = new Intent(this, CartListActivity.class);
            startActivity(intent);
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
                "No product data received!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}