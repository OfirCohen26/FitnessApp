package com.example.fitnessapplication.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fitnessapplication.Utils.Activity_Base;
import com.example.fitnessapplication.Screen.Plan_Description;
import com.example.fitnessapplication.Screen.Plans;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.Object.User;
import com.example.fitnessapplication.Variables;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends Activity_Base implements Variables {
    // Layouts
    private LinearLayout profile_LAY_planA;
    private LinearLayout profile_LAY_planB;
    private LinearLayout profile_LAY_planC;

    // Buttons
    private MaterialButton profile_BTN_planA;
    private MaterialButton profile_BTN_planB;
    private MaterialButton profile_BTN_planC;

    // ImageViews
    private ImageView profile_IMG_logout;
    private ImageView profile_IMG_plans;

    // TextViews
    private TextView profile_LBL_userName;
    private TextView profile_LBL_email;

    //Firebase
    private CollectionReference mFireStoreCollectionRefUser;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize App Views
        findViews();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            readUserFromFireStore(currentUser.getUid());
        }
        // Initialize App Buttons
        initButton();
    }

    private void initButton() {
        profile_IMG_plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                backToPlans();
                Log.d("in Profile", "to Plans");
                finish();
            }
        });

        profile_IMG_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutCurrentUser();
            }
        });

        profile_BTN_planA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparePlan(A);
            }
        });

        profile_BTN_planB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparePlan(B);
            }
        });

        profile_BTN_planC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparePlan(C);
            }
        });
    }

    private void preparePlan(String planSign) {
        Intent intent = new Intent(this, Plan_Description.class);
        intent.putExtra("planSign", planSign);
        startActivity(intent);
        Log.d("in Profile", "to Plan description");
        finish();
    }

    private void backToPlans() {
        Intent intent = new Intent(this, Plans.class);
        startActivity(intent);
        Log.d("in Profile", "to Plans");
        finish();
    }

    // Read user data from DB
    void readUserFromFireStore(final String userID) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users/").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                updateUI(user);
            }
        });

        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    // Update User Profile details according currently signed-in user
    private void updateUI(User user) {
        profile_LBL_userName.setText(user.getUserName());
        profile_LBL_email.setText(user.getEmail());
        String plans = user.getMy_plans().toString().replace("[", "").replace("]", "").replace(", ", "");
        if (plans.contains(A))
            profile_LAY_planA.setVisibility(View.VISIBLE);
        else
            profile_LAY_planA.setVisibility(View.GONE);
        if (plans.contains(B))
            profile_LAY_planB.setVisibility(View.VISIBLE);
        else
            profile_LAY_planB.setVisibility(View.GONE);
        if (plans.contains(C))
            profile_LAY_planC.setVisibility(View.VISIBLE);
        else
            profile_LAY_planC.setVisibility(View.GONE);
    }

    //Sign out a user
    private void signOutCurrentUser() {
        mAuth.signOut();
//        Intent intent = new Intent(this, Sign_In.class);
//        startActivity(intent);
        Log.d("in Profile", "to Sign_In after logout");
        Intent intent = new Intent(getApplicationContext(), Sign_In.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }


    private void findViews() {
        profile_IMG_logout = findViewById(R.id.profile_IMG_logout);
        profile_IMG_plans = findViewById(R.id.profile_IMG_plans);
        profile_LBL_userName = findViewById(R.id.profile_LBL_userName);
        profile_LBL_email = findViewById(R.id.profile_LBL_email);
        profile_BTN_planA = findViewById(R.id.profile_BTN_planA);
        profile_BTN_planB = findViewById(R.id.profile_BTN_planB);
        profile_BTN_planC = findViewById(R.id.profile_BTN_planC);
        profile_LAY_planA = findViewById(R.id.profile_LAY_planA);
        profile_LAY_planB = findViewById(R.id.profile_LAY_planB);
        profile_LAY_planC = findViewById(R.id.profile_LAY_planC);

    }
}
