package com.kumar.esewa_integration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.f1soft.esewapaymentsdk.EsewaConfiguration;
import com.f1soft.esewapaymentsdk.EsewaPayment;
import com.f1soft.esewapaymentsdk.ui.screens.EsewaPaymentActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PAYMENT = 1;
    private EsewaConfiguration esewaConfiguration;
    private static final String CONFIG_ENVIRONMENT = EsewaConfiguration.ENVIRONMENT_TEST;

    private static final String MERCHANT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R";
    private static final String MERCHANT_SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize eSewa configuration
        initializeEsewaConfiguration();

        // Find the button and set its click listener
        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment("1");
            }
        });
    }

    private void initializeEsewaConfiguration() {
        String encodedClientId = Base64.encodeToString(MERCHANT_ID.getBytes(), Base64.DEFAULT);
        String encodedSecretKey = Base64.encodeToString(MERCHANT_SECRET_KEY.getBytes(), Base64.DEFAULT);

        // Log the encoded credentials for debugging
        Log.i("ClientTAG", "Client ID: " + encodedClientId);
        Log.i("SecretTAG", "Secret Key: " + encodedSecretKey);

        // Initialize eSewa configuration
        esewaConfiguration = new EsewaConfiguration(
                MERCHANT_ID,
                MERCHANT_SECRET_KEY,
                CONFIG_ENVIRONMENT
        );
        // Log the environment setting for debugging
        Log.i("TAG", "Environment: " + esewaConfiguration.getEnvironment());
    }

    private void makePayment(String amount) {
        EsewaPayment eSewaPayment = new EsewaPayment(
                amount,
                "someProductName",
                "someUniqueId_" + System.nanoTime(),
                "NPR", // Assuming NPR is the currency code
                new HashMap<>()
        );
        Intent intent = new Intent(MainActivity.this, EsewaPaymentActivity.class);
        intent.putExtra(EsewaConfiguration.ESEWA_CONFIGURATION, esewaConfiguration);
        intent.putExtra(EsewaPayment.ESEWA_PAYMENT, eSewaPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            handlePaymentResult(resultCode, data);
        }
    }

    private void handlePaymentResult(int resultCode, Intent data) {
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
