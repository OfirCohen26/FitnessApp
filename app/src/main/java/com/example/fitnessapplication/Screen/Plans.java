package com.example.fitnessapplication.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.fitnessapplication.Auth.Profile;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.Utils.Activity_Base;
import com.example.fitnessapplication.Variables;
import com.google.android.material.button.MaterialButton;

public class Plans extends Activity_Base implements Variables {
    // Buttons
    private MaterialButton plans_BTN_planA;
    private MaterialButton plans_BTN_planB;
    private MaterialButton plans_BTN_planC;

    // ImageViews
    private ImageView plans_IMG_userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        findViews();
        initButton();
    }

    private void initButton() {
        plans_IMG_userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToProfileScreen();
            }
        });

        plans_BTN_planA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparePlan(A);
            }
        });

        plans_BTN_planB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparePlan(B);
            }
        });

        plans_BTN_planC.setOnClickListener(new View.OnClickListener() {
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
        Log.d("in Plans", "to Plan_Description");
    }

    private void moveToProfileScreen() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    private void findViews() {
        plans_IMG_userProfile = findViewById(R.id.plans_IMG_userProfile);
        plans_BTN_planA = findViewById(R.id.plans_BTN_planA);
        plans_BTN_planB = findViewById(R.id.plans_BTN_planB);
        plans_BTN_planC = findViewById(R.id.plans_BTN_planC);
    }
}