package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

/**
 * Created by Kylodroid on 08-06-2020.
 */
public class CreateTeam2Fragment extends Fragment {

    Activity activity;
    private LinearLayout ten, fifteen, twenty, thirty, fifty;
    private TextView tenText, fifteenText, twentyText, thirtyText, fiftyText;
    RangeSeekBar distance, betAmount;

    CreateTeam2Fragment(Activity activity){
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_team_2, container, false);
        initiate(view);
        return view;
    }

    private void initiate(View view){
        ten = view.findViewById(R.id.ten);
        fifteen = view.findViewById(R.id.fifteen);
        twenty = view.findViewById(R.id.twenty);
        thirty = view.findViewById(R.id.thirty);
        fifty = view.findViewById(R.id.fifty);
        tenText = view.findViewById(R.id.ten_text);
        fifteenText = view.findViewById(R.id.fifteen_text);
        twentyText = view.findViewById(R.id.twenty_text);
        thirtyText = view.findViewById(R.id.thirty_text);
        fiftyText = view.findViewById(R.id.fifty_text);
        distance = view.findViewById(R.id.distance_seek);
        betAmount = view.findViewById(R.id.bet_seek);

        CharSequence[] distanceArray = {"0", "50"};
        distance.setTickMarkTextArray(distanceArray);
        distance.setIndicatorTextDecimalFormat("0");
        CharSequence[] betArray = {"0", "10000"};
        betAmount.setTickMarkTextArray(betArray);
        betAmount.setIndicatorTextDecimalFormat("0");
        betAmount.setSteps(100);

        distance.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                ((CreateTeamChange) activity).setDistance((int)leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        betAmount.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                ((CreateTeamChange) activity).setMaxBet((int)leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                ten.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                tenText.setTextColor(getResources().getColor(R.color.white));
                ((CreateTeamChange) activity).setAgeGroup("10-15");
            }
        });

        fifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                fifteen.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                fifteenText.setTextColor(getResources().getColor(R.color.white));
                ((CreateTeamChange) activity).setAgeGroup("15-20");
            }
        });

        twenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                twenty.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                twentyText.setTextColor(getResources().getColor(R.color.white));
                ((CreateTeamChange) activity).setAgeGroup("20-30");
            }
        });

        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                thirty.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                thirtyText.setTextColor(getResources().getColor(R.color.white));
                ((CreateTeamChange) activity).setAgeGroup("30-40");
            }
        });

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelections();
                fifty.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                fiftyText.setTextColor(getResources().getColor(R.color.white));
                ((CreateTeamChange) activity).setAgeGroup("50+");
            }
        });
    }

    private void removeSelections(){
        fifty.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        fiftyText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        thirty.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        thirtyText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        twenty.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        twentyText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        fifteen.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        fifteenText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        ten.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        tenText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
    }
}
