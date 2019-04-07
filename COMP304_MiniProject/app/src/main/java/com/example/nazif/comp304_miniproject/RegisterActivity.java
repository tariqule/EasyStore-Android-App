/*

Filename:       RegisterActivity.java
Description:    Activity that allows a user to register. Validates all inputs and also uses SMS service
                to confirm the user's phone number.

 */

package com.example.nazif.comp304_miniproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

public class RegisterActivity extends AppCompatActivity implements PhoneDialogFragment.PhoneDialogListener{
    DatabaseHelper projectDB;
    private Button btnRegister;

    EditText userName, emailId, homeAddress, userPassword, confirmPassword, phoneNumber;

    private String username, emailAdd, address,password,phone;

    private static final int PERMISSION_REQUEST_CODE = 123;

    private int code;

    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Sign Up");
        projectDB= new DatabaseHelper(this);

        // get all editTexts
        userName=findViewById(R.id.name);
        emailId=findViewById(R.id.Email_address);
        homeAddress=findViewById(R.id.address);
        userPassword=findViewById(R.id.login_password);
        confirmPassword=findViewById(R.id.confirm_password);
        phoneNumber = findViewById(R.id.Phone_number);


        btnRegister=findViewById(R.id.Btncreate_account);

        // Validate editText inputs on button click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=userName.getText().toString().trim();
                emailAdd=emailId.getText().toString().trim();
                address=homeAddress.getText().toString().trim();
                password=userPassword.getText().toString().trim();
                String passwordMatch=confirmPassword.getText().toString().trim();
                phone=phoneNumber.getText().toString().trim();

                //validation and to to warn user to enter details
                if(TextUtils.isEmpty(username)) {
                    userName.setError("Please enter your name ");
                    return;
                }
                if(TextUtils.isEmpty(emailAdd)) {
                    emailId.setError("Please enter your email ");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    userPassword.setError("Please enter password ");
                    return;
                }
                if(TextUtils.isEmpty(passwordMatch)) {
                    confirmPassword.setError("Please confirm your password ");
                    return;
                }
                if(TextUtils.isEmpty(phone)||TextUtils.isDigitsOnly(phone)) {
                    phoneNumber.setError("Please enter your phone number ");
                    return;
                }

                if (password.equals(passwordMatch)) {

                    // Check permissions for SMS
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                        if (checkSelfPermission(Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_DENIED) {

                            String[] permissions = {Manifest.permission.SEND_SMS};

                            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                        }
                        else{
                            // Send SMS with code for phone validation
                            sendSMSCode();
                        }
                    }
                }
                else{
                    setTitle("Password did not match!");

                    Toast.makeText(RegisterActivity.this,"Password does not match !", Toast.LENGTH_LONG).show();
                }
             }
        });


        //
        // Hook up the VideoView to our UI.
        videoBG = findViewById(R.id.videoView3);

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
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current position if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
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

    // showDialog method
    // Used to create dialog for phone number confirmation as SMS usage
    public void showDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new PhoneDialogFragment();
        dialog.show(getSupportFragmentManager(), "PhoneDialogFragment");

    }

    // sendSMSCode method
    // Used to send confirmation code through SMS, given the phone number entered
    private void sendSMSCode(){

        SharedPreferences sharedPreferences = getSharedPreferences("confirm_phone",MODE_PRIVATE);
        SharedPreferences.Editor phoneEditor = sharedPreferences.edit();

        // Get random 6 digit code
        code = (int)(Math.random() * 899999 + 100000);
        String message = "Thank you for registering for EasyShop! Your EasyShop confirmation code is " + code +".";

        // Use shared prefs to store code so that the DialogFragment can test to see if input is correct
        phoneEditor.putString("code",""+code);
        phoneEditor.apply();

        // Send the text containing the confirmation code
        SmsManager sms= SmsManager.getSmsManagerForSubscriptionId(138);
        sms.sendTextMessage(phone, null, message, null, null);

        // Show the dialog
        showDialog();
    }

    // onDialogPositiveClick method
    // For dialog clicks, runs only when the input code matches the sent code. If so, add user to DB.
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        // Add User info to DB
        long val = projectDB.addUser(username,emailAdd,address,password,Long.parseLong(phone));
        if (val> 0) {
            Toast.makeText(RegisterActivity.this,"Registration Successful !", Toast.LENGTH_LONG).show();

            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));}
        else {
            Toast.makeText(RegisterActivity.this,"Error Occurred During Registration !", Toast.LENGTH_LONG).show();
        }
    }

    // onRequestPermissionResult method
    // Runs every time a permission is asked for
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(RegisterActivity.this, "SMS permission granted !", Toast.LENGTH_LONG).show();
                    sendSMSCode();
                } else {
                    Toast.makeText(RegisterActivity.this,"SMS permissions not granted, please allow SMS permission !", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

}
