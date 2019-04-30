package com.example.offlinemaps;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private TextView counterView;
    private int steps = 0;
    private Button goalsBtn;
    private SensorManager sensorManager;
    private Sensor stepSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepcounter);

        counterView = findViewById(R.id.counterText);
        goalsBtn = findViewById(R.id.goalsBtn);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        counterView.setText(String.valueOf(steps));

        sensorManager.registerListener(this,stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

       goalsBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), GoalsActivity.class);
               intent.putExtra("currentSteps",steps);
               startActivity(intent);
           }
       });
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

       if(event.values[0] == 1.0f) {
           steps++;
       }
        counterView.setText(Integer.toString(steps));


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
