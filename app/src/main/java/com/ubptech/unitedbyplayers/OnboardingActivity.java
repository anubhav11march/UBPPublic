package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Kylodroid on 04-06-2020.
 */
public class OnboardingActivity extends AppCompatActivity implements ViewPageChange {

    ViewPager viewPager;
    LinearLayout nextButton;
    int position =0;
    boolean checkViewPager = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new SliderAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(position);
        nextButton = (LinearLayout) findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(checkViewPager){
                    viewPager.setCurrentItem(++position);
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
