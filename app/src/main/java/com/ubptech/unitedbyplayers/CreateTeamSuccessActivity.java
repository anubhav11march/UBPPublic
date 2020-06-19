package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CreateTeamSuccessActivity extends AppCompatActivity {

    FirebaseFirestore database;
    DocumentReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String teamCode, teamId, teamSport;
    TextView teamName, publicPrivate, teamCaptain, teamCodee, teamSportt;
    LinearLayout doneButton;
    ImageView whatsapp, insta, copy, teamPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team_success);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        Utils.setStatusBarColor(window, this);
        teamName = findViewById(R.id.team_name);
        teamCodee = findViewById(R.id.team_code);
        teamSportt = findViewById(R.id.team_sport);
        publicPrivate = findViewById(R.id.private_public);
        teamCaptain = findViewById(R.id.team_captain);
        doneButton = findViewById(R.id.done_button);
        teamPhoto = findViewById(R.id.team_photo);
        whatsapp = findViewById(R.id.whatsapp);
        insta = findViewById(R.id.insta);
        copy = findViewById(R.id.copy);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateTeamSuccessActivity.this, HomeActivity.class));
                finish();
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format(
                                        "https://api.whatsapp.com/send?text=%s",
                                         "Join my team " + teamCode
                                )
                        )
                ));
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("message UBP", "Join my team " + teamCode);
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(CreateTeamSuccessActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "https://www.instagram.com/unitedbyplayers/";
                try {
                    if (getPackageManager().getPackageInfo("com.instagram.android", 0) != null) {
                        if (url.endsWith("/")) {
                            url = url.substring(0, url.length() - 1);
                        }
                        final String username = url.substring(url.lastIndexOf("/") + 1);
                        intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                        intent.setPackage("com.instagram.android");
                        startActivity(intent);
                    }
                } catch (PackageManager.NameNotFoundException ignored) {
                }
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        teamCode = bundle.getString("teamCode");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        mRef = database.collection("codes").document(teamCode);
        mRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            teamId = document.get("fullCode").toString();
                            teamSport = document.get("sport").toString();
                            fetchTeam();
                        }
                    }
                });
    }

    private void fetchTeam(){
        database.collection("teams").document(teamSport)
                .collection("teams").document(teamId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            setUI(document);
                        }
                    }
                });
    }

    private void setUI(DocumentSnapshot document){
        teamName.setText(document.get("name").toString());
        teamCaptain.setText(currentUser.getDisplayName());
        teamCodee.setText(document.get("teamCode").toString());
        publicPrivate.setText(document.get("allowDiscovery").toString().equals("false")
                ? "Private Team":"Currently looking for players");
        teamSportt.setText((char)(teamSport.charAt(0)-32) + teamSport.substring(1) + " team");
        HashMap<String, String> url = (HashMap<String, String>) document.get("pictures");
        Glide.with(CreateTeamSuccessActivity.this).load(url.get("0"))
                .apply(new RequestOptions()
                        .override(220, 220)).centerCrop().into(teamPhoto);
//        Picasso.get().load(url.get("0")).into(teamPhoto);
    }
}
