/*

Filename:       ProductClothing.java
Description:    Activity that shows a ListView of the books in the shop.

 */

package com.example.nazif.comp304_miniproject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsClothing extends AppCompatActivity {

    ListView list;
    String[] listclothing ={"White T-Shirt", "Blue T-SHirt","Hoodie",};

    Integer[] imgclothing={R.drawable.tshirt, R.drawable.tshirt2, R.drawable.tpant,};

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences sharedPreferencesSelection = getSharedPreferences("Selectionitem",MODE_PRIVATE);
//        String getProductname = sharedPreferencesSelection.getString("clothing","");



        setContentView(R.layout.activity_products_clothing);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Select item");  //setting the titlbar

        list=(ListView)findViewById(R.id.mylv);

        CustomListAdapter adapter=new CustomListAdapter(this, listclothing, imgclothing);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selecteditem= listclothing[+position]; //To get the item selected from the listview
                Toast.makeText(getApplicationContext(), selecteditem, Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("selection", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selecteditem",selecteditem);
//                editor.putString("itemname","tshirt");

               String pic = imgclothing[+position].toString();

               editor.putString("selectedimg",pic);
               editor.apply();

               Intent intent = new Intent(ProductsClothing.this, ProductDetails.class);
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
