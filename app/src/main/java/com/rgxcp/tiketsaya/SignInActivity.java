package com.rgxcp.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    Button button_sign_in;
    EditText password, username;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        button_sign_in = findViewById(R.id.button_sign_in);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);

        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengubah state menjadi loading
                button_sign_in.setEnabled(false);
                button_sign_in.setText("Loading ...");

                final String j_username = username.getText().toString();
                final String j_password = password.getText().toString();

                // Validasi input kalo kosong
                if (j_username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username harus di isi!", Toast.LENGTH_SHORT).show();
                    button_sign_in.setEnabled(true);
                    button_sign_in.setText("SIGN IN");
                } else {
                    if (j_password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Password harus di isi!", Toast.LENGTH_SHORT).show();
                        button_sign_in.setEnabled(true);
                        button_sign_in.setText("SIGN IN");
                    } else {
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(j_username);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Ambil data password dari Firebase
                                    String f_password = dataSnapshot.child("password").getValue().toString();

                                    // Validasi password
                                    if (j_password.equals(f_password)) {
                                        // Menyimpan data di local storage
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, username.getText().toString());
                                        editor.apply();

                                        // Pindah activity
                                        Intent goto_Home = new Intent(SignInActivity.this, HomeActivity.class);
                                        startActivity(goto_Home);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password salah.", Toast.LENGTH_SHORT).show();
                                        button_sign_in.setEnabled(true);
                                        button_sign_in.setText("SIGN IN");
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Username tidak ditemukan.", Toast.LENGTH_SHORT).show();
                                    button_sign_in.setEnabled(true);
                                    button_sign_in.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
