package com.example.pethealthcare.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pethealthcare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name, email, password;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private static final String TAG = "RegistrationActivity";  // Debugging Tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Redirect if already logged in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }

        // Initialize UI elements
        name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        // Sign In text click listener
        TextView signInText = findViewById(R.id.textView3);
        signInText.setOnClickListener(this::signIn);
    }

    public void signUp(View view) {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(userName)) {
            showToast("Enter your name!");
            return;
        }
        if (TextUtils.isEmpty(userEmail)) {
            showToast("Enter your email!");
            return;
        }
        if (!userEmail.contains("@") || !userEmail.contains(".")) {
            showToast("Enter a valid email!");
            return;
        }
        if (TextUtils.isEmpty(userPassword) || userPassword.length() < 6) {
            showToast("Password must be at least 6 characters long!");
            return;
        }

        // Register user with Firebase Authentication
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User registered successfully");
                        String userId = auth.getCurrentUser().getUid();

                        // Prepare user data
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", userName);
                        userData.put("email", userEmail);

                        // Save to Firestore
                        firestore.collection("Users").document(userId)
                                .set(userData)
                                .addOnCompleteListener(storeTask -> {
                                    if (storeTask.isSuccessful()) {
                                        showToast("Account created successfully!");
                                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Log.e(TAG, "Firestore error: ", storeTask.getException());
                                        showToast("Failed to save user data!");
                                    }
                                });
                    } else {
                        Log.e(TAG, "Registration failed: ", task.getException());
                        showToast("Account creation failed: " + task.getException().getMessage());
                    }
                });
    }

    public void signIn(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    private void showToast(String message) {
        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
