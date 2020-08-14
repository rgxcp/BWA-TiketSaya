package com.rgxcp.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedActivity extends AppCompatActivity {

    Animation btm_to_top, top_to_btm;
    Button button_sign_in, button_new_account;
    ImageView app_emblem;
    TextView get_started_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        btm_to_top = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);
        top_to_btm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);

        button_sign_in = findViewById(R.id.button_sign_in);
        button_new_account = findViewById(R.id.button_new_account);
        app_emblem = findViewById(R.id.app_emblem);
        get_started_description = findViewById(R.id.get_started_description);

        app_emblem.startAnimation(top_to_btm);
        get_started_description.startAnimation(top_to_btm);
        button_sign_in.startAnimation(btm_to_top);
        button_new_account.startAnimation(btm_to_top);

        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_SignIn = new Intent(GetStartedActivity.this, SignInActivity.class);
                startActivity(goto_SignIn);
                finish();
            }
        });
        button_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_SignUpOne = new Intent(GetStartedActivity.this, SignUpOneActivity.class);
                startActivity(goto_SignUpOne);
                finish();
            }
        });
    }
}
