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

public class SignUpThreeActivity extends AppCompatActivity {

    Animation btm_to_top, splash_anim, top_to_btm;
    Button button_explore_now;
    ImageView ic_success_sign_up;
    TextView label_congratulations, sign_up_three_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_three);

        btm_to_top = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);
        splash_anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        top_to_btm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);

        button_explore_now = findViewById(R.id.button_explore_now);
        ic_success_sign_up = findViewById(R.id.ic_success_sign_up);
        label_congratulations = findViewById(R.id.label_congratulations);
        sign_up_three_description = findViewById(R.id.sign_up_three_description);

        ic_success_sign_up.startAnimation(splash_anim);
        label_congratulations.startAnimation(top_to_btm);
        sign_up_three_description.startAnimation(top_to_btm);
        button_explore_now.startAnimation(btm_to_top);

        button_explore_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_Home = new Intent(SignUpThreeActivity.this, HomeActivity.class);
                startActivity(goto_Home);
                finish();
            }
        });
    }
}
