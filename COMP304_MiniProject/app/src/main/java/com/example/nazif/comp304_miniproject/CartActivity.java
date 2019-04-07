/*

Filename:       CartActivity.java
Description:    Displays Cart contents of user. Retrieves Cart information from the DB.

 */

package com.example.nazif.comp304_miniproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    // Create objects
    TextView TotalValue;
    Button ClearBtn, PayBtn;
    ListView productListView;

    DatabaseHelper projectDB;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        projectDB = new DatabaseHelper(this);

        // Get username by shared prefs
        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");

        productListView = findViewById(R.id.list_product);
        TotalValue = findViewById(R.id.displayTotal);
        ClearBtn = findViewById(R.id.clearCartBtn);
        PayBtn = findViewById(R.id.payCartBtn);

        Double total = 0.0;


//        String price = (String) TotalValue.getText();


        Cursor cursor = projectDB.viewCart(user);



//        String price = cursor.getString(0);
//        SharedPreferences sharedPrefCart = getSharedPreferences("cartItem", MODE_PRIVATE);
//        SharedPreferences.Editor cartEditor = sharedPrefCart.edit();
//        cartEditor.putString("price", price);

        // Send user to checkout on pay button click
//        Total Cart Value = $0
        PayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
                SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
                String user = sharedPreferences.getString("user","");

                Cursor cursor = projectDB.viewCart(user);
//
              if(cursor.getCount()!=0) {
                    startActivity(new Intent(CartActivity.this, CheckoutActivity.class));

                          }
                     else{
                           Toast.makeText(CartActivity.this,"Cart is Empty !",Toast.LENGTH_SHORT).show();

                          }



            }
        });
//        ImageView productimg = findViewById(R.id.cartimg);
        // Get user's cart

        // Get cart information based on db output
        if (cursor.getCount() != 0) {

            ArrayList<String> productNames = new ArrayList<>();
            ArrayList<Integer> productQuantities = new ArrayList<>();
            ArrayList<Double> productPrices = new ArrayList<>();
            ArrayList<ImageView> productImages = new ArrayList<>();


            while (cursor.moveToNext()) {

                productNames.add(cursor.getString(2));
                int quantity = cursor.getInt(3);
                double singlePrice = cursor.getDouble(4);
//                int productimgs =cursor.getInt(5);


//                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(productimgs); //converting image to bitmap
//                productimg.setImageDrawable(bitmapdraw);

//                productImages.add(productimg);

                productQuantities.add(quantity);
                // Price listed is combined price of quantity of item
                productPrices.add(quantity*singlePrice);

                // Add up total cart value
                total += quantity*singlePrice;
            }




            String value = "Total Cart Value = " + String.format("$%.2f",total);



            // Set elements in the activity
            TotalValue.setText(value);

            CustomCartListView adapter = new CustomCartListView(CartActivity.this, productNames.toArray(new String[0]), productQuantities.toArray(new Integer[0]), productPrices.toArray(new Double[0]));
            productListView.setAdapter(adapter);
        }




    }

    // reset method
    public void reset(View view) {

        // Get username
        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");

        // Clear cart.
        long result = projectDB.clearCart(user);

        if (result > 0){
            Toast.makeText(CartActivity.this, "Your cart has been cleared!", Toast.LENGTH_SHORT).show();

            TotalValue.setText("Total cart value = $0");
            productListView = findViewById(R.id.list_product);
            productListView.setAdapter(null);
        }
        else{
            Toast.makeText(CartActivity.this, "There was an error clearing your cart!", Toast.LENGTH_SHORT).show();
        }
    }
}
