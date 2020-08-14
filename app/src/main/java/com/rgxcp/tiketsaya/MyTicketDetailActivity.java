package com.rgxcp.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetailActivity extends AppCompatActivity {

    TextView x_nama_wisata, x_lokasi_wisata, x_date, x_time, x_ketentuan_wisata;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        x_nama_wisata = findViewById(R.id.x_nama_wisata);
        x_lokasi_wisata = findViewById(R.id.x_lokasi_wisata);
        x_date = findViewById(R.id.x_date);
        x_time = findViewById(R.id.x_time);
        x_ketentuan_wisata = findViewById(R.id.x_ketentuan_wisata);

        // Mengambil data dari Intent
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        // Mengambil data dari Firebase
        reference = FirebaseDatabase.getInstance().getReference().child("wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                x_nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                x_lokasi_wisata.setText(dataSnapshot.child("lokasi_wisata").getValue().toString());
                x_date.setText(dataSnapshot.child("date").getValue().toString());
                x_time.setText(dataSnapshot.child("time").getValue().toString());
                x_ketentuan_wisata.setText(dataSnapshot.child("ketentuan_wisata").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
