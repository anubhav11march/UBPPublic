package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilePermission;
import java.util.HashMap;

public class CreateTeamActivity extends AppCompatActivity implements CreateTeamChange {

    LinearLayout nextButton;
    static int pos = 0;
    private View p1, p2, p3;
    ImageView teamPhoto;
    private static final int GALLERY_REQUEST = 2;
    Uri uri = null;
    TextView addTeamPhoto;
    String teamName, teamSport, ageGroup, bio, teamCode, teamId;
    boolean otherPlayersAllow, teamPhotoAdded;
    int maxBet, distance;
    FirebaseFirestore database;
    DocumentReference userRef, teamRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    StorageReference storageReference;
    View loadingView;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        getSupportActionBar().hide();
        nextButton = (LinearLayout) findViewById(R.id.next_button);
        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        teamPhoto = findViewById(R.id.team_photo);
        addTeamPhoto = findViewById(R.id.add_team_photo);
        loadingView = findViewById(R.id.create_team_loading);
        viewPager = findViewById(R.id.options);
        viewPager.setAdapter(new CreateTeamSliderAdapter(getSupportFragmentManager(), CreateTeamActivity.this));
        viewPager.setCurrentItem(0);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos==0)
                    if(teamName!=null && teamSport!=null)
                        displayFragment(++pos);
                    else {
                        Toast.makeText(CreateTeamActivity.this, "Please fill the above information to continue",
                                Toast.LENGTH_SHORT).show();
                        displayFragment(0);
                    }
                else if(pos==1)
                    if(ageGroup!=null && distance >0)
                        displayFragment(++pos);
                    else {
                        Toast.makeText(CreateTeamActivity.this, "Please fill the above information to continue",
                                Toast.LENGTH_SHORT).show();
                        displayFragment(1);
                    }
                else if(pos==2)
                    if(bio!=null) {
                        if(teamPhotoAdded)
                            createTeam();
                        else Toast.makeText(CreateTeamActivity.this, "Please add a team picture to continue",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(CreateTeamActivity.this, "Please fill the above information to continue",
                                Toast.LENGTH_SHORT).show();
                        displayFragment(2);
                    }

            }
        });

        teamPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTeamPhoto();
            }
        });

        displayFragment(pos);
    }

    void createTeam(){
        loadingView.setVisibility(View.VISIBLE);
        HashMap<String, Object> team = new HashMap<>();
        team.put("name", teamName);
        team.put("sport", teamSport);
        team.put("allowDiscovery", otherPlayersAllow);
        team.put("ageGroup", ageGroup);
        team.put("distance", distance);
        team.put("maxBet", maxBet);
        team.put("bio", bio);
        team.put("pictures", null);
        team.put("captain", currentUser.getUid());
        team.put("teamCode", teamCode);
        teamRef = database.collection("teams").document(teamSport);
        teamRef.collection("teams")
                .add(team)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        teamCode = documentReference.getId().substring(0, 6);
                        teamId = documentReference.getId();
                        addTeamCode();
                        addToUserDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "An error occured " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addTeamCode(){
        HashMap<String, Object> code = new HashMap<>();
        code.put("fullCode", teamId);
        code.put("teamCode", teamCode);
        code.put("sport", teamSport);
        database.collection("codes").document(teamCode)
                .set(code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
        teamRef.collection("teams")
                .document(teamId)
                .update(code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
        HashMap<String, String> teamMembers = new HashMap<>();
        teamMembers.put("playerUid", currentUser.getUid());
        teamRef.collection("teams").document(teamId)
                .collection("teamMembers")
                .add(teamMembers)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });
    }

    private void uploadTeamPicture(){
        final StorageReference filePath = storageReference.child(uri.getLastPathSegment());
        (filePath).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        addUrl(uri.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("AAA", e.toString());
                    }
                });
            }
        });
    }

    private void addUrl(String url){
        HashMap<String, String> urls = new HashMap<>();
        urls.put("0", url);
        teamRef.collection("teams")
                .document(teamId)
                .update("pictures", urls)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(CreateTeamActivity.this, CreateTeamSuccessActivity.class);
                        intent.putExtra("teamCode", teamCode);
                        startActivity(intent);
                        loadingView.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("AAA", e.toString());
                    }
                });
    }

    private void addToUserDatabase(){
        HashMap<String, String> team = new HashMap<>();
        team.put("teamCode", teamCode);
        userRef = database.collection("users").document(currentUser.getUid());
        userRef.collection("teams")
                .document(teamCode)
                .set(team)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        uploadTeamPicture();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("AAA", "user database: " + e.toString());
                    }
                });
    }

    void displayFragment(int position){
        switch (position){
            case 0: viewPager.setCurrentItem(0);
                    p1.setBackgroundResource(R.drawable.current_page);
                    p2.setBackgroundResource(R.drawable.not_current_page);
                p3.setBackgroundResource(R.drawable.not_current_page);
                        break;
            case 1: viewPager.setCurrentItem(1);
                p1.setBackgroundResource(R.drawable.not_current_page);
                p2.setBackgroundResource(R.drawable.current_page);
                p3.setBackgroundResource(R.drawable.not_current_page);
                break;
            case 2: viewPager.setCurrentItem(2);
                p1.setBackgroundResource(R.drawable.not_current_page);
                p2.setBackgroundResource(R.drawable.not_current_page);
                p3.setBackgroundResource(R.drawable.current_page);
                break;
        }
    }

    void setCurrentPage(int position){
        switch (position){
            case 0:
                p1.setBackgroundResource(R.drawable.current_page);
                p2.setBackgroundResource(R.drawable.not_current_page);
                p3.setBackgroundResource(R.drawable.not_current_page);
                break;
            case 1:
                p1.setBackgroundResource(R.drawable.not_current_page);
                p2.setBackgroundResource(R.drawable.current_page);
                p3.setBackgroundResource(R.drawable.not_current_page);
                break;
            case 2:
                p1.setBackgroundResource(R.drawable.not_current_page);
                p2.setBackgroundResource(R.drawable.not_current_page);
                p3.setBackgroundResource(R.drawable.current_page);
                break;
        }
    }

    private void addTeamPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            Glide.with(this).load(uri)
                    .apply(new RequestOptions()
                            .override(200, 200)).centerCrop().into(teamPhoto);
//            Picasso.get().load(uri).resize(200, 200).centerCrop().into(teamPhoto);
            addTeamPhoto.setText("Change Team Photo");
            teamPhotoAdded = true;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void setTeamName(String name) {
        teamName = name;
    }

    @Override
    public void setSport(String sport) {
        teamSport = sport;
    }

    @Override
    public void setOtherPlayers(boolean choice) {
        otherPlayersAllow = choice;
    }

    @Override
    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    @Override
    public void setMaxBet(int bet) {
        maxBet = bet;
    }

    @Override
    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void setBio(String bio) {
        this.bio = bio;
    }
}
