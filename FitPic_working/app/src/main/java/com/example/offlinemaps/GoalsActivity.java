package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GoalsActivity extends AppCompatActivity {


    private int currentStep;
    private RecyclerView goalsRecyclerView;
    private ChallengesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView timerText;
    private CountDownTimer countDownTimer;
    private long timeLeftinMs = 36000000;


    private DatabaseReference userRef;
    private String mCurrentUser = FirebaseAuth.getInstance().getUid();
    private  User currentUser;

    public int currentPoints;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

       Intent intent = getIntent();
       Bundle bundle = intent.getExtras();
        currentStep = bundle.getInt("currentSteps");

        timerText = findViewById(R.id.timer_textView);



        final ArrayList<Challenges> challengesList = new ArrayList<>();

        challengesList.add(new Challenges(getCurrentStep(),"Walk 20 Steps!", 20, "Steps",20));
        challengesList.add(new Challenges(getCurrentStep(),"Walk 50 Steps!", 50, "Steps",50));
        challengesList.add(new Challenges(getCurrentStep(),"Walk 100 Steps!", 100, "Steps",100));
        challengesList.add(new Challenges(0,"Walk 100 Meters!", 10, "Meters",20));
        challengesList.add(new Challenges(getCurrentStep()/20,"Burn 10 calories!", 10, "Calories",20));

        goalsRecyclerView = findViewById(R.id.goalsRecyclerView);
        goalsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ChallengesAdapter(challengesList);

        goalsRecyclerView.setLayoutManager(layoutManager);
        goalsRecyclerView.setAdapter(adapter);



        startTimer();
        updateTimer();


        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUser);



        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                currentPoints = currentUser.getmPoints();
                Log.d("Point", ""+currentPoints);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public int getCurrentStep() {
        return currentStep;
    }



    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftinMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftinMs = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }

        }.start();

    }
        public void  updateTimer(){

            int seconds = (int) (timeLeftinMs / 1000);
            int minutes = (seconds / 60);
            int hours = (minutes / 60);

            minutes = minutes - hours * 60;

            seconds = seconds - (minutes * 60) - (hours * 60 * 60);

            String timeLeftText;
            timeLeftText = "Next Automatic Refresh is in " + hours;
            timeLeftText += ":";
            if(minutes < 10 ) timeLeftText += "0";
            timeLeftText += minutes;
            timeLeftText += ":";
            if(seconds < 10) timeLeftText += "0";
            timeLeftText += seconds;

            timerText.setText(timeLeftText);

        }

    public int getCurrentPoints() {
        return currentPoints;
    }
}


