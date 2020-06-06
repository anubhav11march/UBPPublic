package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

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
    Uri uri = null;
    HashMap<String, Boolean> preferences = null;
    int age, gender, distance;
    String ageGroup;
    View loadingView;
    AlertDialog.Builder builder;

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
                if(checkViewPager){
                    viewPager.setCurrentItem(++position);
                    progressFraction.setText((position+1) + "/3");
                    if(android.os.Build.VERSION.SDK_INT>=24)
                        progressBar.setProgress(33*(position+1), true);
                    else
                        progressBar.setProgress(33*(position+1));
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

    private void startOnboarding(){
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void check(boolean done) {
        checkViewPager = done;
    }

    @Override
    public void addPictureUri(Uri uri) {
        this.uri = uri;
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
