/*

Filename:       CheckoutActivity.java
Description:    Checkout Activity, view for user to input and submit payment information.

 */

package com.example.nazif.comp304_miniproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CheckoutActivity extends AppCompatActivity {
    //create object of database helper class
    DatabaseHelper projectDB;
     //creating button object
    Button paymentButton;
    //instantiating buttons
    EditText creditCardNo, securityPin, expirayDate;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        projectDB=new DatabaseHelper(this);

        // get EditTexts
        creditCardNo=findViewById(R.id.creditcard_number);
        securityPin=findViewById(R.id.security_pin);
        expirayDate=findViewById(R.id.expiry_date);

        paymentButton=findViewById(R.id.Btnpayment);

        // Confirm valid inputs on button press
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String creditCard=creditCardNo.getText().toString().trim();
                String pin=securityPin.getText().toString().trim();
                String expirationDate=expirayDate.getText().toString().trim();
                if(TextUtils.isEmpty(creditCard)) {
                    creditCardNo.setError("Please enter your credit number ");
                    return;
                }
                if(TextUtils.isEmpty(pin)) {
                    securityPin.setError("Please enter security pin ");
                    return;
                }
                if(TextUtils.isEmpty(expirationDate)) {
                    expirayDate.setError("Please enter expiry date ");
                    return;
                }

                int pinNumber = Integer.parseInt(pin);
                // input payment into db
                long values=projectDB.insertCheck(creditCard,pinNumber,expirationDate);

                if (values> 0) {
                    // Send user to next activity if db insert was valid
                    Toast.makeText(CheckoutActivity.this, " Payment approved !", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(CheckoutActivity.this, ShippingInfoActivity.class));
                }
                else {
                    Toast.makeText(CheckoutActivity.this,"Error Occurred !", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}
