package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class GoalsActivity extends AppCompatActivity {


    private int currentStep;
    private RecyclerView goalsRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

       Intent intent = getIntent();
       Bundle bundle = intent.getExtras();
        currentStep = bundle.getInt("currentSteps");


        ArrayList<Challenges> challengesList = new ArrayList<>();

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
    }

    public int getCurrentStep() {
        return currentStep;
    }
}
