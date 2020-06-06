package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kylodroid on 04-06-2020.
 */
public class OnboardingActivity extends AppCompatActivity implements ViewPageChange {

    ViewPager viewPager;
    LinearLayout nextButton;
    int position =0;
    ProgressBar progressBar;
    TextView progressFraction;
    boolean checkViewPager = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new SliderAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(position);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressFraction = findViewById(R.id.progress_fraction);
        progressFraction.setText((position+1) + "/3");
        nextButton = (LinearLayout) findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(checkViewPager){
                    viewPager.setCurrentItem(++position);
                    progressFraction.setText((position+1) + "/3");
                    if(android.os.Build.VERSION.SDK_INT>=24)
                        progressBar.setProgress(33*(position+1), true);
                    else
                        progressBar.setProgress(33*(position+1));
//                }
//                else {
//                    Toast.makeText(OnboardingActivity.this, "Please complete the above steps to continue"
//                    , Toast.LENGTH_SHORT).show();
//                    viewPager.setCurrentItem(position);
//                }
            }
        });
    }

    @Override
    public void check(boolean done) {
        checkViewPager = done;
    }
}
