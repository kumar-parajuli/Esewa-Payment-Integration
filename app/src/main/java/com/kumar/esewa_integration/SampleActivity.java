package com.kumar.esewa_integration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;
import com.kumar.esewa_integration.R;

import java.util.HashMap;


/**
 * @author Aqhter
 */
public class SampleActivity extends Activity {
    private static final String CONFIG_ENVIRONMENT = EsewaConfiguration.ENVIRONMENT_TEST;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private EsewaConfiguration eSewaConfiguration;

    private static final String MERCHANT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R";
    private static final String MERCHANT_SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        Button btnPay = findViewById(R.id.btnPays);

        eSewaConfiguration = new EsewaConfiguration(
                MERCHANT_ID,
                MERCHANT_SECRET_KEY,
                CONFIG_ENVIRONMENT
);

      btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePayment("10");
            }
        });


    }

    private void makePayment(String amount) {
//        EsewaPayment eSewaPayment = new EsewaPayment(amount, "someProductName", "someUniqueId_" + System.nanoTime(), "https://somecallbackurl.com");
        EsewaPayment eSewaPayment = new EsewaPayment(
                amount,
                "someProductName",
                "someUniqueId_" + System.nanoTime(),
                "NPR", // Assuming NPR is the currency code
                new HashMap<>()
        );


        Intent intent = new Intent(SampleActivity.this, EsewaPaymentActivity.class);
        intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
        intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);
                Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show();
            } else if (resultCode == EsewaPayment.RESULT_EXTRAS_INVALID) {
                String s = data.getStringExtra(EsewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);
            }
        }
    }
}
