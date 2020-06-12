package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class JoinTeamActivity extends AppCompatActivity {

    FirebaseFirestore database;
    DocumentReference mRef;
    TextView name;
    ImageView photo;
    EditText teamCode;
    LinearLayout joinTeamButton;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);
        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        photo = findViewById(R.id.photo);
        teamCode = findViewById(R.id.team_code);
        joinTeamButton = findViewById(R.id.join_team_button);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();
        mRef = database.collection("users").document(currentUser.getUid());
        mRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            name.setText(document.get("name").toString());
                            HashMap<String, String> pic = (HashMap<String, String>)document.get("pictures");
                            Glide.with(JoinTeamActivity.this).load(pic.get("0"))
                                    .apply(new RequestOptions()
                                            .override(200, 200)).centerCrop().into(photo);
//                            Picasso.get().load(pic.get("0")).into(photo);
                        }
                    }
                });

        joinTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String code = teamCode.getText().toString().trim();
                if(code.length()!=6){
                    Toast.makeText(JoinTeamActivity.this, "Please enter a valid team code", Toast.LENGTH_LONG).show();
                    return;
                }
                database.collection("codes").document(code)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(!documentSnapshot.exists()){
                                        Toast.makeText(JoinTeamActivity.this, "No team exists with this code", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else {
                                        checkIfAlreadyPartOfTeam(code, documentSnapshot.get("fullCode").toString(), documentSnapshot.get("sport").toString());
                                    }
                                }
                            }
                        });
            }
        });
    }

    private void addNibbToTeam(final String code, String sport){
        HashMap<String, String> player = new HashMap<>();
        player.put("playerUid", currentUser.getUid());
        database.collection("teams").document(sport)
                .collection("teams").document(code)
                .collection("teamMembers")
                .add(player)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        addTeamToNibb(code.substring(0, 6));
                        Log.v("AAA", "Added nibb to team");
                    }
                });
    }

    private void addTeamToNibb(final String code){
        HashMap<String, String> player = new HashMap<>();
        player.put("teamCode", code);
        database.collection("users").document(currentUser.getUid())
                .collection("teams")
                .document(code)
                .set(player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(JoinTeamActivity.this, "Great, joined team", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(JoinTeamActivity.this, WelcomeTeamActivity.class);
                        intent.putExtra("teamCode", code);
                        startActivity(intent);
                        finish();
                    }
                });
    }
    static boolean alreadypartOfTeam = false;
    private void checkIfAlreadyPartOfTeam(String code, final String fullCode, final String sport){

        mRef.collection("teams").document(code.substring(0, 6))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                Toast.makeText(JoinTeamActivity.this, "You are already a part of this team", Toast.LENGTH_LONG).show();
                            }
                            else {
                                addNibbToTeam(fullCode, sport);
                            }
                        }
                    }
                });
    }
}
