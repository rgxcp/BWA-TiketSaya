package com.rgxcp.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation splash_anim, btm_to_top;
    ImageView app_logo_color;
    TextView app_description;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Load animation
        splash_anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        btm_to_top = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);

        // Load variable
        app_logo_color = findViewById(R.id.app_logo_color);
        app_description = findViewById(R.id.app_description);

        // Run animation
        app_logo_color.startAnimation(splash_anim);
        app_description.startAnimation(btm_to_top);

        // Menjalankan getLocalUsername
        getLocalUsername();
    }

    public void getLocalUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

        if (username_key_new.isEmpty()) {
            // Membuat delay 2 detik
            Handler waktu = new Handler();
            waktu.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // menuju dari page SplashActivity ke GetStartedActivity
                    Intent goto_GetStarted = new Intent(SplashActivity.this, GetStartedActivity.class);
                    startActivity(goto_GetStarted);
                    finish();
                }
            }, 2000);
        } else {
            // Membuat delay 2 detik
            Handler waktu = new Handler();
            waktu.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // menuju dari page SplashActivity ke GetStartedActivity
                    Intent goto_Home = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(goto_Home);
                    finish();
                }
            }, 2000);
        }
    }
}
