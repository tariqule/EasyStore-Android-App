/*

Filename:       CustomCartListView.java
Description:    Used to define a custom ListView, specifically the one in the CartActivity.
                Custom ListView displays name, quantity, and price of given products.

 */

package com.example.nazif.comp304_miniproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomCartListView extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] productNames;
    private final Integer[] productQuantities;
    private final Double[] productPrices;

    // CustomCartListView constructor
    public CustomCartListView(Activity context, String[] productNames, Integer[] productQuantities, Double[] productPrices) {
        super(context, R.layout.cart_listview_single, productNames);
        this.context = context;
        this.productNames = productNames;
        this.productQuantities = productQuantities;
        this.productPrices = productPrices;
    }

    // getView method
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row= inflater.inflate(R.layout.cart_listview_single, null, true);

        // Get textviews
        TextView productView = row.findViewById(R.id.productName);
        TextView quantityView = row.findViewById(R.id.productQuantity);
        TextView priceView = row.findViewById(R.id.productPrice);

        String quantity = "Quantity : " + productQuantities[position];
        String price = String.format("$%.2f",productPrices[position]);

        // Set textview texts.
        productView.setText(productNames[position]);
        quantityView.setText(quantity);
        priceView.setText(price);
        return row;
    }
}
