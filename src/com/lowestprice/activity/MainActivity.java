package com.lowestprice.activity;

import android.accounts.*;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lowestprice.R;

import java.io.IOException;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String BARCODE_TYPE = "barcodeType";
    public static final String SEARCH_REQUEST = "searchRequest";
    public static final String BARCODE_CONTENT = "barcodeContent";
    private static final String NO_DATA_MSG = "No product data received!";

    private ImageButton scanBarcodeBtn, findPriceBtn;
    private Location location;
    private EditText productNameOrBarcodeEtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findPriceBtn = (ImageButton) findViewById(R.id.findPriceButton);
        scanBarcodeBtn = (ImageButton) findViewById(R.id.scanBarcodeButton);
        productNameOrBarcodeEtxt = (EditText) findViewById(R.id.productNameOrBarcode);

        findPriceBtn.setOnClickListener(this);
        scanBarcodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == findPriceBtn.getId()) {
            Intent intent = new Intent(this, ProductScannerActivity.class);
            intent.putExtra(SEARCH_REQUEST, productNameOrBarcodeEtxt.getText().toString());
            startActivity(intent);
        } else if (view.getId() == scanBarcodeBtn.getId()) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null && scanningResult.getContents() != null) {
            Intent next = new Intent(this, ProductScannerActivity.class);

            next.putExtra(BARCODE_TYPE, scanningResult.getFormatName());
            next.putExtra(BARCODE_CONTENT, scanningResult.getContents());
            startActivity(next);
        } else {
            Toast.makeText(getApplicationContext(),
                NO_DATA_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            // Get the result of the operation from the AccountManagerFuture.
            Bundle bundle = null;
            try {
                bundle = result.getResult();
            } catch (OperationCanceledException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (AuthenticatorException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            // The token is a named value in the bundle. The name of the value
            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

            Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
            if (launch != null) {
                startActivityForResult(launch, 0);
                return;
            }
        }
    }
}
