package com.example.nazif.comp304_miniproject;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;






//THIS ACTIVITY IS NOT IN USE









public class MainActivity extends AppCompatActivity {
    //instantiating buttons
    private Button joinNowBt, loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActionBar actionBar = getSupportActionBar();
//actionBar.hide();
        //setting and getting values for the buttons from xml
         joinNowBt=findViewById(R.id.btn_Signup);
         loginBtn=findViewById(R.id.btn_Login);
        //onclick listener for the buttons and view activities
         joinNowBt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(MainActivity.this,RegisterActivity.class));
             }
         });
         loginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(MainActivity.this, LoginActivity.class));
             }
         });
    }

}
