package com.lowestprice.activity;

import android.accounts.*;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.lowestprice.R;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button scanBarcodeBtn, findPriceBtn;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findPriceBtn = (Button) findViewById(R.id.findPrice);
        scanBarcodeBtn = (Button) findViewById(R.id.scanBarcode);
        findPriceBtn.setOnClickListener(this);
        scanBarcodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.findPrice) {
            Toast.makeText(getApplicationContext(),
                "Not implemented yet!", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.scanBarcode) {
            Intent intent = new Intent(this, ProductScannerActivity.class);
            startActivity(intent);
        }
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
