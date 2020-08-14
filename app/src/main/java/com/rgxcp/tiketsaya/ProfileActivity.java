package com.rgxcp.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    Button button_back, button_edit_profile, button_sign_out;
    ImageView profile_photo;
    LinearLayout ticket_detail;
    TextView full_name, bio;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RecyclerView recview_myticket_place;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;

    DatabaseReference reference, reference0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getLocalUsername();

        button_back = findViewById(R.id.button_back);
        button_edit_profile = findViewById(R.id.button_edit_profile);
        button_sign_out = findViewById(R.id.button_sign_out);
        ticket_detail = findViewById(R.id.ticket_detail);
        profile_photo = findViewById(R.id.profile_photo);
        full_name = findViewById(R.id.full_name);
        bio = findViewById(R.id.bio);
        recview_myticket_place = findViewById(R.id.recview_myticket_place);
        recview_myticket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTicket>();

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_Home = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(goto_Home);
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                full_name.setText(dataSnapshot.child("full_name").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                Picasso.with(ProfileActivity.this).load(dataSnapshot.child("url_profile").getValue().toString()).centerCrop().fit().into(profile_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_EditProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(goto_EditProfile);
                finish();
            }
        });

        reference0 = FirebaseDatabase.getInstance().getReference().child("users_ticket").child(username_key_new);
        reference0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    MyTicket p = dataSnapshot1.getValue(MyTicket.class);
                    list.add(p);
                }
                ticketAdapter = new TicketAdapter(ProfileActivity.this, list);
                recview_myticket_place.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        button_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menghapus data dari lokal storage
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                Intent goto_GetStarted = new Intent(ProfileActivity.this, GetStartedActivity.class);
                startActivity(goto_GetStarted);
                finish();
            }
        });
        /*
        ticket_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_MyTicketDetail = new Intent(ProfileActivity.this, MyTicketDetailActivity.class);
                startActivity(goto_MyTicketDetail);
                finish();
            }
        });
        */
    }

    public void getLocalUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
