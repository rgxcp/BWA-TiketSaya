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
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignUpTwoActivity extends AppCompatActivity {

    Button button_add_profile, button_continue;
    EditText bio, full_name;
    ImageView value_profile_sign_up;
    LinearLayout button_back;

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
        setContentView(R.layout.activity_sign_up_two);

        getLocalUsername();

        bio = findViewById(R.id.bio);
        button_back = findViewById(R.id.button_back);
        button_continue = findViewById(R.id.button_continue);
        button_add_profile = findViewById(R.id.button_add_profile);
        full_name = findViewById(R.id.full_name);
        value_profile_sign_up = findViewById(R.id.value_profile_sign_up);

        button_add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_photo();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backto_SignUpOne = new Intent(SignUpTwoActivity.this, SignUpOneActivity.class);
                startActivity(backto_SignUpOne);
                finish();
            }
        });
        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengubah state menjadi loading
                button_continue.setEnabled(false);
                button_continue.setText("Loading ...");

                // Menyimpan data di Firebase
                reference = FirebaseDatabase.getInstance().getReference().child("users").child(username_key_new);
                storage = FirebaseStorage.getInstance().getReference().child("user_photo").child(username_key_new);

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
                                    reference.getRef().child("bio").setValue(bio.getText().toString());
                                    reference.getRef().child("full_name").setValue(full_name.getText().toString());
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Intent goto_SignUpThree = new Intent(SignUpTwoActivity.this, SignUpThreeActivity.class);
                                    startActivity(goto_SignUpThree);
                                    finish();
                                }
                            });
                        }
                    });
                }
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
            Picasso.with(this).load(photo_location).centerCrop().fit().into(value_profile_sign_up);
        }
    }

    public void getLocalUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
