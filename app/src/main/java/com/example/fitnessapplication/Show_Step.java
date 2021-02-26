package com.example.fitnessapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;

public class Show_Step extends Activity_Base implements Variables {
    // Layouts
    private RelativeLayout show_step_LAY_timer_for_break;
    private LinearLayout show_step_LAY_step;
    private LinearLayout show_step_LAY_firstSteps;
    private LinearLayout show_step_LAY_secondSteps;

    // Buttons
    private MaterialButton show_step_BTN_go;
    private MaterialButton show_step_BTN_step1;
    private MaterialButton show_step_BTN_step2;
    private MaterialButton show_step_BTN_step3;
    private MaterialButton show_step_BTN_step4;
    private MaterialButton show_step_BTN_step5;
    private MaterialButton show_step_BTN_step6;

    // ImageViews
    private ImageView show_step_IMG_ImgAerobic;
    private ImageView show_step_IMG_stepImg;

    // TextViews
    private TextView show_step_LBL_timeFor;
    private TextView show_step_LBL_time_for_break;
    private TextView show_step_LBL_stepDes;

    // ProgressBars
    private ProgressBar show_step_PGB_countDown;
    private ProgressBar show_step_PGB_countDown_for_break;

    // CountDown Timers
    private CountDownTimer countDownTimerForExercise;
    private CountDownTimer countDownTimerForBreak;

    // Variables
    boolean counterIsRunning1 = false;
    boolean counterIsRunning2 = false;

    //Declare a variable to hold CountDownTimer remaining time
    private long timeRemainingForTimerForExercise = 0;
    private int timeRemainingForTimerForForBreakOrAero = 0;

    //Declare a variable to hold count down timer's paused status
    private boolean isPausedTimerForExercise = false;
    private boolean isPausedTimerForBreakOrAero = false;

    boolean isVisible = true;

    String[] steps;
    String planSign;
    int stepCounter = 1;
    int rounds = 1;
    int secondsForExercise;
    int secondsForRestOrAerobic;
    private MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__step);
        getPlanSign();
        // Initialize App Views
        findViews();
        // Initialize the steps array according the plan
        initStepsArray();
        // Initialize App Buttons
        initButtons();
        // Initialize Views according the plan
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            if(mediaPlayer !=null && mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }catch(Exception e){
        }
        Log.d("onPause", "onPause");
//        isPausedTimerForExercise = true;
//        isPausedTimerForBreakOrAero = true;

        Log.d("onPause", " before countDownTimerForExercise" + countDownTimerForExercise);
//        if (counterIsRunning1 == true)
        if (countDownTimerForExercise != null) {
            Log.d("onPause", "  countDownTimerForExercise in if");
            //            isPausedTimerForExercise = true;
            countDownTimerForExercise.cancel();
            countDownTimerForExercise = null;
        }

        Log.d("onPause", "after countDownTimerForExercise" + countDownTimerForExercise);

        Log.d("onPause", "before countDownTimerForBreak" + countDownTimerForBreak);

//        if (counterIsRunning2 == true)
        if (countDownTimerForBreak != null) {
            Log.d("onPause", "  countDownTimerForBreak in if");

            //            isPausedTimerForBreakOrAero = true;
            countDownTimerForBreak.cancel();
            countDownTimerForBreak = null;
        }

        Log.d("onPause", "after countDownTimerForBreak" + countDownTimerForBreak);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer!= null)
            mediaPlayer.start();
        Log.d("onResume", "onResume");
        // Timer for Exercise
        Log.d("onResume", "before  countDownTimerForExercise" + countDownTimerForExercise);

        if (counterIsRunning1 == true) {
            if (countDownTimerForExercise == null) {
                Log.d("onResume", "in if  countDownTimerForExercise" + countDownTimerForExercise);
                // Timer was paused, re-create with saved time.
                isPausedTimerForExercise = false;
//                Log.d("onResume", "onResume" + timeRemainingForTimerForExercise);
                countDownTimerForExercise(timeRemainingForTimerForExercise);
            }
        }
        if (counterIsRunning2 == true) {
            if (countDownTimerForBreak == null) { // Timer was paused, re-create with saved time.
                Log.d("onResume", "in if  startTimerForBreakOrAerobic" + countDownTimerForExercise);
                isPausedTimerForBreakOrAero = false;
                Log.d("onResume", "timeRemainingForTimerForForBreakOrAero" + timeRemainingForTimerForForBreakOrAero);
                startTimerForBreakOrAerobic(timeRemainingForTimerForForBreakOrAero);
            }
        }
    }

    private void initViews() {
        // The lay of timer for break or aerobic is gone when step is shown
        show_step_LAY_timer_for_break.setVisibility(View.GONE);
        show_step_PGB_countDown_for_break.setMax(secondsForRestOrAerobic);

        // Timer for exercise
        show_step_PGB_countDown.setMax(secondsForExercise);
        show_step_LBL_stepDes.setText(steps[0]);

        int stepImg = getResources().getIdentifier(planSign.toLowerCase() + "_exercise_1", TYPE, FOLDER);
        setStepImage(stepImg, show_step_IMG_stepImg);
        if (planSign.equals(C)) {
            show_step_BTN_step6.setVisibility(View.GONE);
        }
    }

    private void initStepsArray() {
        if (planSign.equals(A)) {
            steps = Arrays.copyOf(PLAN_A, PLAN_A.length);
            secondsForExercise = 3;
            secondsForRestOrAerobic = 3;
        } else if (planSign.equals(B)) {
            steps = Arrays.copyOf(PLAN_B, PLAN_B.length);
            secondsForExercise = 3;
            secondsForRestOrAerobic = 3;
        } else {
            steps = Arrays.copyOf(PLAN_C, PLAN_C.length);
            secondsForExercise = 2;// 20sec
            secondsForRestOrAerobic = 2; // 10 sec
        }
    }

    private void initButtons() {
        show_step_BTN_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimerForExercise(secondsForExercise);
            }
        });

        show_step_BTN_step1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStep(show_step_BTN_step1, show_step_BTN_go, planSign.toLowerCase(), show_step_IMG_stepImg, steps[0], 1);
            }
        });

        show_step_BTN_step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_step_BTN_step1.setEnabled(false);
                showStep(show_step_BTN_step2, show_step_BTN_go, planSign.toLowerCase(), show_step_IMG_stepImg, steps[1], 2);
            }
        });

        show_step_BTN_step3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStep(show_step_BTN_step3, show_step_BTN_go, planSign.toLowerCase(), show_step_IMG_stepImg, steps[2], 3);
            }
        });

        show_step_BTN_step4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStep(show_step_BTN_step4, show_step_BTN_go, planSign.toLowerCase(), show_step_IMG_stepImg, steps[3], 4);
            }
        });

        show_step_BTN_step5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStep(show_step_BTN_step5, show_step_BTN_go, planSign.toLowerCase(), show_step_IMG_stepImg, steps[4], 5);
            }
        });

        show_step_BTN_step6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStep(show_step_BTN_step6, show_step_BTN_go, planSign.toLowerCase(), show_step_IMG_stepImg, steps[5], 6);
            }
        });

    }

    private void handelRoundsOfExercises() {
        switch (planSign) {
            case A:
                rounds++;
                Log.d("rounds", "" + rounds);
                if (rounds < 4)
                    stepCounter = stepCounter - 1;
                breakOrAerobicTime(secondsForRestOrAerobic);
                break;
            case B:
                if (stepCounter % 2 == 0) {
                    isVisible = true;
                    setVisibleStepViews();
                    preformClickNextStep(stepCounter);
                } else if (stepCounter % 2 != 0) {
                    Log.d("rounds", "" + rounds);
                    rounds++;
                    if (rounds < 4)
                        stepCounter = stepCounter - 2;
                    breakOrAerobicTime(secondsForRestOrAerobic);
                }
                break;
            case C:
                rounds++;
                if (rounds < 9)
                    stepCounter = stepCounter - 1;
                breakOrAerobicTime(secondsForRestOrAerobic);
                break;
        }
    }

    private void breakOrAerobicTime(int seconds) {
        isVisible = false;
        setVisibleStepViews();
        show_step_LAY_timer_for_break.setVisibility(View.VISIBLE);
        if (planSign.equals(B)) {
            int stepImg = getResources().getIdentifier("aerobic", TYPE, FOLDER);
            setStepImage(stepImg, show_step_IMG_ImgAerobic);
            show_step_LBL_timeFor.setText("Aerobic Time");
        } else
            show_step_IMG_ImgAerobic.setVisibility(View.GONE);
        if ((rounds == 4 && stepCounter != 7 && (planSign.equals(A) || planSign.equals(B)))
                || (rounds == 9 && planSign.equals(C) && stepCounter != 6))
            rounds = 1;
        startTimerForBreakOrAerobic(seconds);
    }


    private void startTimerForBreakOrAerobic(int secondNotForExercise) {
        countDownTimerForBreak = new CountDownTimer(secondNotForExercise * 1000, 500) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                counterIsRunning2 = true;
                long seconds = leftTimeInMilliseconds / 1000;
                show_step_PGB_countDown_for_break.setProgress((int) seconds);
                show_step_LBL_time_for_break.setText(String.format("%02d", seconds));
                Log.d("startTimerForBreak", "onTick seconds (int) " + (int) seconds);
                timeRemainingForTimerForForBreakOrAero = (int) seconds;
            }
            @Override
            public void onFinish() {
                playSound(R.raw.timer_over);
                counterIsRunning2 = false;
                handelFinishedRound();
            }
        }.start();
    }

    private void handelFinishedRound() {
        if (rounds == 9 && planSign.equals(C) || rounds == 4 && (planSign.equals(A) || planSign.equals(B))) {
            moveToFinishedScreen();
        } else {
            isVisible = true;
            show_step_PGB_countDown_for_break.setProgress(secondsForRestOrAerobic);
            show_step_LAY_timer_for_break.setVisibility(View.GONE);
            setVisibleStepViews();
            preformClickNextStep(stepCounter);
        }
    }

    private void startTimerForExercise(int secondsForExercise) {
        show_step_BTN_go.setEnabled(false);
        stepCounter++;
        Log.d("stepCounter", "startTimerForExercise" + stepCounter);
        countDownTimerForExercise(secondsForExercise * 1000);
    }

    private void countDownTimerForExercise(long TimeInMilliseconds) {
        countDownTimerForExercise = new CountDownTimer(TimeInMilliseconds, 500) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                counterIsRunning1 = true;
                long secondsLeft = leftTimeInMilliseconds / 1000;
                show_step_PGB_countDown.setProgress((int) secondsLeft);
                show_step_BTN_go.setText(String.format("%02d", secondsLeft));
                Log.d("onTick", "countDownTimerForExercise" + secondsLeft);
                timeRemainingForTimerForExercise = leftTimeInMilliseconds;
            }
            @Override
            public void onFinish() {
                playSound(R.raw.timer_over);
                counterIsRunning1 = false;
                show_step_BTN_go.setText("GO!");
                show_step_PGB_countDown.setProgress(secondsForExercise);
                handelRoundsOfExercises();
            }
        }.start();
    }

    private void moveToFinishedScreen() {
        Intent intent = new Intent(this, Finished_Plan.class);
        startActivity(intent);
        finish();
    }

    private void setVisibleStepViews() {
        // Show Step Buttons
        if (isVisible) {
            show_step_LAY_step.setVisibility(View.VISIBLE);
            show_step_LAY_firstSteps.setVisibility(View.VISIBLE);
            show_step_LAY_secondSteps.setVisibility(View.VISIBLE);
        } else {
            // Not Show Step Buttons
            show_step_LAY_step.setVisibility(View.GONE);
            show_step_LAY_firstSteps.setVisibility(View.GONE);
            show_step_LAY_secondSteps.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        onPause();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to finish the plan?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                onResume();
            }
        });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void preformClickNextStep(int numberOfSteps) {
        switch (numberOfSteps) {
            case 1:
                show_step_BTN_step1.performClick();
                break;
            case 2:
                show_step_BTN_step2.performClick();
                break;
            case 3:
                show_step_BTN_step3.performClick();
                break;
            case 4:
                show_step_BTN_step4.performClick();
                break;
            case 5:
                show_step_BTN_step5.performClick();
                break;
            case 6:
                show_step_BTN_step6.performClick();
                break;
        }
    }

    private void showStep(MaterialButton step, MaterialButton start, String plan, ImageView showImg, String stepDes, int stepN) {
        int stepImg = getResources().getIdentifier(plan + "_exercise_" + stepN, TYPE, FOLDER);
        setStepImage(stepImg, showImg);
        step.setBackgroundColor(getResources().getColor(R.color.blue));
        start.setEnabled(true);
        Log.d("enabale", "Start");
        show_step_LBL_stepDes.setText(stepDes);
    }

    public void setStepImage(int id, ImageView imageView) {
        if (!this.isFinishing()) {
            Glide.with(this).load(id).into(imageView);
        }
        Glide.with(getApplicationContext()).load(id).into(imageView);
    }

    private void playSound(int rawSound) {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
            } catch (Exception ex) { }
        }

        mediaPlayer = MediaPlayer.create(this, rawSound);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
        mediaPlayer.start();
    }

    private void getPlanSign() {
        Bundle bundle = getIntent().getExtras();
        planSign = bundle.getString("planSign");
    }

    private void findViews() {
        show_step_BTN_step1 = findViewById(R.id.show_step_BTN_step1);
        show_step_BTN_step2 = findViewById(R.id.show_step_BTN_step2);
        show_step_BTN_step3 = findViewById(R.id.show_step_BTN_step3);
        show_step_BTN_step4 = findViewById(R.id.show_step_BTN_step4);
        show_step_BTN_step5 = findViewById(R.id.show_step_BTN_step5);
        show_step_BTN_step6 = findViewById(R.id.show_step_BTN_step6);
        show_step_IMG_stepImg = findViewById(R.id.show_step_IMG_stepImg);
        show_step_BTN_go = findViewById(R.id.show_step_BTN_go);
        show_step_PGB_countDown = findViewById(R.id.show_step_PGB_countDown);
        show_step_LBL_stepDes = findViewById(R.id.show_step_LBL_stepDes);
        show_step_LAY_firstSteps = findViewById(R.id.show_step_LAY_firstSteps);
        show_step_LAY_secondSteps = findViewById(R.id.show_step_LAY_secondSteps);
        show_step_LAY_step = findViewById(R.id.show_step_LAY_step);
        show_step_LAY_timer_for_break = findViewById(R.id.show_step_LAY_timer_for_break);
        show_step_PGB_countDown_for_break = findViewById(R.id.show_step_PGB_countDown_for_break);
        show_step_LBL_time_for_break = findViewById(R.id.show_step_LBL_time_for_break);
        show_step_LBL_timeFor = findViewById(R.id.show_step_LBL_timeFor);
        show_step_IMG_ImgAerobic = findViewById(R.id.show_step_IMG_ImgAerobic);
    }
}