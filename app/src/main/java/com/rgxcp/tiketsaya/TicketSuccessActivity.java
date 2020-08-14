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

public class TicketSuccessActivity extends AppCompatActivity {

    Animation btm_to_top, splash_anim, top_to_btm;
    Button button_dashboard, button_view_tickets;
    ImageView ic_success_buy_ticket;
    TextView label_congratulations, ticket_success_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_success);

        btm_to_top = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);
        splash_anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        top_to_btm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);

        button_dashboard = findViewById(R.id.button_dashboard);
        button_view_tickets = findViewById(R.id.button_view_tickets);
        ic_success_buy_ticket = findViewById(R.id.ic_success_buy_ticket);
        label_congratulations = findViewById(R.id.label_congratulations);
        ticket_success_description = findViewById(R.id.ticket_success_description);

        ic_success_buy_ticket.startAnimation(splash_anim);
        label_congratulations.startAnimation(top_to_btm);
        ticket_success_description.startAnimation(top_to_btm);
        button_dashboard.startAnimation(btm_to_top);
        button_view_tickets.startAnimation(btm_to_top);

        button_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_Home = new Intent(TicketSuccessActivity.this, HomeActivity.class);
                startActivity(goto_Home);
                finish();
            }
        });
        button_view_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_MyTicketDetail = new Intent(TicketSuccessActivity.this, MyTicketDetailActivity.class);
                startActivity(goto_MyTicketDetail);
                finish();
            }
        });
    }
}
