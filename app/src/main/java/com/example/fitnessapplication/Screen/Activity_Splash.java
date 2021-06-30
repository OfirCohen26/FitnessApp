package com.example.fitnessapplication.Screen;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

import com.example.fitnessapplication.Auth.Sign_Up;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.Utils.Activity_Base;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Splash extends Activity_Base {
    private FirebaseAuth mAuth;
    private Intent mainIntent;
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__splash);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser != null) {
            // User is signed in
            mainIntent = new Intent(Activity_Splash.this, Plans.class);
        } else {
            // No user is signed in
            mainIntent = new Intent(Activity_Splash.this, Sign_Up.class);
        }
        /* New Handler to start the Main-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Main-Activity. */
                Activity_Splash.this.startActivity(mainIntent);
                Activity_Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
