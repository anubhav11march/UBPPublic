package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
SportChangeListener, MessageFragmentInstanceListener, TitleChangeListener, ChangeFragmentListener{

    FirebaseAuth mAuth;
    FirebaseFirestore database;
    String phoneNumber, method, name;
    DocumentReference mRef;
    FirebaseUser currentUser;
    NavigationView drawer;
    DrawerLayout drawerLayout;
    LinearLayout drawerButton, nameSpinner, settingsButton, homeButton, favButton;
    ImageView profile1, profile2, profile3, dropdownIcon, messagesIcon;
    ArrayList<Profile> profiles = new ArrayList<>();
    ArrayList<String> profileNames = new ArrayList<>();
    Spinner teamsSpinner;
    ArrayAdapter teamsSpinnerAdapter;
    TextView profileName, logoutButton, pageTitle;
    ImageView gear, home, favs;
    final int REQUEST_LOCATION_PERMISSION = 1;
    String currentProfileCode, currentSport;
    ArrayList<PlayerIdLoc> playerIds = new ArrayList<>();
    ArrayList<TeamIdLoc> teamIds = new ArrayList<>();
    ArrayList<TeamCardDetails> teamCardDetails = new ArrayList<>();
    ArrayList<PlayerCardDetails> playerCardDetails = new ArrayList<>();
    double currLat, currLon, currTeamLat, currTeamLon;
    Fragment fragment;
    RelativeLayout messagesButton;
    FrameLayout loadingView;
    boolean isPlayer;


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
                            isPlayer = true;
                            updateHomeUIForJoiningTeamNew(document);
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
        messagesButton = findViewById(R.id.messages_button);
        drawer = findViewById(R.id.drawer_layout);
        drawerButton = findViewById(R.id.drawer_button);
        drawerLayout = findViewById(R.id.drawer);
        nameSpinner = drawer.getHeaderView(0).findViewById(R.id.name_spinner);
        profile1 = drawer.getHeaderView(0).findViewById(R.id.profile_1);
        profile2 = drawer.getHeaderView(0).findViewById(R.id.profile_2);
        profile3 = drawer.getHeaderView(0).findViewById(R.id.profile_3);
        logoutButton = drawer.getHeaderView(0).findViewById(R.id.logout_button);
        teamsSpinner = drawer.getHeaderView(0).findViewById(R.id.team_spinner);
        profileName = drawer.getHeaderView(0).findViewById(R.id.profile_name);
        settingsButton = findViewById(R.id.settings_button);
        homeButton = findViewById(R.id.home_button);
        favButton = findViewById(R.id.fav_button);
        gear = findViewById(R.id.gear);
        home = findViewById(R.id.home);
        favs = findViewById(R.id.favs);
        dropdownIcon = findViewById(R.id.dropdown_icon);
        pageTitle = findViewById(R.id.page_title);
        loadingView = findViewById(R.id.loading_view);
        messagesIcon = findViewById(R.id.messages_icon);

        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messagesButtonClicked();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    mAuth.signOut();
                    startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new DiscoverFragment(HomeActivity.this, playerCardDetails, mRef, database, mAuth);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_layout, fragment);
                ft.commit();
                if(currentProfileCode.equals(currentUser.getUid())) {
                    pageTitle.setText("Join");
                    dropdownIcon.setVisibility(View.GONE);
                }
                else {
                    pageTitle.setText("Challenge");
                    dropdownIcon.setVisibility(View.VISIBLE);
                }
                gear.setImageDrawable(getResources().getDrawable(R.drawable.gear_not));
                home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                favs.setImageDrawable(getResources().getDrawable(R.drawable.favs_not));
                if (isPlayer) {
                    fetchTeamsForJoining();
                } else {
                    fetchTeamsForChallenging();
                }
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new AppSettingsFragment(HomeActivity.this, mRef);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_layout, fragment);
                ft.commit();

                pageTitle.setText("Settings");
                dropdownIcon.setVisibility(View.GONE);
                gear.setImageDrawable(getResources().getDrawable(R.drawable.gear));
                home.setImageDrawable(getResources().getDrawable(R.drawable.home_not));
                favs.setImageDrawable(getResources().getDrawable(R.drawable.favs_not));
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new AppSettingsFragment(HomeActivity.this, mRef);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_layout, fragment);
                ft.commit();

                gear.setImageDrawable(getResources().getDrawable(R.drawable.gear_not));
                home.setImageDrawable(getResources().getDrawable(R.drawable.home_not));
                favs.setImageDrawable(getResources().getDrawable(R.drawable.favs));
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

        currentSport = "cricket";
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

    private void updateHomeUIForJoiningTeamNew(final DocumentSnapshot document){
        DatabaseReference locRef = FirebaseDatabase.getInstance().getReference("path/to/geofireteams");
        final GeoFire geoFire = new GeoFire(locRef);
        DatabaseReference locRef1 = FirebaseDatabase.getInstance().getReference("path/to/geofire");
        final GeoFire geoFire1 = new GeoFire(locRef1);

        currentProfileCode = currentUser.getUid();

        dropdownIcon.setVisibility(View.GONE);
        pageTitle.setText("Join");
        homeButton.performClick();

        teamIds = new ArrayList<>();

        //location updation
        if(!checkLocationPermission())
            checkLocationPermission();
        Utils.checkIfLocationOn(this, HomeActivity.this);
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest =
                new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        if(location!=null){
                            Log.v("AAA", location.getLatitude() + " " + location.getLongitude());
                            geoFire1.setLocation(currentProfileCode, new GeoLocation(location.getLatitude()
                                    , location.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if(error!=null){
                                        Log.v("AAA", "Some error occurred");
                                        Toast.makeText(HomeActivity.this, "An error occurred in fetching nearby teams, " +
                                                "please restart your app", Toast.LENGTH_LONG).show();
                                    }else {
                                        currLat = location.getLatitude();
                                        currLon = location.getLongitude();
                                        Log.v("AAA", "Location added");
                                        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(),
                                                location.getLongitude()), 2000);

                                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                            @Override
                                            public void onKeyEntered(String key, GeoLocation location) {
//                                                if(!key.equals(currentProfileCode))
                                                teamIds.add(new TeamIdLoc(key, location.latitude, location.longitude));
                                                Log.v("AAA", key);
                                            }

                                            @Override
                                            public void onKeyExited(String key) {
                                                Log.v("AAA", "exited");
                                            }

                                            @Override
                                            public void onKeyMoved(String key, GeoLocation location) {
                                                Log.v("AAA", "moved");
                                            }

                                            @Override
                                            public void onGeoQueryReady() {
                                                Log.v("AAA", "DOne");
                                                fetchTeamsForJoining();
                                            }

                                            @Override
                                            public void onGeoQueryError(DatabaseError error) {
                                                Log.v("AAA", "error");
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        else {
                            Log.v("AAA", "Location null");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        checkLocationPermission();
                        Log.v("AAA", "failed");
                    }
                });

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

    private void fetchTeamsForJoining(){
        //TODO: location ka check ki on hui ya nahi aur permission hai ya nahi
        teamCardDetails = new ArrayList<>();
        for (int i=0; i<teamIds.size(); i++) {
            final int index = i;
            database.collection("teams").document(currentSport)
                    .collection("teams").document(teamIds.get(i).getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot.exists()) {
                                    mRef.collection("teamsResponse").document(documentSnapshot.get("fullCode").toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                       DocumentSnapshot documentSnapshot1 = task.getResult();
                                                       if(!documentSnapshot1.exists())
                                                           continueAddingTeamForJoining(documentSnapshot, index);
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(teamCardDetails.size()==0){
                        if (fragment instanceof DiscoverFragment)
                            ((NoTeamAvailableInGivenRadiusListener) fragment).noTeamUpdate("No team around, try increasing the distance");
                    }
                }
            }, 3000);
        }
    }

    private void continueAddingTeamForJoining(DocumentSnapshot documentSnapshot, int index){
        HashMap<String, String> pics = (HashMap<String, String>) documentSnapshot.get("pictures");
        double distance = Math.sqrt(
                Math.pow(Math.abs(teamIds.get(index).getLat() - currLat) * 110.574, 2) +
                        Math.pow(Math.abs(Math.cos(teamIds.get(index).getLon()) - Math.cos(currLon)) * 111.32, 2));
        String distanceWith1Decimal = (distance + "").substring(0, (distance + "").indexOf(".")) +
                (distance + "").substring((distance + "").indexOf("."), (distance + "").indexOf(".") + 2);
        String friendlyOrNot;
        if(documentSnapshot.get("maxBet").equals(0))
            friendlyOrNot = getString(R.string.bullet) + " Friendly Matches Only";
        else
            friendlyOrNot = getString(R.string.bullet) + " Max Bet " + documentSnapshot.get("maxBet");
        if (documentSnapshot.get("totalMatches") == null) {
            teamCardDetails.add(new TeamCardDetails(
                    friendlyOrNot,
                    "No matches played till now",
                    distanceWith1Decimal + " Kms Away",
                    pics,
                    documentSnapshot.get("name").toString(),
                    documentSnapshot.get("sport").toString(),
                    documentSnapshot.get("fullCode").toString()
            ));
        } else {
            teamCardDetails.add(new TeamCardDetails(
                    friendlyOrNot,
                    documentSnapshot.get("totalMatches").toString() + " Matches " + getString(R.string.bullet) + " "
                            + documentSnapshot.get("wonMatches").toString() + " Won " + getString(R.string.bullet) + " "
                            + documentSnapshot.get("lostMatches").toString() + " Lost",
                    distanceWith1Decimal + " Kms Away",
                    pics,
                    documentSnapshot.get("name").toString(),
                    documentSnapshot.get("sport").toString(),
                    documentSnapshot.get("fullCode").toString()
            ));
        }

        if (fragment instanceof DiscoverFragment)
            ((TeamsListReadyListener) fragment).updateTeamsList(teamCardDetails);
    }

    private void updateHomeUI(final DocumentSnapshot document){
        DatabaseReference locRef = FirebaseDatabase.getInstance().getReference("path/to/geofireteams");
        final GeoFire geoFire = new GeoFire(locRef);

        currentProfileCode = currentUser.getUid();

        dropdownIcon.setVisibility(View.GONE);
        pageTitle.setText("Join");
        homeButton.performClick();

        playerIds = new ArrayList<>();

        //location updation
        if(!checkLocationPermission())
            checkLocationPermission();
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest =
                new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        if(location!=null){
                            Log.v("AAA", location.getLatitude() + " " + location.getLongitude());
                            geoFire.setLocation(currentProfileCode, new GeoLocation(location.getLatitude()
                                    , location.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if(error!=null){
                                        Log.v("AAA", "Some error occurred");
                                    }else {
                                        currLat = location.getLatitude();
                                        currLon = location.getLongitude();
                                        Log.v("AAA", "Location added");
                                        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(),
                                                location.getLongitude()), 2000);

                                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                            @Override
                                            public void onKeyEntered(String key, GeoLocation location) {
//                                                if(!key.equals(currentProfileCode))
                                                    playerIds.add(new PlayerIdLoc(key, location.latitude, location.longitude));
                                                    Log.v("AAA", key);
                                            }

                                            @Override
                                            public void onKeyExited(String key) {
                                                Log.v("AAA", "exited");
                                            }

                                            @Override
                                            public void onKeyMoved(String key, GeoLocation location) {
                                                Log.v("AAA", "moved");
                                            }

                                            @Override
                                            public void onGeoQueryReady() {
                                                Log.v("AAA", "DOne");
                                                fetchPlayers();
                                            }

                                            @Override
                                            public void onGeoQueryError(DatabaseError error) {
                                                Log.v("AAA", "error");
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        else {
                            Log.v("AAA", "Location null");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        checkLocationPermission();
                        Log.v("AAA", "failed");
                    }
                });

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
                            if(documentSnapshot.exists()) {
                                profiles.add(new Profile(documentSnapshot.get("teamName").toString(),
                                        documentSnapshot.get("sport").toString(),
                                        documentSnapshot.get("fullCode").toString()));
                                profileNames.add(documentSnapshot.get("teamName").toString());
                                teamsSpinner.setAdapter(teamsSpinnerAdapter);
                            }
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

    private void fetchPlayers(){
        playerCardDetails = new ArrayList<>();
        for (int i=0; i<playerIds.size(); i++){
            final int index = i;
            database.collection("users").document(playerIds.get(i).getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                HashMap<String, String> pics = (HashMap<String, String>) documentSnapshot.get("pictures");
                                double distance = Math.sqrt(
                                        Math.pow(Math.abs(playerIds.get(index).getLat() - currLat)*110.574, 2) +
                                        Math.pow(Math.abs(Math.cos(playerIds.get(index).getLon()) - Math.cos(currLon))*111.32, 2));
                                String distanceWith1Decimal = (distance+"").substring(0, (distance+"").indexOf(".")) +
                                        (distance+"").substring((distance+"").indexOf("."), (distance+"").indexOf(".")+2);
                                if(documentSnapshot.get("totalMatches") == null){
                                    playerCardDetails.add(new PlayerCardDetails(
                                            documentSnapshot.get("gender").toString(),
                                            "No matches played till now",
                                            distanceWith1Decimal + " Kms Away",
                                            pics,
                                            documentSnapshot.get("name").toString()
                                    ));
                                }
                                else {
                                    playerCardDetails.add(new PlayerCardDetails(
                                            documentSnapshot.get("gender").toString(),
                                            documentSnapshot.get("totalMatches").toString() + " Matches " + getString(R.string.bullet) + " "
                                            + documentSnapshot.get("wonMatches").toString() + " Won " + getString(R.string.bullet) + " "
                                            + documentSnapshot.get("lostMatches").toString() + " Lost",
                                            distanceWith1Decimal + " Kms Away",
                                            pics,
                                            documentSnapshot.get("name").toString()

                                    ));
                                }
                                if (fragment instanceof DiscoverFragment)
                                    ((PlayersListReadyListener) fragment).updatePlayersList(playerCardDetails);
                            }
                        }
                    });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Log.v("AAA", "dafa");
        profileName.setText(profiles.get(i).getTeamName());
        if(currentProfileCode.equals(profiles.get(i).getFullCode()))
            return;
        loadingView.setVisibility(View.VISIBLE);
        drawerLayout.closeDrawer(Gravity.LEFT);
        Toast.makeText(HomeActivity.this, "Switching profile", Toast.LENGTH_LONG).show();
        if(profiles.get(i).getFullCode().equals(currentUser.getUid())) {
            isPlayer = true;
            loadingView.setVisibility(View.GONE);
            mRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                updateHomeUIForJoiningTeamNew(documentSnapshot);
                            }
                        }
                    });
        }
        else {
            isPlayer = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                }
            }, 1500);
            database.collection("teams")
                    .document(profiles.get(i).getSport())
                    .collection("teams")
                    .document(profiles.get(i).getFullCode())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot = task.getResult();
                                changeUIToTeamView(documentSnapshot);
                            }
                        }
                    });
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void changeUIToTeamView(DocumentSnapshot documentSnapshot){
        DatabaseReference teamLocRef = FirebaseDatabase.getInstance().getReference("path/to/geofireteams");
        final GeoFire teamGeoFire = new GeoFire(teamLocRef);
        DatabaseReference playerLocRef = FirebaseDatabase.getInstance().getReference("path/to/geofire");
        final GeoFire playerGeoFire = new GeoFire(playerLocRef);

        currentProfileCode =documentSnapshot.get("fullCode").toString();
        currentSport = documentSnapshot.get("sport").toString();

        pageTitle.setText("Challenge");
        dropdownIcon.setVisibility(View.VISIBLE);
        homeButton.performClick();

        teamIds = new ArrayList<>();

        //location updation
        if(!checkLocationPermission())
            checkLocationPermission();
        Utils.checkIfLocationOn(this, HomeActivity.this);
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest =
                new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        if(location!=null){
                            Log.v("AAA", location.getLatitude() + " " + location.getLongitude());
                            teamGeoFire.setLocation(currentProfileCode, new GeoLocation(location.getLatitude()
                                    , location.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    if(error!=null){
                                        Log.v("AAA", "Some error occurred");
                                    }else {
                                        currLat = location.getLatitude();
                                        currLon = location.getLongitude();
                                        Log.v("AAA", "Location added");
                                        GeoQuery geoQuery = teamGeoFire.queryAtLocation(new GeoLocation(location.getLatitude(),
                                                location.getLongitude()), 2000);

                                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                            @Override
                                            public void onKeyEntered(String key, GeoLocation location) {
//                                                if(!key.equals(currentProfileCode))
//                                                playerIds.add(new PlayerIdLoc(key, location.latitude, location.longitude));
                                                teamIds.add(new TeamIdLoc(key, location.latitude, location.longitude));
                                                Log.v("AAA", key);
                                            }

                                            @Override
                                            public void onKeyExited(String key) {
                                                Log.v("AAA", "exited");
                                            }

                                            @Override
                                            public void onKeyMoved(String key, GeoLocation location) {
                                                Log.v("AAA", "moved");
                                            }

                                            @Override
                                            public void onGeoQueryReady() {
                                                Log.v("AAA", "DOne");
//                                                Toast.makeText(HomeActivity.this,
//                                                        "Teams and players will be fetched",
//                                                        Toast.LENGTH_LONG).show();
                                                fetchTeamsForChallenging();
                                            }

                                            @Override
                                            public void onGeoQueryError(DatabaseError error) {
                                                Log.v("AAA", "error");
                                            }
                                        });
                                    }
                                }
                            });
                        }
                        else {
                            Log.v("AAA", "Location null");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        checkLocationPermission();
                        Log.v("AAA", "failed");
                    }
                });

        HashMap<String, String> userPictures = (HashMap<String, String>) documentSnapshot.get("pictures");
        Glide.with(this)
                .load(userPictures.get("0"))
                .circleCrop()
                .into(profile1);
        profile1.setBackground(getResources().getDrawable(R.drawable.round_image_100));
    }

    private void fetchTeamsForChallenging(){
        teamCardDetails = new ArrayList<>();
        for (int i=0; i<teamIds.size(); i++) {
            final int index = i;
            database.collection("teams").document(currentSport)
                    .collection("teams").document(teamIds.get(i).getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                final DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    database.collection("teams").document(currentSport)
                                            .collection("teams").document(currentProfileCode)
                                            .collection("teamsResponse").document(documentSnapshot.get("fullCode").toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot1 = task.getResult();
                                                        if (!documentSnapshot1.exists())
                                                            continueAddingTeamForChallenging(documentSnapshot, index);
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (teamCardDetails.size() == 0) {
                        if (fragment instanceof DiscoverFragment)
                            ((NoTeamAvailableInGivenRadiusListener) fragment).noTeamUpdate("No team around, try increasing the distance");
                    }
                }
            }, 3000);
//            if(fragment instanceof DiscoverFragment){
//                ((TeamViewEnabledListener) fragment).hideSportsTabBar();
//            }
        }
    }

    private void continueAddingTeamForChallenging(DocumentSnapshot documentSnapshot, int index){
        HashMap<String, String> pics = (HashMap<String, String>) documentSnapshot.get("pictures");
        double distance = Math.sqrt(
                Math.pow(Math.abs(teamIds.get(index).getLat() - currLat) * 110.574, 2) +
                        Math.pow(Math.abs(Math.cos(teamIds.get(index).getLon()) - Math.cos(currLon)) * 111.32, 2));
        String distanceWith1Decimal = (distance + "").substring(0, (distance + "").indexOf(".")) +
                (distance + "").substring((distance + "").indexOf("."), (distance + "").indexOf(".") + 2);
        String friendlyOrNot;
        if(documentSnapshot.get("maxBet").equals(0))
            friendlyOrNot = getString(R.string.bullet) + " Friendly Matches Only";
        else
            friendlyOrNot = getString(R.string.bullet) + " Max Bet " + documentSnapshot.get("maxBet");
        if (documentSnapshot.get("totalMatches") == null) {
            teamCardDetails.add(new TeamCardDetails(
                    friendlyOrNot,
                    "No matches played till now",
                    distanceWith1Decimal + " Kms Away",
                    pics,
                    documentSnapshot.get("name").toString(),
                    documentSnapshot.get("sport").toString(),
                    documentSnapshot.get("fullCode").toString()
            ));
        } else {
            teamCardDetails.add(new TeamCardDetails(
                    friendlyOrNot,
                    documentSnapshot.get("totalMatches").toString() + " Matches " + getString(R.string.bullet) + " "
                            + documentSnapshot.get("wonMatches").toString() + " Won " + getString(R.string.bullet) + " "
                            + documentSnapshot.get("lostMatches").toString() + " Lost",
                    distanceWith1Decimal + " Kms Away",
                    pics,
                    documentSnapshot.get("name").toString(),
                    documentSnapshot.get("sport").toString(),
                    documentSnapshot.get("fullCode").toString()
            ));
        }

        if (fragment instanceof DiscoverFragment)
            ((TeamsListReadyListener) fragment).updateTeamsList(teamCardDetails);
    }

    private boolean checkLocationPermission(){
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            ActivityCompat.requestPermissions(this,
                    permissions, REQUEST_LOCATION_PERMISSION);
        }
        return false;

    }

    @Override
    public void updateSport(String sport) {
        currentSport = sport;
        if(isPlayer)
            fetchTeamsForJoining();
        else fetchTeamsForChallenging();
    }

    private boolean pressedBack = false;

    @Override
    public void onBackPressed() {
        if(fragment instanceof ChatFragment){
            messagesButtonClicked();
            return;
        }
        if(!(fragment instanceof DiscoverFragment)){
            homeButton.performClick();
            return;
        }
        if(pressedBack)
            super.onBackPressed();
        else {
            pressedBack = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pressedBack = false;
                }
            }, 2000);
        }
    }

    @Override
    public void changeMessageIcon(boolean isMessages) {
        if(isMessages)
            messagesIcon.setImageDrawable(getDrawable(R.drawable.vertical_ellipses));
        else  messagesIcon.setImageDrawable(getDrawable(R.drawable.chat));
    }


    @Override
    public void updateTitle(String title) {
        pageTitle.setText(title);
    }

    @Override
    public void changeFragment(Fragment fragment) {
        this.fragment = fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, fragment);
        ft.commit();
    }

    private void messagesButtonClicked(){
        fragment  = new MessagesFragment(HomeActivity.this, database, mRef, mAuth);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, fragment);
        ft.commit();

        pageTitle.setText("Messages");
        dropdownIcon.setVisibility(View.GONE);
        gear.setImageDrawable(getResources().getDrawable(R.drawable.gear_not));
        home.setImageDrawable(getResources().getDrawable(R.drawable.home_not));
        favs.setImageDrawable(getResources().getDrawable(R.drawable.favs_not));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Utils.LOCATION_SETTING_REQUEST){
           if(resultCode == 0) {
                //show location dikha bhai
           }
           else {
               Toast.makeText(getApplicationContext(), "Granted", Toast.LENGTH_LONG).show();
               finish();
               startActivity(new Intent(this, HomeActivity.class));
           }
        }
    }
}
