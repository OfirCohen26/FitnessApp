package com.example.fitnessapplication;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sign_In extends AppCompatActivity {
    // Layout
    private RelativeLayout login_LAY_loading;

    //Buttons
    private MaterialButton login_BTN_signUp;
    private MaterialButton login_BTN_login;

    // EditTexts
    private EditText login_EDT_email;
    private EditText login_EDT_password;

    // Validation Values
    private Validation validation;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Initialize validation
        validation = new Validation();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize App Views
        findViews();
        // Initialize App Buttons
        initButtons();
        login_LAY_loading.setVisibility(View.GONE);
    }

    private void initButtons() {
        login_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSignUpScreen();
            }
        });

        login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValueAndStore();
            }
        });

    }

    // Checks if the data entered by user is logically correct
    private void checkValueAndStore() {
        if (!validation.validateEmail(login_EDT_email) | !validation.validatePassword(login_EDT_password)) {
            return;
        } else {
            isUserExist();
        }
    }

    // Find user and Sign in user with Email & Password
    private void isUserExist() {
        final String userEnteredEmail = login_EDT_email.getText().toString().trim();
        final String userEnteredPassword = login_EDT_password.getText().toString().trim();

        login_LAY_loading.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ff", "Sign in success");
                            // If user exist , we read his data from DB
                            FirebaseUser user = mAuth.getCurrentUser();
                            readUserFromFireStore(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user
                            login_LAY_loading.setVisibility(View.GONE);

                            if (task.getException().getMessage().contains("email")) {
                                login_EDT_email.setError(task.getException().getMessage());
                            } else if (task.getException().getMessage().contains("password")) {
                                login_EDT_password.setError(task.getException().getMessage());
                            } else if (task.getException().getMessage().contains("no user")) {
                                login_EDT_email.setError(task.getException().getMessage());
                            }
                        }
                    }
                });

    }

    // Read user data from DB
    void readUserFromFireStore(final String userID) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users/").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                newActivity();
            }
        });
        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void newActivity() {
        Intent intent;
        intent = new Intent(getApplicationContext(), Plans.class);
        startActivity(intent);
        finish();
    }

    private void moveToSignUpScreen() {
        Intent intent = new Intent(this, Sign_Up.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_EDT_password = findViewById(R.id.login_EDT_password);
        login_BTN_signUp = findViewById(R.id.login_BTN_signUp);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_LAY_loading = findViewById(R.id.login_LAY_loading);
    }
}