/*

Filename:       ShippingInfoActivity.java
Description:    Activity that allows user to input their shipping information.

 */

package com.example.nazif.comp304_miniproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShippingInfoActivity extends AppCompatActivity {
    //create object of database helper class
    DatabaseHelper projectDB;
    //instantiating edit text and button
    EditText streetAddress, cityname, provinceName, postalCode;
    Button btnConfirm;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_info);

        projectDB= new DatabaseHelper(this);
        // streetAddress=findViewById(R.id.home_address);

        // get editViews
        cityname=findViewById(R.id.city);
        provinceName=findViewById(R.id.province);
        postalCode=findViewById(R.id.postal_code);
        streetAddress=findViewById(R.id.street_address);
        btnConfirm=findViewById(R.id.btnShipping);

        // Confirm valid inputs on button click
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=streetAddress.getText().toString().trim();
                String city=cityname.getText().toString().trim();
                String province=provinceName.getText().toString().trim();
                String zipCode=postalCode.getText().toString().trim();

                if(TextUtils.isEmpty(address)) {
                    streetAddress.setError("Please enter street address");
                    return;
                }
                if(TextUtils.isEmpty(city)) {
                    cityname.setError("Please enter your city ");
                    return;
                }
                if(TextUtils.isEmpty(province)) {
                    provinceName.setError("Please enter your province");
                    return;
                }
                if(TextUtils.isEmpty(zipCode)) {
                    postalCode.setError("Please enter postal code ");
                    return;
                }
                // calling the method from database helper class
                long values=projectDB.insertShippingAddress(address, city,province,zipCode);
                if (values> 0) {

                    // Clear user's cart upon successful confirmation of shipping
                    SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
                    String user = sharedPreferences.getString("user","");
                    projectDB.clearCart(user);

                    Toast.makeText(ShippingInfoActivity.this, " Your shipping is confirmed !", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getApplicationContext(), DetailsActivity.class));
                }
                else {
                    Toast.makeText(ShippingInfoActivity.this,"Error Occurred !", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
