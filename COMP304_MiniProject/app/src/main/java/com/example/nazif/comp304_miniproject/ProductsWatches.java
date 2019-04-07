/*

Filename:       ProductWatches.java
Description:    Activity that shows a ListView of the watches in the shop.

 */
package com.example.nazif.comp304_miniproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsWatches extends AppCompatActivity {

        //Invicta Men's

    ListView list;
    String[] listwatches ={"Invicta Men's"};
    Integer[] imgwatches = {R.drawable.watches};

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_products_clothing);

//        TextView price = findViewById(R.id.price);
//        SharedPreferences sharedPrefItem = getSharedPreferences("Selectionitem", Context.MODE_PRIVATE);
//        String getPrice = sharedPrefItem.getString("PriceWatch","");
//        price.setText(getPrice);

//        SharedPreferences sharedPreferences = getSharedPreferences("item",MODE_PRIVATE); //geting the selected item from the sharedPref
//        String clothing = sharedPreferences.getString("clothing", "");
//        String electronics = sharedPreferences.getString("electronics", "");
//        String books = sharedPreferences.getString("books", "");
//        String watches = sharedPreferences.getString("watches", "");
//        String movies = sharedPreferences.getString("movies", "");



        setTitle("Select item");  //setting the titlbar

        list=(ListView)findViewById(R.id.mylv);


        CustomListAdapter adapter=new CustomListAdapter(this, listwatches, imgwatches);


        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selecteditem= listwatches[+position];
                Toast.makeText(getApplicationContext(), selecteditem, Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("selection", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selecteditem",selecteditem);
                editor.putString("itemname","watch");

                String pic = imgwatches[+position].toString();

                editor.putString("selectedimg",pic);
                editor.apply();

                Intent intent = new Intent(ProductsWatches.this, ProductDetails.class);
                startActivity(intent);

            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}

