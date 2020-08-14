package com.rgxcp.tiketsaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    Button button_edit_profile, button_save_profile;
    EditText edit_full_name, edit_bio, edit_username, edit_email, edit_password;
    ImageView edit_url_profile;

    Uri photo_location;
    Integer photo_max = 1;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        button_edit_profile = findViewById(R.id.button_edit_profile);
        button_save_profile = findViewById(R.id.button_save_profile);
        edit_full_name = findViewById(R.id.edit_full_name);
        edit_bio = findViewById(R.id.edit_bio);
        edit_username = findViewById(R.id.edit_username);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        edit_url_profile = findViewById(R.id.edit_url_profile);

        getLocalUsername();

        // Mendapatkan data lalu di letakkin di EditText dulu
        // addValueEventListener = realtime
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference().child("user_photo").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                edit_full_name.setText(dataSnapshot.child("full_name").getValue().toString());
                edit_bio.setText(dataSnapshot.child("bio").getValue().toString());
                edit_username.setText(dataSnapshot.child("username").getValue().toString());
                edit_email.setText(dataSnapshot.child("email").getValue().toString());
                edit_password.setText(dataSnapshot.child("password").getValue().toString());
                Picasso.with(EditProfileActivity.this).load(dataSnapshot.child("url_profile").getValue().toString()).centerCrop().fit().into(edit_url_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
            }
        });

        button_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengubah state menjadi loading
                button_save_profile.setEnabled(false);
                button_save_profile.setText("Loading ...");

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Kemudian baru mengubah data
                        dataSnapshot.getRef().child("full_name").setValue(edit_full_name.getText().toString());
                        dataSnapshot.getRef().child("bio").setValue(edit_bio.getText().toString());
                        dataSnapshot.getRef().child("username").setValue(edit_username.getText().toString());
                        dataSnapshot.getRef().child("email").setValue(edit_email.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(edit_password.getText().toString());

                        Intent goto_Profile = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(goto_Profile);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error.", Toast.LENGTH_SHORT).show();
                    }
                });

                // Validasi file
                if (photo_location != null) {
                    final StorageReference storageReference1 = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                    storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uri_photo = uri.toString();
                                    reference.getRef().child("url_profile").setValue(uri_photo);
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Intent goto_Profile = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                    startActivity(goto_Profile);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }
        });

        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_photo();
            }
        });
    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void find_photo() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(edit_url_profile);
        }
    }

    public void getLocalUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
