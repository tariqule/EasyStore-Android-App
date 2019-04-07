/*

Filename:       LoginActivity.java
Description:    Activity used for the user to login, saves user to SharedPreferences on login.

 */

package com.example.nazif.comp304_miniproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

public class LoginActivity extends AppCompatActivity {
    //instantiating objects
     Button loginButton;
     EditText userEmail, userPassword;
     DatabaseHelper projectDB;

    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       //creating object of database class
        projectDB = new DatabaseHelper(this);

        loginButton=findViewById(R.id.login_btn);
        userEmail=findViewById(R.id.login_email);
        userPassword=findViewById(R.id.login_password);
        //login for the register user handler
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId=userEmail.getText().toString().trim();
                String passwordLogin=userPassword.getText().toString().trim();
                //entry validation
                if(TextUtils.isEmpty(emailId)) {
                    userEmail.setError("Please enter your email ");
                    return;
                }
                if(TextUtils.isEmpty(passwordLogin)) {
                    userPassword.setError("Please enter your password ");
                    return;
                }
                boolean res= projectDB.checkUser(emailId,passwordLogin);

                if (res== true)
                {
                    // Save to shared prefs
                    SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
                    SharedPreferences.Editor userEditor =sharedPreferences.edit();
                    userEditor.putString("user",emailId);
                    userEditor.apply();
                    toastMessage("Login successful !");

                    // Login user by sending them into the app
                    startActivity(new Intent(LoginActivity.this,ProductList.class));
                }
                else {
                    setTitle("No Records Founds");
                    toastMessage("Login Error !");
                }


            }
        });

        Button helpBtn = findViewById(R.id.help);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage("Redirecting to help site");
                goToUrl ( "https://tariqule.me/contact-me");
            }
        });

        setTitle("Login");
        //
        // Hook up the VideoView to our UI.
        videoBG = findViewById(R.id.videoView2);

        // Build your video Uri
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.city); // and then finally add your video resource. Make sure it is stored
        // in the raw folder.

        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();

        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,
        // check out the Android Docs: https://developer.android.com/reference/android/widget/VideoView.html
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current posistion if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });

    }

    // toastMessage method
    // Sends a toast message to the user
    public void toastMessage(String message) {
        Toast.makeText(LoginActivity.this, message,Toast.LENGTH_LONG).show();
    }


    // onPause method
    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    // onResume method
    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }

    // onDestroy method
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
