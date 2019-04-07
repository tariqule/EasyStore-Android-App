/*

Filename:       ProductList.java
Description:    Activity that shows the logged in user all item categories in the shop.

 */

package com.example.nazif.comp304_miniproject;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;

public class ProductList extends AppCompatActivity {

    SharedPreferences sharedPrefItem;
//    TextView priceText = findViewById(R.id.price);


    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        EditText search = findViewById(R.id.search);
        setTitle("Category");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openDialog();
                //Do something after 100ms
            }
        }, 500);


    }

    // onCreateOptionsMenu method
    // To show the options menu in the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        //show the menu
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    // onOptionsItemSelected method
    // called whenever an item in your options menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent;
        //handle menu items by their id
        switch (item.getItemId())
        {

            case R.id.viewCart:
                intent = new Intent(ProductList.this,CartActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(ProductList.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                intent = new Intent(ProductList.this,SlideViewActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }


    // onclickclothing method
    // Runs when clothing is clicked
    public void onclickclothing(View view){

        String tshirt = String.valueOf((R.string.tshirt));
        //pref to store the selection to item
        sharedPrefItem = getSharedPreferences("Selectionitem", Context.MODE_PRIVATE);
        SharedPreferences.Editor itemEditor = sharedPrefItem.edit();
        itemEditor.putString("ProductKey","tshirt");
//        itemEditor.putString("PriceClothing","$"+tshirt);
         itemEditor.apply();


        Intent intent = new Intent(ProductList.this, ProductsClothing.class);
        startActivity(intent);
    }

    // onclickelectronics method
    // Runs when electronics is clicked
    public  void onclickelectronics(View view){

        String electronics = String.valueOf((R.string.electronic));

        sharedPrefItem = getSharedPreferences("Selectionitem", Context.MODE_PRIVATE);
        SharedPreferences.Editor itemEditor = sharedPrefItem.edit();
        itemEditor.putString("ProductKey", "electronics");
//        itemEditor.putString("PriceElectronics","$"+electronics);

        itemEditor.apply();


        Intent intent = new Intent(ProductList.this, ProductsElectronics.class);
        startActivity(intent);
    }

    // onclickbooks method
    // Runs when books is clicked
    public void onclickbooks(View vIew){

        String book = String.valueOf((R.string.book));

        sharedPrefItem = getSharedPreferences("Selectionitem", Context.MODE_PRIVATE);
        SharedPreferences.Editor booksseditor = sharedPrefItem.edit();
        booksseditor.putString("ProductKey", "books");
//        booksseditor.putString("PriceBook","$"+book);

        booksseditor.apply();

        Intent intent = new Intent(ProductList.this, ProductsBooks.class);

        startActivity(intent);
    }

    // onclickwatches method
    // Runs when watches is clicked
    public  void onclickwatches(View view){

        String watch = String.valueOf((R.string.watch));

        sharedPrefItem = getSharedPreferences("Selectionitem", Context.MODE_PRIVATE);
        SharedPreferences.Editor watchessseditor = sharedPrefItem.edit();
        watchessseditor.putString("ProductKey", "watches");
//        watchessseditor.putString("PriceWatch","$"+watch);

        watchessseditor.apply();

        Intent intent = new Intent(ProductList.this, ProductsWatches.class);
        startActivity(intent);
    }

    // onclickmovies method
    // Runs when movies is clicked
    public void onclickmovies(View view){
        String movie = String.valueOf((R.string.movie));


        sharedPrefItem = getSharedPreferences("Selectionitem", Context.MODE_PRIVATE);
        SharedPreferences.Editor moviessseditor = sharedPrefItem.edit();
        moviessseditor.putString("ProductKey", "movies");
//        moviessseditor.putString("PriceMovies","$"+movie);

        moviessseditor.apply();


        Intent intent = new Intent(ProductList.this, ProductsMovies.class);

        startActivity(intent);
    }

    // openDialog method
    // Opens an Alert Dialog on run to thank the user.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void openDialog() {
//TextView textUser = findViewById(R.id.editTxtUser);
//
////Putting username in the shared preference
        SharedPreferences sharedPref = getSharedPreferences("username", Context.MODE_PRIVATE);

        String name = sharedPref.getString("user", "");
        //


        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Welcome " + name);
        title.setPadding(10, 10, 10, 10);   // Set Position

        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(this);
        // Message Properties
        msg.setText("Thanks for being a member \n Choose a category");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.CENTER;
        okBT.setPadding(10, 10, 10, 10);
        okBT.setGravity(Gravity.CENTER);
        okBT.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);// Set Position
        okBT.setTextColor(Color.BLUE);

        okBT.setLayoutParams(neutralBtnLP);

    }

    // Create options menu

}
