/*

Filename:       WelcomeActivity.java
Description:    Startup activity for the app. Sends user to SlideViewActivity upon finished load (animation
                finished).

 */

package com.example.nazif.comp304_miniproject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Define Progress bar animations
        ObjectAnimator progressAnimator;
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(1000);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView img = findViewById(R.id.es);
        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        img.startAnimation(animZoomIn);

        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 1000, 0);

        //Sets the duration of the progress bar animation.
        progressAnimator.setDuration(2000);
        //Start the animation.
        progressAnimator.start();

        progressAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            //When the animation ends, it will load the main activity.
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), SlideViewActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

}
