package com.ark.android.weatherapp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.ark.android.weatherapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView sun = (ImageView) findViewById(R.id.sun);
        Animation sunRise = AnimationUtils.loadAnimation(this, R.anim.sun_rise);
        sun.startAnimation(sunRise);

        sunRise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ImageView clock = (ImageView) findViewById(R.id.clock);
        Animation clockTurn = AnimationUtils.loadAnimation(this, R.anim.clock_turn);
        clock.startAnimation(clockTurn);

        ImageView hour = (ImageView) findViewById(R.id.hour);
        //get the hour turn animation
        Animation hourTurn = AnimationUtils.loadAnimation(this, R.anim.hour_turn);
        //apply the animation to the View
        hour.startAnimation(hourTurn);

    }
}
