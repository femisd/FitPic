package com.example.offlinemaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUsername extends AppCompatActivity {

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_username);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get reference to database.
        String currentUser = FirebaseAuth.getInstance().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("mUsername");

        //Initialise fields.
        final EditText username = (EditText) findViewById(R.id.et_update_username);
        Button update  = (Button) findViewById(R.id.bt_update);

        //Update username on button click.
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(UpdateUsername.this, ProfileUI.class);
                startActivity(profile);
                userRef.setValue(username.getText().toString());
                finish();
            }
        });
    }

}
