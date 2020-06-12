package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kylodroid on 04-06-2020.
 */
public class OnboardingActivity extends AppCompatActivity implements ViewPageChange {

    ViewPager viewPager;
    LinearLayout nextButton;
    int position =0;
    ProgressBar progressBar;
    TextView progressFraction, nextText;
    boolean checkViewPager = false;
    HashMap<Integer, Uri> uris = null;
    HashMap<String, Boolean> preferences = null;
    int age, gender, distance;
    String ageGroup;
    View loadingView;
    AlertDialog.Builder builder;
    FirebaseFirestore database;
    FirebaseAuth mAuth;
    DocumentReference mRef;
    FirebaseUser currentUser;
    StorageReference storageReference;
    HashMap<String, String> urii = new HashMap<>();
    boolean databaseWrite = false, storageWrite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new SliderAdapter(getSupportFragmentManager(), OnboardingActivity.this));
        viewPager.setCurrentItem(position);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressFraction = findViewById(R.id.progress_fraction);
        progressFraction.setText((position+1) + "/3");
        nextButton = (LinearLayout) findViewById(R.id.next_button);
        nextText = findViewById(R.id.next_text);
        loadingView = findViewById(R.id.onboard_loading);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        mRef = database.collection("users").document(currentUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference();

        builder = new AlertDialog.Builder(this);

        if(android.os.Build.VERSION.SDK_INT>=24)
            progressBar.setProgress(33*(position+1), true);
        else
            progressBar.setProgress(33*(position+1));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 2){
                    if(age!=0 && distance!=0 && gender!=0 && ageGroup!=null){
                        startOnboarding();
                    }
                    else {
                        Toast.makeText(OnboardingActivity.this, "Please complete the above steps to continue"
                                , Toast.LENGTH_SHORT).show();
                        viewPager.setCurrentItem(position);
                    }
                }
                else if(checkViewPager){
                    viewPager.setCurrentItem(++position);
                    progressFraction.setText((position+1) + "/3");
                    if(android.os.Build.VERSION.SDK_INT>=24)
                        progressBar.setProgress(33*(position+1), true);
                    else
                        progressBar.setProgress(33*(position+1));
                    if(progressBar.getProgress() == 99)
                        progressBar.setProgress(100);
                    if(position==2){
                        progressBar.setProgress(100);
                        nextText.setText("Get Started");
                    }
                }
                else {
                    Toast.makeText(OnboardingActivity.this, "Please complete the above steps to continue"
                    , Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(position);
                }
                checkViewPager = false;
            }
        });
    }

    static int x = 0, y = 0;

    private void startOnboarding(){
        loadingView.setVisibility(View.VISIBLE);
        HashMap<String, Object> db = new HashMap<>();
        db.put("preferences", preferences);
        db.put("age", age);
        if(gender == 1)
            db.put("gender", "male");
        else db.put("gender", "female");
        db.put("ageGroup", ageGroup);
        db.put("distance", distance);
        db.put("onboarding", "true");
        for(Map.Entry<Integer, Uri> u : uris.entrySet()){
            if(u.getValue()!=null)
                y++;
        }
        for(Map.Entry<Integer, Uri> u : uris.entrySet()){
            if(u.getValue()!=null){
                final StorageReference filePath = storageReference.child(u.getValue().getLastPathSegment());
                (filePath).putFile(u.getValue()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //                                Toast.makeText(OnboardingActivity.this, "Uploaded picture " + x, Toast.LENGTH_SHORT).show();
                                urii.put(x++ + "", uri.toString());
                                Log.v("AAA", "Uploaded picture " + x);
                                mRef.update("pictures", urii)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.v("AAA", "Write successful");
                                                storageWrite = true;
                                                if(x == y)
                                                    doneOnboarding();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("AAA", e.toString());
                                storageWrite = false;
                            }
                        });
                    }
                });
            }

        }
        db.put("pictures", urii);
        x=0;
        mRef.update(db)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseWrite = true;
                        doneOnboarding();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OnboardingActivity.this,  e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void doneOnboarding(){
        if(storageWrite && databaseWrite){
            loadingView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Onboarding Successful. Welcome aboard!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(OnboardingActivity.this, DecisionActivity.class));
            finish();
        }
    }

    @Override
    public void check(boolean done) {
        checkViewPager = done;
    }

    @Override
    public void addPictureUri(HashMap<Integer, Uri> uris) {
        this.uris = new HashMap<>(uris);
    }

    @Override
    public void addPreferences(HashMap<String, Boolean> preferences) {
        this.preferences = preferences;
    }

    @Override
    public void addAge(int age) {
        this.age = age;
    }

    @Override
    public void addGender(int gender) {
        this.gender = gender;
    }

    @Override
    public void addAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    @Override
    public void addDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void onBackPressed() {
        builder.setMessage("Unsaved changes might get lost.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Do you want to exit?");
        alertDialog.show();

    }
}
