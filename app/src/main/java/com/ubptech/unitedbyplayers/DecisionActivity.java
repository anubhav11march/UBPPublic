package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DecisionActivity extends AppCompatActivity {

    LinearLayout createTeam, joinTeam;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        getSupportActionBar().hide();
        createTeam = (LinearLayout) findViewById(R.id.create_team_button);
        joinTeam = (LinearLayout) findViewById(R.id.join_team_button);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRef = firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid());

        createTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecisionActivity.this, CreateTeamActivity.class));
                finish();
            }
        });

        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DecisionActivity.this, JoinTeamActivity.class));
                finish();
            }
        });
    }
}