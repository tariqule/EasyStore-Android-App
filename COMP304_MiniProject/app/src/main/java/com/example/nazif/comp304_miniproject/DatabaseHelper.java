/*

Filename:       DatabaseHelper.java
Description:    Takes care of all database work for the app.

 */

package com.example.nazif.comp304_miniproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static  final String DATABASE_NAME="onlinestore.db";
    private static final int DATABASE_VERSION = 2;

    // registration table
    public static final String TABLE_CUSTOMER="registration";
    public static final String CUSTOMER_ID="ID";
    public static final String CUSTOMER_NAME="name";
    public static final String EMAIL_ADD="email";
    public static final String HOME_ADD="address";
    public static final String PASSWORD="password";
    public static final String PHONE_NUMBER="phonenumber";

    // checkout table
    public static final String TABLE_CHECKOUT="checkout";
    public static final String CHECKOUT_ID="checkoutid";
    public static final String CREDITCARD_NUMBER="creditnumber";
    public static final String PIN ="pinnumber";
    public static final String EXPIRY_DATE="expirydate";

    //shipping table
    public static final String TABLE_SHIPPING="shipping";
    public static final String SHIPPING_ID="shippingid";
    public static final String ADDRESS="street_address";
    public static final String CITY="city";
    public static final String PROVINCE ="province";
    public static final String POSTAL_CODE="postalcode";

    // cart table
    public static final String TABLE_CART="cart";
    public static final String CART_ID = "cartid";
    public static final String PRODUCT = "product";
    public static final String QUANTITY = "quantity";
    public static final String PRICE = "price";
    public static final String IMGID = "imgid";  //added by tariq


    // DatabaseHelper constructor
    public DatabaseHelper (Context context){
        super (context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    // onCreate method
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables
        db.execSQL("CREATE TABLE registration(ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT, address TEXT, password TEXT, phone INTEGER)");
        db.execSQL("CREATE TABLE checkout (checkoutid INTEGER PRIMARY KEY AUTOINCREMENT, creditnumber TEXT, pinnumber INTEGER, expirydate TEXT)");
        db.execSQL("CREATE TABLE shipping (shippingid INTEGER PRIMARY KEY AUTOINCREMENT, street_address TEXT, city TEXT, province TEXT, postalcode TEXT)" );
        db.execSQL("CREATE TABLE cart (cartid INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, product TEXT, quantity INTEGER, price FLOAT, imgid INTEGER)");

    }

    // onUpgrade method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPPING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);

    }

    // insertCheck method
    // For inserting data into the checkout table
    public long insertCheck(String creditCard, int pin, String expirationDate){

        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("creditnumber", creditCard);
        contentValues.put("pinnumber",pin);
        contentValues.put("expirydate",expirationDate);

        long res=database.insert("checkout",null,contentValues);
        database.close();
        return res;
    }

    // insertShippingAddress method
    // For inserting values into the shipping table
    public long insertShippingAddress(String address, String cityName, String provinceName, String zipCode)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("street_address",address);
        contentValues.put("city", cityName);
        contentValues.put("province", provinceName);
        contentValues.put("postalcode",zipCode);

        long res=database.insert("shipping",null,contentValues);
        database.close();
        return res;
    }

    // viewShipping method
    // For retrieving data from the shipping table
    public Cursor viewShipping(){

        SQLiteDatabase db = this.getWritableDatabase();
        //the sql query to return the last row inserted
        String getShipping="SELECT * FROM shipping" + " ORDER BY "  + "shippingid" +" DESC"+ "   LIMIT 1";

        Cursor shippingResult=db.rawQuery(getShipping,null);
        return shippingResult;
    }

    // viewCart method
    // For retrieve cart information through user/email from the cart table
    public Cursor viewCart(String email){
        SQLiteDatabase db = this.getWritableDatabase();

        String getCart="SELECT * FROM cart" + " WHERE email = ? ORDER BY "  + "cartid";

        Cursor cartResult=db.rawQuery(getCart,new String[]{email});
        return cartResult;
    }



    // addCart method
    // For adding data into the cart table
    // If there is already the same entry in the cart, updates it with additional quantity.
    public long addCart (String email, String product, int quantity, double price, int imgid) {

        long result;
        ContentValues contentValues= new ContentValues();

        String [] columns= {"*"};
        SQLiteDatabase readDB= getReadableDatabase();
        String selection=EMAIL_ADD + "=?" + " and " + PRODUCT + "=?";
        String [] selectionArgs= {email, product};

        // Check if the product is already in the user's cart.
        Cursor cursor = readDB.query(TABLE_CART, columns, selection, selectionArgs, null, null, null);

        SQLiteDatabase writeDB= this.getWritableDatabase();
        contentValues.put(EMAIL_ADD, email);
        contentValues.put(PRODUCT, product);
        contentValues.put(PRICE, price);
        contentValues.put(IMGID, imgid); //added by tariq



        // Item already in user's cart, so we update values by adding quantity to current value
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            contentValues.put(QUANTITY, (cursor.getInt(cursor.getColumnIndex(QUANTITY)) + quantity));
            String cartID = cursor.getInt(cursor.getColumnIndex(CART_ID))+"";
            result = writeDB.update(TABLE_CART, contentValues, CART_ID +" = ?",
                    new String[]{ cartID });
        }
        // Item not in user's cart, just add it to the cart
        else{
            contentValues.put(QUANTITY, quantity);
            result = writeDB.insert(TABLE_CART, null, contentValues);
        }

        cursor.close();
        readDB.close();
        writeDB.close();
        return result;
    }

    // clearCart method
    // Delete the cart of the given email id.
    public long clearCart(String email){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TABLE_CART, EMAIL_ADD + " = ?",
                new String[] { email });

        db.close();
        return result;
    }

    // addUser method
    // For inserting values into the user table
    public long addUser (String name, String email,String address, String password, long phone) {
        SQLiteDatabase database= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("address", address);
        contentValues.put("password", password);
        contentValues.put("phone", phone);

        long result=database.insert("registration", null, contentValues);
        database.close();
        return result;
    }

    // checkUser method
    // To check if a user already exists
    public boolean checkUser(String userEmail, String password ){
        String [] columns= {CUSTOMER_ID};
        SQLiteDatabase db= getReadableDatabase();
        String selection=EMAIL_ADD + "=?" + " and " + PASSWORD + "=?";
        String [] selectionArgs= {userEmail, password};

        Cursor cursor= db.query(TABLE_CUSTOMER, columns, selection, selectionArgs, null, null, null);
        int count=cursor.getCount();
        cursor.close();
        db.close();

        if (count >0)
            return true;

        else
            return false;
    }

//    public Cursor getAllData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query="Select * from registration";
//        // String lastrecord=" SELECT * FROM " + ADMIN_TABLE +" WHERE employeeId "+ " Max (employeeID)";
//        Cursor res=db.rawQuery(query,null);
//
//
//        // Cursor res=db.rawQuery(" select * from "+ ADMIN_TABLE, null);
//        // Cursor res=db.rawQuery("select * from "+ ADMIN_TABLE, "order by COLUMN_USER_ID DEC LIMIT 1",null)
//        return res;
//    }
}
