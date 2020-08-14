package com.rgxcp.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Random;

public class TicketCheckoutActivity extends AppCompatActivity {

    Button button_minus, button_pay_now, button_plus;
    ImageView alert;
    TextView current_balance, total_price, total_ticket, nama_wisata, lokasi_wisata, ketentuan_wisata;
    Integer val_total_ticket = 1;
    Integer val_current_balance = 0;
    Integer val_ticket_price = 0;
    Integer val_total_price = 0;
    Integer sisa_balance = 0;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String date = "";
    String time = "";
    Integer trans_number = new Random().nextInt();

    DatabaseReference reference, reference0, reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getLocalUsername();

        // Mengambil data dari Intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_new = bundle.getString("jenis_tiket");

        button_minus = findViewById(R.id.button_minus);
        button_pay_now = findViewById(R.id.button_pay_now);
        button_plus = findViewById(R.id.button_plus);
        alert = findViewById(R.id.alert);
        current_balance = findViewById(R.id.current_balance);
        total_price = findViewById(R.id.total_price);
        total_ticket = findViewById(R.id.total_ticket);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi_wisata = findViewById(R.id.lokasi_wisata);
        ketentuan_wisata = findViewById(R.id.ketentuan_wisata);

        // Men set default beberapa element
        total_ticket.setText(val_total_ticket.toString());
        alert.setVisibility(View.GONE);

        // Men set default button minus nya disabled
        button_minus.animate().alpha(0).setDuration(100).start();
        button_minus.setEnabled(false);

        // Mengambil data dari Firebase [0]
        reference0 = FirebaseDatabase.getInstance().getReference().child("users").child(username_key_new);
        reference0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                val_current_balance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                current_balance.setText("US$ " + val_current_balance + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        // Mengambil data dari Firebase
        reference = FirebaseDatabase.getInstance().getReference().child("wisata").child(jenis_tiket_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi_wisata.setText(dataSnapshot.child("lokasi_wisata").getValue().toString());
                ketentuan_wisata.setText(dataSnapshot.child("ketentuan_wisata").getValue().toString());
                date = dataSnapshot.child("date").getValue().toString();
                time = dataSnapshot.child("time").getValue().toString();
                val_ticket_price = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                val_total_price = val_ticket_price * val_total_ticket;
                total_price.setText("$" + val_total_price + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val_total_ticket -= 1;
                total_ticket.setText(val_total_ticket.toString());
                if (val_total_ticket < 2) {
                    button_minus.animate().alpha(0).setDuration(100).start();
                    button_minus.setEnabled(false);
                }

                val_total_price = val_ticket_price * val_total_ticket;
                total_price.setText("$" + val_total_price + "");

                if (val_total_price < val_current_balance) {
                    button_pay_now.animate().translationY(0).alpha(1).setDuration(200).start();
                    button_pay_now.setEnabled(true);
                    current_balance.setTextColor(Color.parseColor("#203DD1"));
                    alert.setVisibility(View.GONE);
                }
            }
        });
        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                val_total_ticket += 1;
                total_ticket.setText(val_total_ticket.toString());
                if (val_total_ticket > 1) {
                    button_minus.animate().alpha(1).setDuration(300).start();
                    button_minus.setEnabled(true);
                }

                val_total_price = val_ticket_price * val_total_ticket;
                total_price.setText("$" + val_total_price + "");

                if (val_total_price > val_current_balance) {
                    button_pay_now.animate().translationY(250).alpha(0).setDuration(200).start();
                    button_pay_now.setEnabled(false);
                    current_balance.setTextColor(Color.parseColor("#D1206B"));
                    alert.setVisibility(View.VISIBLE);
                }
            }
        });
        button_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menyimpan data user di Firebase [1] dan membuat tabel baru "users_ticket"
                reference1 = FirebaseDatabase.getInstance().getReference().child("users_ticket").child(username_key_new).child(nama_wisata.getText().toString() + trans_number);
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference1.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + trans_number);
                        reference1.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference1.getRef().child("lokasi_wisata").setValue(lokasi_wisata.getText().toString());
                        reference1.getRef().child("ketentuan_wisata").setValue(ketentuan_wisata.getText().toString());
                        reference1.getRef().child("jumlah_ticket").setValue(val_total_ticket.toString());
                        reference1.getRef().child("time").setValue(time);
                        reference1.getRef().child("date").setValue(date);

                        Intent goto_TicketSuccess = new Intent(TicketCheckoutActivity.this, TicketSuccessActivity.class);
                        startActivity(goto_TicketSuccess);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
                    }
                });

                // Mengubah sisa balance ketika ticket berhasil dibeli
                reference2 = FirebaseDatabase.getInstance().getReference().child("users").child(username_key_new);
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = val_current_balance - val_total_price;
                        reference2.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void getLocalUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
