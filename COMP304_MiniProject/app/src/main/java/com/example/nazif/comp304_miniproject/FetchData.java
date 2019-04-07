package com.example.nazif.comp304_miniproject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;




//-----------------------------------------
//THIS ACTIVITY IS NOT IN USE
//-----------------------------------------










public class FetchData extends AsyncTask<Void, Void, Void> {

    String data;
    String dataParsed ="";
    String singlePArsed ="";
    @Override
protected Void doInBackground(Void... voids) {

    try {
        URL url = new URL("https://api.myjson.com/bins/hpr2q");

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";

        while (line!=null){
            line = bufferedReader.readLine();
        data = data + line;    //storingthe entire json in the data
        }

JSONArray JA = new JSONArray(data);


for(int i =0; i<JA.length(); i++){
    JSONObject JO = (JSONObject) JA.get(i);

singlePArsed ="Description: " + JO.getString("id");

dataParsed = dataParsed + singlePArsed + "\n";

}

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
        return null;
        }

protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        ProductDetails.productinfo.setText(this.dataParsed);
}
}