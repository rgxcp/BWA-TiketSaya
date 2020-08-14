package com.rgxcp.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailActivity extends AppCompatActivity {

    Button button_buy_ticket;
    ImageView url_thumbnail;
    TextView nama_wisata, lokasi_wisata, spots, wifi, festivals, desc_wisata;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        button_buy_ticket = findViewById(R.id.button_buy_ticket);
        url_thumbnail = findViewById(R.id.url_thumbnail);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi_wisata = findViewById(R.id.lokasi_wisata);
        spots = findViewById(R.id.spots);
        wifi = findViewById(R.id.wifi);
        festivals = findViewById(R.id.festivals);
        desc_wisata = findViewById(R.id.desc_wisata);

        // Mengambil data dari Intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_new = bundle.getString("jenis_tiket");

        // Mengambil data dari Firebase berdasarkan Intent
        reference = FirebaseDatabase.getInstance().getReference().child("wisata").child(jenis_tiket_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi_wisata.setText(dataSnapshot.child("lokasi_wisata").getValue().toString());
                spots.setText(dataSnapshot.child("spots").getValue().toString());
                wifi.setText(dataSnapshot.child("wifi").getValue().toString());
                festivals.setText(dataSnapshot.child("festivals").getValue().toString());
                desc_wisata.setText(dataSnapshot.child("desc_wisata").getValue().toString());
                Picasso.with(TicketDetailActivity.this).load(dataSnapshot.child("url_thubmnail").getValue().toString()).centerCrop().fit().into(url_thumbnail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        button_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_TicketCheckout = new Intent(TicketDetailActivity.this, TicketCheckoutActivity.class);
                goto_TicketCheckout.putExtra("jenis_tiket", jenis_tiket_new);
                startActivity(goto_TicketCheckout);
                finish();
            }
        });
    }
}
