package com.example.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ImageView imageViewProfile,imageButtonHomeProfile;
    private Button buttonChangePhoto;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private TextView textViewName, textViewEmail;


    @Override
    protected void onCreate(Bundle savedIstanceState) {
        super.onCreate(savedIstanceState);
        setContentView(R.layout.activity_profile);

        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageButtonHomeProfile = findViewById(R.id.imageButtonHomeProfile);
        buttonChangePhoto = findViewById(R.id.buttonChangePhoto);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());

        imageViewProfile = findViewById(R.id.imageViewProfile);
        textViewName = findViewById(R.id.textViewNameProfile);
        textViewEmail = findViewById(R.id.textViewEmailProfile);

        imageButtonHomeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });

        buttonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

            }
        });



        loadUserProfile();
    }


    private void loadUserProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String imageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                    textViewName.setText(name);
                    textViewEmail.setText(email);

                    // Cargar la imagen de perfil
                    if (imageUrl != null) {
                       //                     Glide.with(ProfileActivity.this).load(imageUrl).into(imageViewProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejo de errores
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            imageViewProfile.setImageURI(selectedImageUri);
        }


}}
