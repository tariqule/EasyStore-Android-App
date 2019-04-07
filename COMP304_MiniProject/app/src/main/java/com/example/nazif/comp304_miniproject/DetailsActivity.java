/*

Filename:       DetailsActivity.java
Description:    Activity to show to the user their confirmed shipping information.

 */

package com.example.nazif.comp304_miniproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    //instantiating and creating objects
    DatabaseHelper projectDB;
     Cursor cursor;
    ListView listViewShipping;
    Button btnReturnToShopping;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //setting and getting values
        listViewShipping = findViewById(R.id.list_view);
        btnReturnToShopping=findViewById(R.id.ReturnToShopping);

        projectDB = new DatabaseHelper(this);
        ArrayList<String> shippingInfo = new ArrayList<>();
        //calling the method from the database
        cursor = projectDB.viewShipping();

       // listAdapter = new ListAdapter(getApplicationContext(), R.layout.row_layout);
        shippingDetails();

        btnReturnToShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DetailsActivity.this, ProductList.class));
            }
        });
    }

    // shippingDetails method
    // Retrieves shipping data from the database
    public void shippingDetails () {
     ArrayList<String> shippingInfo= new ArrayList<>();
     Cursor cursor=projectDB.viewShipping();

      while (cursor.moveToNext()) {

          shippingInfo.add("Order Number : " +cursor.getString(0) + "\n" +
          "Street Address : " +cursor.getString(1) + "\n" +
          "City : " +cursor.getString(2) + "\n" +
          "Province : " +cursor.getString(3) + "\n" +
          "Postal Code : " +cursor.getString(4));
          ListAdapter listAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shippingInfo);
          listViewShipping.setAdapter(listAdapter);
      }

    }
}

