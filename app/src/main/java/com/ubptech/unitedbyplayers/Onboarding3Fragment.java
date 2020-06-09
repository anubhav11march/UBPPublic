package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public class Onboarding3Fragment extends Fragment{

    private RangeSeekBar distanceSeek;
    private LinearLayout age, pickLayout, male, female, ten, fifteen, twenty, thirty, forty, fifty;
    private TextView ageText, done, maleText, femaleText, tenText, fifteenText, twentyText, thirtyText, fortyText, fiftyText, hiPlayer;
    private NumberPicker agePicker;
    private int gender = 0, ageGroup = 0;
    private Activity activity;
    int AGE, GENDER, AGEGROUP, DISTANCE;

    Onboarding3Fragment(Activity activity){
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_3, container, false);
        distanceSeek = view.findViewById(R.id.distance_seek);
        CharSequence[] array = {"0", "50"};
        distanceSeek.setTickMarkTextArray(array);
        distanceSeek.setIndicatorTextDecimalFormat("0");
        initiate(view);
        return view;
    }

    private void initiate(View view){
        age = view.findViewById(R.id.age);
        ageText = view.findViewById(R.id.age_text);
        agePicker = view.findViewById(R.id.number_picker);
        done = view.findViewById(R.id.done);
        pickLayout = view.findViewById(R.id.age_pick);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        ten = view.findViewById(R.id.ten);
        fifteen = view.findViewById(R.id.fifteen);
        twenty = view.findViewById(R.id.twenty);
        thirty = view.findViewById(R.id.thirty);
        forty = view.findViewById(R.id.forty);
        fifty = view.findViewById(R.id.fifty);
        maleText = view.findViewById(R.id.male_text);
        femaleText = view.findViewById(R.id.female_text);
        tenText = view.findViewById(R.id.ten_text);
        fifteenText = view.findViewById(R.id.fifteen_text);
        twentyText = view.findViewById(R.id.twenty_text);
        thirtyText = view.findViewById(R.id.thirty_text);
        fortyText = view.findViewById(R.id.forty_text);
        fiftyText = view.findViewById(R.id.fifty_text);
        hiPlayer = view.findViewById(R.id.hi_player);

        hiPlayer.setText("Hi " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + ".");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLayout.setVisibility(View.GONE);
            }
        });

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicker();
            }
        });

        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ageText.setText(i1+"");
                ((ViewPageChange) activity).addAge(i1);
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gender==0){
                    male.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                    maleText.setTextColor(getResources().getColor(R.color.white));
                    gender = 1;
                }
                else if(gender == 2){
                    male.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                    maleText.setTextColor(getResources().getColor(R.color.white));
                    gender = 1;
                    female.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
                    femaleText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
                }
                ((ViewPageChange) activity).addGender(1);
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gender==0){
                    female.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                    femaleText.setTextColor(getResources().getColor(R.color.white));
                    gender = 2;
                }
                else if(gender == 1){
                    female.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                    femaleText.setTextColor(getResources().getColor(R.color.white));
                    gender = 2;
                    male.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
                    maleText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
                }
                ((ViewPageChange) activity).addGender(2);
            }
        });

        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                ten.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                tenText.setTextColor(getResources().getColor(R.color.white));
                ((ViewPageChange) activity).addAgeGroup("10-15");
            }
        });

        fifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                fifteen.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                fifteenText.setTextColor(getResources().getColor(R.color.white));
                ((ViewPageChange) activity).addAgeGroup("15-20");
            }
        });

        twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                twenty.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                twentyText.setTextColor(getResources().getColor(R.color.white));
                ((ViewPageChange) activity).addAgeGroup("20-30");
            }
        });

        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                thirty.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                thirtyText.setTextColor(getResources().getColor(R.color.white));
                ((ViewPageChange) activity).addAgeGroup("30-40");
            }
        });

        forty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                forty.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                fortyText.setTextColor(getResources().getColor(R.color.white));
                ((ViewPageChange) activity).addAgeGroup("40-50");
            }
        });

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                fifty.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                fiftyText.setTextColor(getResources().getColor(R.color.white));
                ((ViewPageChange) activity).addAgeGroup("50+");
            }
        });

        distanceSeek.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                ((ViewPageChange) activity).addDistance((int)leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }

    private void removeSelections(){
        fifty.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        fiftyText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        forty.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        fortyText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        thirty.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        thirtyText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        twenty.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        twentyText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        fifteen.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        fifteenText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        ten.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        tenText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
    }

    private void showPicker(){
        pickLayout.setVisibility(View.VISIBLE);
        agePicker.setMinValue(13);
        agePicker.setMaxValue(80);
    }

}
