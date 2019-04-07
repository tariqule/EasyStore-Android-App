/*

Filename:       ProductBooks.java
Description:    Activity that shows a ListView of the books in the shop.

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

public class ProductsBooks extends AppCompatActivity {

        //Bury Your Dead

    ListView list;
    String[] listbooks ={"Bury Your Dead"};
    Integer[] imgbooks = {R.drawable.pbooks};

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);


        setContentView(R.layout.activity_products_clothing);

        setTitle("Select item");  //setting the titlbar

        list=(ListView)findViewById(R.id.mylv);


        CustomListAdapter adapter=new CustomListAdapter(this, listbooks, imgbooks);


        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selecteditem= listbooks[+position];
                Toast.makeText(getApplicationContext(), selecteditem, Toast.LENGTH_SHORT).show();
//                SharedPreferences sharedPref = getSharedPreferences("", )
                SharedPreferences sharedPreferences = getSharedPreferences("selection", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selecteditem",selecteditem);
                editor.putString("itemname","book");

                String pic = imgbooks[+position].toString();

                editor.putString("selectedimg",pic);
                editor.apply();

                Intent intent = new Intent(ProductsBooks.this, ProductDetails.class);
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
