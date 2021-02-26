package com.example.fitnessapplication;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class Sign_Up extends Activity_Base {
    // Buttons
    private MaterialButton sign_up_BTN_login;
    private MaterialButton sign_up_BTN_signUp;

    // EditTexts
    private EditText sign_up_EDT_email;
    private EditText sign_up_EDT_ReEnterPassword;
    private EditText sign_up_EDT_password;
    private EditText sign_up_EDT_userName;

    //Validation Values
    private Validation validation;

    // Firebase
    private CollectionReference mFireStoreCollectionRef;
    private FirebaseAuth mAuth;

    //Variables
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        // Initialize validation
        validation = new Validation();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Reference to users collection
        mFireStoreCollectionRef = FirebaseFirestore.getInstance().collection("users/");
        // Initialize App Views
        findViews();
        // Initialize App Buttons
        initButtons();
    }

    private void initButtons() {
        sign_up_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLoginScreen();
            }
        });

        sign_up_BTN_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValueAndStore();
                Log.d("ff", "in sign up");
            }
        });
    }

    // Checks if the data entered by user is logically correct
    private void checkValueAndStore() {
        if (!validation.validateUserName(sign_up_EDT_userName) |
                !validation.validateEmail(sign_up_EDT_email) |
                !validation.validatePassword(sign_up_EDT_password) |
                !validation.validateSamePassword(sign_up_EDT_password, sign_up_EDT_ReEnterPassword)) {
            return;
        }

        createUser();

        String email = sign_up_EDT_email.getText().toString().trim();
        String password = sign_up_EDT_password.getText().toString().trim();

        Log.d("ff", "email");
        Log.d("ff", "username");
        Log.d("ff", "password");

        signUpUserWithEmailAndPassword(email, password);
    }

    // Sign Up User With Email & Password
    private void signUpUserWithEmailAndPassword(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // When the data entered by the user is correct
                        if (task.isSuccessful()) {
                            Log.d("ff", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Save user in FireStore
                            addNewUser(user);
                            newActivity();
                        } else {
                            if (task.getException().getMessage().contains("email")) {
                                sign_up_EDT_email.setError(task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    // Save user In DB
    private void addNewUser(FirebaseUser firebaseUser) {
        String keyId = mAuth.getCurrentUser().getUid();
        mFireStoreCollectionRef.document(keyId).set(user);
    }

    private void newActivity() {
        // After sign Up, the user can see and choose plan
        Intent intent = new Intent(getApplicationContext(), Plans.class);
        startActivity(intent);
        finish();
    }

    // Create User
    private void createUser() {
        user = new User(sign_up_EDT_userName.getText().toString(),
                sign_up_EDT_email.getText().toString(),
                sign_up_EDT_password.getText().toString(), Arrays.asList(new String[]{}));
    }

    // Move to login screen
    private void moveToLoginScreen() {
        Intent intent = new Intent(this, Sign_In.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        sign_up_EDT_userName = findViewById(R.id.sign_up_EDT_userName);
        sign_up_EDT_email = findViewById(R.id.sign_up_EDT_email);
        sign_up_EDT_password = findViewById(R.id.sign_up_EDT_password);
        sign_up_EDT_ReEnterPassword = findViewById(R.id.sign_up_EDT_ReEnterPassword);
        sign_up_BTN_signUp = findViewById(R.id.sign_up_BTN_signUp);
        sign_up_BTN_login = findViewById(R.id.sign_up_BTN_login);
    }
}