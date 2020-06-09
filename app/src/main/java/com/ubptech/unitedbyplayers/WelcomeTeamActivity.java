package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class WelcomeTeamActivity extends AppCompatActivity {

    FirebaseFirestore database;
    DocumentReference mRef;
    TextView teamName, teamSport, teamCaptain, teamCode, privatePublic, welcomeToTeam;
    ImageView teamPhoto;
    String teamId, teamCodee, teamSportt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_team);
        getSupportActionBar().hide();

        Bundle bundle = getIntent().getExtras();
        teamCodee = bundle.getString("teamCode");

        initiate();

    }

    private void initiate(){
        teamName = findViewById(R.id.team_name);
        teamSport = findViewById(R.id.team_sport);
        teamCode = findViewById(R.id.team_code);
        teamCaptain = findViewById(R.id.team_captain);
        privatePublic = findViewById(R.id.private_public);
        teamPhoto = findViewById(R.id.team_photo);
        welcomeToTeam = findViewById(R.id.welcome_to_team);

        database = FirebaseFirestore.getInstance();
        database.collection("codes").document(teamCodee)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            teamId = documentSnapshot.get("fullCode").toString();
                            teamSportt = documentSnapshot.get("sport").toString();
                            updateUI();
                        }
                    }
                });
    }

    private void updateUI(){
        database.collection("teams").document(teamSportt)
                .collection("teams").document(teamId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            teamName.setText("Team Name: " + documentSnapshot.get("name"));
                            welcomeToTeam.setText("Welcome to the " + documentSnapshot.get("name") + " Team Account");
                            teamSport.setText("Sport: " + documentSnapshot.get("sport") + " team");
                            teamCode.setText("Team Code: " + teamCodee);
                            privatePublic.setText(documentSnapshot.get("allowDiscovery").equals("false") ?
                                    "Private team" : "Currently looking for players");
                            HashMap<String, String> url = (HashMap<String, String>) documentSnapshot.get("pictures");
                            Picasso.get().load(url.get("0")).into(teamPhoto);
                            setCaptainName(documentSnapshot);
                        }
                    }
                });
    }

    private void setCaptainName(final DocumentSnapshot documentSnapshot){
        database.collection("users").document(documentSnapshot.get("captain").toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot1 = task.getResult();
                            teamCaptain.setText("Captain: " + documentSnapshot1.get("name"));
                        }
                    }
                });
    }
}
