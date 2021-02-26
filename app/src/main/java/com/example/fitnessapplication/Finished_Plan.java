package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class Finished_Plan extends Activity_Base{
    // Buttons
    MaterialButton finished_plan_BTN_back_to_PlanDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_plan);
        // Initialize App Views
        findView();
        // Initialize App Buttons
        initButton();
    }

    private void initButton(){
        finished_plan_BTN_back_to_PlanDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findView() {
        finished_plan_BTN_back_to_PlanDes = findViewById(R.id.finished_plan_BTN_back_to_PlanDes);
    }
}