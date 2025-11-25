package com.example.pethealthcare.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pethealthcare.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetDetailsFormActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private TextInputEditText etPetName, etPetAge, etPetBreed, etPetType, etHealthStatus, etAdditionalNotes;
    private RadioGroup rgParentExperience;
    private Button btnSubmit, btnAddPhoto;
    private ImageView imgPetPhoto;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details_form);

        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("pet_images");

        etPetName = findViewById(R.id.etPetName);
        etPetAge = findViewById(R.id.etPetAge);
        etPetBreed = findViewById(R.id.etPetBreed);
        etPetType = findViewById(R.id.etPetType);
        etHealthStatus = findViewById(R.id.etHealthStatus);
        etAdditionalNotes = findViewById(R.id.etAdditionalNotes);
        rgParentExperience = findViewById(R.id.rgParentExperience);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnAddPhoto = findViewById(R.id.btnAddPhoto);
        imgPetPhoto = findViewById(R.id.imgPetPhoto);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissions();
        }

        btnAddPhoto.setOnClickListener(v -> openFileChooser());
        btnSubmit.setOnClickListener(v -> savePetDetails());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgPetPhoto.setImageURI(imageUri);
            Log.d("ImageUpload", "Image selected: " + imageUri.toString());
        } else {
            Log.e("ImageUpload", "Image selection failed");
        }
    }

    private void savePetDetails() {
        String petName = etPetName.getText().toString().trim();
        String petAge = etPetAge.getText().toString().trim();
        String petBreed = etPetBreed.getText().toString().trim();
        String petType = etPetType.getText().toString().trim();
        String healthStatus = etHealthStatus.getText().toString().trim();
        String additionalNotes = etAdditionalNotes.getText().toString().trim();

        int selectedId = rgParentExperience.getCheckedRadioButtonId();
        String parentExperience = (selectedId == R.id.rbNewParent) ? "New" : "Experienced";

        if (TextUtils.isEmpty(petName) || TextUtils.isEmpty(petAge) || TextUtils.isEmpty(petBreed) || TextUtils.isEmpty(petType)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String petId = db.collection("pets").document().getId();

        if (imageUri == null) {
            saveDataToFirestore(petId, userId, petName, petAge, petBreed, petType, healthStatus, parentExperience, additionalNotes, null);
        } else {
            uploadImageAndSaveData(petId, userId, petName, petAge, petBreed, petType, healthStatus, parentExperience, additionalNotes);
        }
    }

    private void uploadImageAndSaveData(String petId, String userId, String petName, String petAge, String petBreed, String petType,
                                        String healthStatus, String parentExperience, String additionalNotes) {
        StorageReference fileReference = storageRef.child(UUID.randomUUID().toString());

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Log.d("ImageUpload", "Download URL: " + uri.toString());
                    saveDataToFirestore(petId, userId, petName, petAge, petBreed, petType, healthStatus, parentExperience, additionalNotes, uri.toString());
                }))
                .addOnFailureListener(e -> {
                    Log.e("ImageUpload", "Upload failed: " + e.getMessage());
                    Toast.makeText(PetDetailsFormActivity.this, "Image Upload Failed!", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveDataToFirestore(String petId, String userId, String petName, String petAge, String petBreed, String petType,
                                     String healthStatus, String parentExperience, String additionalNotes, String imageUrl) {

        Map<String, Object> petData = new HashMap<>();
        petData.put("id", petId);
        petData.put("userid", userId); // ✅ Storing the current logged-in user ID
        petData.put("name", petName);
        petData.put("age", petAge);
        petData.put("breed", petBreed);
        petData.put("type", petType);
        petData.put("healthStatus", healthStatus);
        petData.put("parentExperience", parentExperience);
        petData.put("additionalNotes", additionalNotes);

        if (imageUrl != null) {
            petData.put("imageUrl", imageUrl);
        }

        db.collection("pets").document(petId).set(petData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Data saved successfully");

                    runOnUiThread(() -> {
                        Toast.makeText(PetDetailsFormActivity.this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PetDetailsFormActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });

                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Data save failed: " + e.getMessage());
                    Toast.makeText(PetDetailsFormActivity.this, "Failed to Save Data!", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}
