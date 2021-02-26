package com.example.fitnessapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;


public class Plan_Description extends Activity_Base implements Variables {
    // Buttons
    private MaterialButton plan_description_BTN_start;

    // ImageViews
    private ImageView plan_description_IMG_backToPlans;
    private ImageView plan_description_IMG_addOrRemovePlanToMyPlans;

    // TextViews

    private TextView plan_description_LBL_chosenPlanSign;
    private TextView plan_description_LBL_chosenPlanDescription;
    private TextView plan_description_LBL_chosenPlanTime;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private CollectionReference mFireStoreCollectionRef;
    private CollectionReference mFireStoreCollectionRefUser;

    // Variables
    private String keyId;
    private String description;
    private String planTime;
    private String planSign;

    boolean isAdded = true;

    private int remove;
    private int add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_description);
        remove = getResources().getIdentifier("minus", TYPE, FOLDER);
        add = getResources().getIdentifier("plus", TYPE, FOLDER);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get the plan that the user choose and Update UI accordingly
        getPlan();
        // Reference to shared collection
        mFireStoreCollectionRef = FirebaseFirestore.getInstance().collection("shared/");
        // Reference to users collection
        mFireStoreCollectionRefUser = FirebaseFirestore.getInstance().collection("users/");
        // Initialize App Views
        findViews();
        // Initialize App Buttons
        initButton();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            keyId = mAuth.getCurrentUser().getUid();
            updateUI();
        }
        // Check if the current plan is in User's details
        searchUidInPlans();
    }

    private void initButton() {
        plan_description_IMG_addOrRemovePlanToMyPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add or Remove plans from User detalis
                handelMyPlans();
            }
        });

        plan_description_IMG_backToPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plan_description_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToShowStepScreen();
            }
        });
    }

    // Update UI according to User's plans
    private void handelMyPlans() {
        Log.d("iS added value", String.valueOf(isAdded));
        switch (String.valueOf(isAdded)) {
            case "true":
                changeSign(remove, plan_description_IMG_addOrRemovePlanToMyPlans);
                isAdded = false;
                if (planSign.equals(A))
                    addPlanToUsersPlans(idDocA);
                else if (planSign.equals(B))
                    addPlanToUsersPlans(idDocB);
                else if (planSign.equals(C))
                    addPlanToUsersPlans(idDocC);
                break;
            case "false":
                changeSign(add, plan_description_IMG_addOrRemovePlanToMyPlans);
                isAdded = true;
                if (planSign.equals(A))
                    removePlanFromUserPlans(idDocA);
                else if (planSign.equals(B))
                    removePlanFromUserPlans(idDocB);
                else if (planSign.equals(C))
                    removePlanFromUserPlans(idDocC);
                break;
        }
    }

    // Add plan to User's details
    private void addPlanToUsersPlans(String docPath) {
        mFireStoreCollectionRef.document(docPath).update(planSign, FieldValue.arrayUnion(keyId));
        mFireStoreCollectionRefUser.document(keyId).update("my_plans", FieldValue.arrayUnion(planSign));
    }

    // Remove plan from User's details
    private void removePlanFromUserPlans(String docPath) {
        mFireStoreCollectionRef.document(docPath).update(planSign, FieldValue.arrayRemove(keyId));
        mFireStoreCollectionRefUser.document(keyId).update("my_plans", FieldValue.arrayRemove(planSign));
    }


    // Check if the current plan is in User's details
    private void searchUidInPlans() {
        mFireStoreCollectionRef.whereArrayContains(planSign, keyId).get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // True if there are no documents in the QuerySnapshot.
                            if (!task.getResult().isEmpty()) {
                                changeSign(remove, plan_description_IMG_addOrRemovePlanToMyPlans);
                                isAdded = false;
                            } else {
                                changeSign(add, plan_description_IMG_addOrRemovePlanToMyPlans);
                                isAdded = true;
                            }
                            Log.d("iS added value search", String.valueOf(isAdded));
                        }
                    }
                });

    }

    // Update Screen according to plan's info
    private void updateUI() {
        plan_description_LBL_chosenPlanSign.setText(planSign);
        description = Arrays.toString(PLAN_A);
        planTime = "18:00";
        if (planSign.equals(B)) {
            description = Arrays.toString(PLAN_B);
            planTime = "30:00";
        } else if (planSign.equals(C)) {
            description = Arrays.toString(PLAN_C);
            planTime = "20:00";
        }
        plan_description_LBL_chosenPlanTime.setText(planTime);
        plan_description_LBL_chosenPlanDescription.setText(description.replace("[", "\n").replace("]", "").replace(", ", "\n"));
    }

    public void changeSign(int id, ImageView imageView) {
        Glide
                .with(this)
                .load(id)
                .into(imageView);
    }

    private void moveToShowStepScreen() {
        Intent intent = new Intent(this, Show_Step.class);
        intent.putExtra("planSign", planSign);
        startActivity(intent);
    }

    // Get the plan that the user choose
    private void getPlan() {
        Bundle bundle = getIntent().getExtras();
        planSign = bundle.getString("planSign");
    }

    private void findViews() {
        plan_description_IMG_backToPlans = findViewById(R.id.plan_description_IMG_backToPlans);
        plan_description_LBL_chosenPlanSign = findViewById(R.id.plan_description_LBL_chosenPlanSign);
        plan_description_LBL_chosenPlanDescription = findViewById(R.id.plan_description_LBL_chosenPlanDescription);
        plan_description_BTN_start = findViewById(R.id.plan_description_BTN_start);
        plan_description_IMG_addOrRemovePlanToMyPlans = findViewById(R.id.plan_description_IMG_addOrRemovePlanToMyPlans);
        plan_description_LBL_chosenPlanTime = findViewById(R.id.plan_description_LBL_chosenPlanTime);
    }
}