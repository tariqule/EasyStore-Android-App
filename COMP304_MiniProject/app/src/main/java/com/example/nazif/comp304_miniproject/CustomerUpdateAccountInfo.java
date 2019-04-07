package com.example.nazif.comp304_miniproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomerUpdateAccountInfo extends AppCompatActivity {





    //THIS ACTIVITY IS NOT IN USE















    private EditText audienceEmail;
    private EditText firstName;
    private EditText lastName;
    private EditText userName;
    private EditText address;
    private EditText city;
    private EditText postalCode;
    private EditText passWord;
    //declearing buttons
    private Button audienceSign;
    private Button btnSh;
    private Button btnAudienceRecord;
    DatabaseHelper mydatabaseHelper =new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_update_account_info);
        SharedPreferences sharedPref = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences emailPref = getSharedPreferences("email",Context.MODE_PRIVATE);

        audienceEmail=findViewById(R.id.audE);
        firstName=findViewById(R.id.audFn);
        address=findViewById(R.id.audAd);
        passWord=findViewById(R.id.audPass);


//        contentValues.put("name", name);
//        contentValues.put("email", email);
//        contentValues.put("address", address);
//        contentValues.put("password", password);

//        Cursor data = mydatabaseHelper.getAllData();

        String user = sharedPref.getString("UserName", "");
        String email = emailPref.getString("email", "");
        String ln = sharedPref.getString("last", "");
        String cityP = sharedPref.getString("city", "");
        String postalP = sharedPref.getString("postal", "");
        String addressP = sharedPref.getString("address", "");

        String fn = sharedPref.getString("first","");
                 firstName.setText(fn);
                 lastName.setText(ln);
                 address.setText(addressP);
          city.setText(cityP);
         postalCode.setText(postalP);
        userName.setText(user);
        audienceEmail.setText(email);

        audienceSign=findViewById(R.id.btnupdate);
        btnSh=findViewById(R.id.btnS);
    }



    public void update(View view) {



                audienceEmail=findViewById(R.id.audE);
                firstName=findViewById(R.id.audFn);
                address=findViewById(R.id.audAd);
                passWord=findViewById(R.id.audPass);

                String emailId = audienceEmail.getText().toString();
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String uName = userName.getText().toString();
                String Address = address.getText().toString();
                String audCity = city.getText().toString();
                String zipCode = postalCode.getText().toString();
                String pass_word = passWord.getText().toString();
                if(TextUtils.isEmpty(emailId)) {
                    audienceEmail.setError("Please enter your email ");
                    return;
                }
                if(TextUtils.isEmpty(fName)) {
                    firstName.setError("Please enter your first name ");
                    return;
                }
                if(TextUtils.isEmpty(audCity)) {
                    city.setError("Please enter your city ");
                    return;
                }

                //Check if Data Inserted properly
//                boolean isUpdated = myDatabaseHelper.InsertAudienceData(emailId,fName,lName,uName,Address,audCity,zipCode,pass_word);
//                  boolean isUpdated = myDatabaseHelper.updateData(emailId,fName,lName,uName,Address,audCity,zipCode,pass_word);
                if (1==1) {
//                    startActivity(new Intent(AudienceUpdateAccountInfo.this,MovieOptions.class));

                    ToastMessage("Data updated Successfully!");
                    //ClearText();
                } else {
                    ToastMessage("Data Already Existed");
                }


    }

    private void ToastMessage(String message){
        Toast.makeText(CustomerUpdateAccountInfo.this ,message,Toast.LENGTH_LONG).show();
    }

    public void done(View v){
        startActivity(new Intent(this,ProductList.class));

    }
}
