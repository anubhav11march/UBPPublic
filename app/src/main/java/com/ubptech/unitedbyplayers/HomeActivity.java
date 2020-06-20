package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseFirestore database;
    String phoneNumber, method, name;
    DocumentReference mRef;
    FirebaseUser currentUser;
    NavigationView drawer;
    DrawerLayout drawerLayout;
    LinearLayout drawerButton, nameSpinner, settingsButton;
    ImageView profile1, profile2, profile3;
    ArrayList<Profile> profiles = new ArrayList<>();
    ArrayList<String> profileNames = new ArrayList<>();
    Spinner teamsSpinner;
    ArrayAdapter teamsSpinnerAdapter;
    TextView profileName;
    ImageView gear, home, favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        Utils.setStatusBarColor(window, this);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
            finish();
            return;
        }

        initializeUIElements();

        currentUser = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        mRef = database.collection("users").document(currentUser.getUid());
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.getString("phone") != null) {
                phoneNumber = bundle.getString("phone");
                name = bundle.getString("name");
            }
            if(bundle.getString("method") != null)
                method = bundle.getString("method");
        }

        mRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        if(document.get("onboarding").equals("false")){
                            Intent intent = new Intent(HomeActivity.this, OnboardingActivity.class);
                            startActivity(intent);
                        }
                        else {
//                            Intent intent = new Intent(HomeActivity.this, DecisionActivity.class);
//                            startActivity(intent);
                            updateHomeUI(document);
                        }

                    }
                    else {
                        addToDatabase();
                        Intent intent = new Intent(HomeActivity.this, OnboardingActivity.class);
                        startActivity(intent);
                    }
                }
                else
                    Toast.makeText(HomeActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
            }
        });


//        Toast.makeText(this, currentUser.getUid(), Toast.LENGTH_SHORT).show();

    }

    private void initializeUIElements(){
        drawer = findViewById(R.id.drawer_layout);
        drawerButton = findViewById(R.id.drawer_button);
        drawerLayout = findViewById(R.id.drawer);
        nameSpinner = drawer.getHeaderView(0).findViewById(R.id.name_spinner);
        profile1 = drawer.getHeaderView(0).findViewById(R.id.profile_1);
        profile2 = drawer.getHeaderView(0).findViewById(R.id.profile_2);
        profile3 = drawer.getHeaderView(0).findViewById(R.id.profile_3);
        teamsSpinner = drawer.getHeaderView(0).findViewById(R.id.team_spinner);
        profileName = drawer.getHeaderView(0).findViewById(R.id.profile_name);
        settingsButton = findViewById(R.id.settings_button);
        gear = findViewById(R.id.gear);
        home = findViewById(R.id.home);
        favs = findViewById(R.id.favs);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AppSettingsFragment(HomeActivity.this);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_layout, fragment);
                ft.commit();

                gear.setImageDrawable(getResources().getDrawable(R.drawable.gear));
                home.setImageDrawable(getResources().getDrawable(R.drawable.home_not));
                favs.setImageDrawable(getResources().getDrawable(R.drawable.favs_not));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AppSettingsFragment(HomeActivity.this);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_layout, fragment);
                ft.commit();

                gear.setImageDrawable(getResources().getDrawable(R.drawable.gear));
                home.setImageDrawable(getResources().getDrawable(R.drawable.home_not));
                favs.setImageDrawable(getResources().getDrawable(R.drawable.favs_not));
            }
        });

        teamsSpinnerAdapter = new ArrayAdapter(this, R.layout.item_dropdown_simple, R.id.team_name,
                profileNames);
//        teamsSpinnerAdapter.setDropDownViewResource(R.layout.item_dropdown_simple);
        teamsSpinner.setAdapter(teamsSpinnerAdapter);

        nameSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamsSpinner.performClick();
            }
        });

        teamsSpinner.setOnItemSelectedListener(this);

        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);

            }
        });
    }

    private void addToDatabase(){
        Map<String, String> user = new HashMap<>();
        user.put("uid", currentUser.getUid());
        user.put("method", method);
        user.put("onboarding", "false");
        if(name!=null){
            user.put("name", name);
            user.put("phone", phoneNumber);
            user.put("email", currentUser.getEmail());
        }
        else {
            user.put("name", currentUser.getDisplayName());
            user.put("email", currentUser.getEmail());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US),
        sdf1 = new SimpleDateFormat("dd/MM/yy, HH:mm", Locale.US);
        Date date = new Date();
        user.put("dateOfCreation", sdf.format(date));
        user.put("lastActive", sdf1.format(date));
        database.collection("users").document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("AAA", "User created");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    static int count = 0;
    private void updateHomeUI(final DocumentSnapshot document){
        profiles.add(new Profile(currentUser.getDisplayName(), "", currentUser.getUid()));
        profileNames.add(currentUser.getDisplayName());
        teamsSpinner.setAdapter(teamsSpinnerAdapter);
        HashMap<String, String> userPictures = (HashMap<String, String>) document.get("pictures");
        Glide.with(this)
                .load(userPictures.get("0"))
                .circleCrop()
                .into(profile1);
        profile1.setBackground(getResources().getDrawable(R.drawable.round_image_100));
        count = 0;
        mRef.collection("teams")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documents: task.getResult()){
                                addTeamToList(documents.getData());
                                if(count<2)
                                    setTeamPicture(count++, documents.getData());
                            }
                            count=0;
                        }
                    }
                });
    }

    private void addTeamToList(final Map<String, Object> doc){
        database.collection("codes").document(doc.get("teamCode").toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            profiles.add(new Profile(documentSnapshot.get("teamName").toString(),
                                    documentSnapshot.get("sport").toString(),
                                    documentSnapshot.get("fullCode").toString()));
                            profileNames.add(documentSnapshot.get("teamName").toString());
                            teamsSpinner.setAdapter(teamsSpinnerAdapter);
                        }
                    }
                });
    }

    private void setTeamPicture(final int imageNo, final Map<String, Object> document){
        String teamCode = document.get("teamCode").toString();
        database.collection("codes").document(teamCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            setPicture(imageNo, documentSnapshot.get("fullCode").toString(),
                                    documentSnapshot.get("sport").toString());
                        }
                    }
                });
    }

    private void setPicture(final int imageNo, String fullCode, String sport){
        database.collection("teams").document(sport)
                .collection("teams").document(fullCode)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            HashMap<String, String> imageUrl = (HashMap<String, String>) documentSnapshot.get("pictures");
                            if(imageNo == 0)
                                Glide.with(HomeActivity.this)
                                        .load(imageUrl.get("0"))
                                        .circleCrop().into(profile2);
                            else if(imageNo == 1)
                                Glide.with(HomeActivity.this)
                                        .load(imageUrl.get("0"))
                                        .circleCrop().into(profile3);
                        }

                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Log.v("AAA", "dafa");
//        Toast.makeText(HomeActivity.this, profiles.get(i).getTeamName(), Toast.LENGTH_LONG).show();
        profileName.setText(profiles.get(i).getTeamName());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
