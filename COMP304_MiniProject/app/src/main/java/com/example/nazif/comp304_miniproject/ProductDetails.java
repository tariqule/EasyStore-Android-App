/*

Filename:       ProductDetails.java
Description:    Dynamic Activity to display product information to user.
                The user is allowed to add an item to the cart and view the cart from here.

 */

package com.example.nazif.comp304_miniproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ProductDetails extends AppCompatActivity {

    ImageView itemimage;
    public static TextView productinfo;  //this holds the product description
    final private int REQUEST_INTERNET = 123;

    DatabaseHelper projectDB;

    String singleParsed = ""; //not used
    String multiParse = "";
    SharedPreferences sharedPrefJson;   //this is the shared pref that get the data from the json and displays it on the product info

    String dataParsed =""; //

    // Start ----Code in not use---------------------------------------------------------//
    private InputStream OpenHttpConnection(String urlString) throws IOException{
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if(!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP Connection");
            try{
                HttpURLConnection httpURLConn = (HttpURLConnection) conn;
                httpURLConn.setAllowUserInteraction(false);
                httpURLConn.setInstanceFollowRedirects(true);
                httpURLConn.setRequestMethod("GET");
                httpURLConn.connect();
                response = httpURLConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK){
                    in = httpURLConn.getInputStream();
                }
            }
            catch (Exception ex){
                Log.d("Networkinng", ex.getLocalizedMessage());
                throw new IOException("Error connecting");
            }

        return  in;
    }
    //Stop ----Code in not use---------------------------------------------------------//

    private Bitmap DownloadImage(String URL){ //this method get the image of easyStore from online and displays it
        Bitmap bitmap = null;
        InputStream in = null;
        try{
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch (IOException e1){
            Log.d("Networking", e1.getLocalizedMessage());
        }
        return bitmap;
    }

    // DownloadImageTask method
    // Used to download an image.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls){
            return DownloadImage((urls[0]));
        }
        protected void onPostExecute(Bitmap result){
            ImageView imageView = findViewById(R.id.networkimg);
            imageView.setImageBitmap(result);
        }
    }



    //Start ----Code in not use---------------------------------------------------------//
    private String DownloadText(String URL){
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e){
            Log.d("Network", e.getLocalizedMessage());
            return "";
        }
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try{
            while ((charRead = isr.read(inputBuffer))>0){
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        }catch (IOException e){
            Log.d("Network", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return DownloadText(urls[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            productinfo=(TextView)findViewById(R.id.productinfo);
            productinfo.setText(result);
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private String ProductInfo(String word) {
        InputStream in = null;
        String strDefinition = "";
        DocumentBuilder db;

        try {
            in = OpenHttpConnection("http://services.aonaware.com/DictService/DictService.asmx/Define?word=" + word);
            Document doc = null;
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            try {
                db = dbf.newDocumentBuilder();
                doc = db.parse(in);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            doc.getDocumentElement().normalize();

            //---retrieve all the <Definition> elements---
            NodeList definitionElements =
                    doc.getElementsByTagName("Definition");

            //---iterate through each <Definition> elements---
            for (int i = 0; i < definitionElements.getLength(); i++) {
                Node itemNode = definitionElements.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    //---convert the Definition node into an Element---
                    Element definitionElement = (Element) itemNode;

                    //---get all the <WordDefinition> elements under
                    // the <Definition> element---
                    NodeList wordDefinitionElements =
                            (definitionElement).getElementsByTagName(
                                    "WordDefinition");

                    strDefinition = "";
                    //---iterate through each <WordDefinition> elements---
                    for (int j = 0; j < wordDefinitionElements.getLength(); j++) {
                        //---convert a <WordDefinition> node into an Element---
                        Element wordDefinitionElement =
                                (Element) wordDefinitionElements.item(j);

                        //---get all the child nodes under the
                        // <WordDefinition> element---
                        NodeList textNodes =
                                ((Node) wordDefinitionElement).getChildNodes();

                        strDefinition +=
                                ((Node) textNodes.item(0)).getNodeValue() + ". \n";
                    }

                }
            }
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        //---return the definitions of the word---
        return strDefinition;
    }
    private class AccessWebServiceTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return ProductInfo(urls[0]);
        }

        protected void onPostExecute(String result) {
//            productinfo=(TextView)findViewById(R.id.productinfo);
//            productinfo.setText(result.substring(12,47));
//            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }
    //Stop------------------------------------------------------------------------ Not in use-----------------------



    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productinfo = findViewById(R.id.productinfo);

        projectDB = new DatabaseHelper(this);

        // Get shared preference info
        SharedPreferences sharedPreferences = getSharedPreferences("selection",MODE_PRIVATE);
        SharedPreferences sharedPreferencesSelection = getSharedPreferences("Selectionitem",MODE_PRIVATE);
        String getProductname = sharedPreferencesSelection.getString("ProductKey","");

        final String getItemSelected = sharedPreferences.getString("selecteditem","");
        String getimage = sharedPreferences.getString("selectedimg","");
//        String getProductname = sharedPreferences.getString("itemname","");

        setTitle(getItemSelected); //setting title

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //this shows the back button the the top

        final Integer getProductimage = Integer.parseInt(getimage);   // gettimg the imagwe from the previous actibity which was in a shared pref

        ImageView productimage = findViewById(R.id.imgselected); //Initializing the product with id
        TextView selecteditem = findViewById(R.id.itemdisplay);
        TextView priceView = findViewById(R.id.price);

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(getProductimage); //converting image to bitmap
        productimage.setImageDrawable(bitmapdraw);

        selecteditem.setText(getItemSelected); //this shows the title on the product detail page


        // check network access
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);

        } else{
            //call this asynchronously
            new DownloadImageTask().execute("https://cdn.store-assets.com/s/227573/f/918709.png");
            //new DownloadTextTask().execute("http://jfdimarzio.com/test.htm");
//            new AccessWebServiceTask().execute(getProductname);
        }


//        FetchData process = new FetchData();
        new ReadJSONFeedTask().execute("https://api.myjson.com/bins/hpr2q");   //setting the URL to getJson

        sharedPrefJson = getSharedPreferences("Json",MODE_PRIVATE);   //this get the Shared pref of json for all the items
//
        String clothingItem = sharedPrefJson.getString("jsonClothing","");
        String electronicsItem = sharedPrefJson.getString("jsonElectronics","");
        String booksItem = sharedPrefJson.getString("jsonBooks","");
        String watchesItem = sharedPrefJson.getString("jsonWatches","");
        String moviesItem = sharedPrefJson.getString("jsonMovies","");

        double price = 10.00;

//        if(savedInstanceState!=null) {

        if (getProductname != null) {
            if (getProductname.equals("tshirt")) {   //getting from productname from shared pref declared in productlist activity
                productinfo.setText(clothingItem);
                price = Double.parseDouble(getString(R.string.tshirt));
            }
            else if(getProductname.equals("electronics")){
                productinfo.setText(electronicsItem);
                price = Double.parseDouble(getString(R.string.electronic));
            }
            else if(getProductname.equals("books")){
                productinfo.setText(booksItem);
                price = Double.parseDouble(getString(R.string.book));
            }
            else if(getProductname.equals("watches")){
                productinfo.setText(watchesItem);
                price = Double.parseDouble(getString(R.string.watch));
            }
            else if(getProductname.equals("movies")){
                productinfo.setText(moviesItem);
                price = Double.parseDouble(getString(R.string.movie));
            }
        }

        Button buttonAddToCart = findViewById(R.id.addtocart);  // this button is responsible for adding to cart cart

        Button ViewCart = findViewById(R.id.btn_view_cart);    // button to sent user to view cart

        priceView.setText("Price: " + String.format("$%.2f", price));

        final double cost = price;

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get username from shared prefs
                SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
                String user = sharedPreferences.getString("user","");

                // Add item to user's cart
                long result = projectDB.addCart(user,getItemSelected,1, cost, getProductimage);

                if (result> 0) {
                    Toast.makeText(ProductDetails.this, getItemSelected + " has been added to your cart!", Toast.LENGTH_SHORT).show();

                }
                else {
                        Toast.makeText(ProductDetails.this,"There was an error adding to cart !",Toast.LENGTH_SHORT).show();
                }

            }
        });

        ViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
                String user = sharedPreferences.getString("user","");

                Cursor cursor = projectDB.viewCart(user);

                if(cursor.getCount()!=0) {
                    Intent intent = new Intent(ProductDetails.this, CartActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ProductDetails.this,"Cart is Empty !",Toast.LENGTH_SHORT).show();

                }
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

    // onRequestPermissionsResult method
    // Runs every time a permission is asked for
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_INTERNET:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(ProductDetails.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // readJSONFeed method
    // used to read a JSON file from a given URL
    public String readJSONFeed(String address) {
        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        };
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream content = new BufferedInputStream(
                    urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return stringBuilder.toString();
    }

    // ReadJSONFeedTask method
    // Async task to run ReadJSONFeed method and retrieve relevant information
    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {

                sharedPrefJson = getSharedPreferences("Json",MODE_PRIVATE);

                JSONArray jsonArray = new JSONArray(result);
                Log.i("JSON", "Description " + jsonArray.length());

                //---print out the content of the json feed---
                for (int i = 0; i < jsonArray.length(); i++) {

                    //--This code not in use----//
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    singleParsed ="Product Description: " + jsonObject.get("description"); //this will get the all the descfription from the json and store in singleParsed
//                    dataParsed = dataParsed + singleParsed + "\n";  // this will display in a good format incremment all the description
                    //--This code not in use----//

                    String clothingItem = jsonArray.getJSONObject(0).get("description").toString(); // this will get the description at index 0 and store in clothing
                    String electronicsItem = jsonArray.getJSONObject(1).get("Descriptions").toString();
                    String booksItem = jsonArray.getJSONObject(3).get("Descriptions").toString();
                    String watchesItem = jsonArray.getJSONObject(2).get("Descriptions").toString();
                    String moviesItem = jsonArray.getJSONObject(4).get("Descriptions").toString();


                    SharedPreferences.Editor jsonEditor = sharedPrefJson.edit();
                    jsonEditor.putString("jsonClothing",clothingItem);
                    jsonEditor.putString("jsonElectronics",electronicsItem);
                    jsonEditor.putString("jsonBooks",booksItem);
                    jsonEditor.putString("jsonWatches",watchesItem);
                    jsonEditor.putString("jsonMovies",moviesItem);
                    jsonEditor.apply();

//                    ProductDetails.productinfo.setText(singleParsed);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
