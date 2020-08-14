package com.rgxcp.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ImageView value_profile_home;
    LinearLayout button_destination_pisa, button_destination_torii, button_destination_pagoda, button_destination_borobudur, button_destination_sphinx, button_destination_monas;
    TextView bio, full_name, user_balance;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getLocalUsername();

        button_destination_pisa = findViewById(R.id.button_destination_pisa);
        button_destination_torii = findViewById(R.id.button_destination_torii);
        button_destination_pagoda = findViewById(R.id.button_destination_pagoda);
        button_destination_borobudur = findViewById(R.id.button_destination_borobudur);
        button_destination_sphinx = findViewById(R.id.button_destination_sphinx);
        button_destination_monas = findViewById(R.id.button_destination_monas);
        value_profile_home = findViewById(R.id.value_profile_home);
        bio = findViewById(R.id.bio);
        full_name = findViewById(R.id.full_name);
        user_balance = findViewById(R.id.user_balance);

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                full_name.setText(dataSnapshot.child("full_name").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("US$ " + dataSnapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeActivity.this).load(dataSnapshot.child("url_profile").getValue().toString()).centerCrop().fit().into(value_profile_home);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        value_profile_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_Profile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(goto_Profile);
                finish();
            }
        });

        button_destination_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_TicketDetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goto_TicketDetail.putExtra("jenis_tiket", "Pisa");
                startActivity(goto_TicketDetail);
                finish();
            }
        });
        button_destination_torii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_TicketDetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goto_TicketDetail.putExtra("jenis_tiket", "Torii");
                startActivity(goto_TicketDetail);
                finish();
            }
        });
        button_destination_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_TicketDetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goto_TicketDetail.putExtra("jenis_tiket", "Pagoda");
                startActivity(goto_TicketDetail);
                finish();
            }
        });
        button_destination_borobudur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_TicketDetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goto_TicketDetail.putExtra("jenis_tiket", "Borobudur");
                startActivity(goto_TicketDetail);
                finish();
            }
        });
        button_destination_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_TicketDetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goto_TicketDetail.putExtra("jenis_tiket", "Sphinx");
                startActivity(goto_TicketDetail);
                finish();
            }
        });
        button_destination_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_TicketDetail = new Intent(HomeActivity.this, TicketDetailActivity.class);
                goto_TicketDetail.putExtra("jenis_tiket", "Monas");
                startActivity(goto_TicketDetail);
                finish();
            }
        });
    }

    public void getLocalUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
