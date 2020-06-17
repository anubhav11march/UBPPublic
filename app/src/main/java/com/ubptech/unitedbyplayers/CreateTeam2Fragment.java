package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    RangeSeekBar distance, betAmount, ageGroup;

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
        distance = view.findViewById(R.id.distance_seek);
        betAmount = view.findViewById(R.id.bet_seek);
        ageGroup = view.findViewById(R.id.age_group_seek);

        CharSequence[] distanceArray = {"0", "50"};
        distance.setTickMarkTextArray(distanceArray);
        distance.setIndicatorTextDecimalFormat("0");
        CharSequence[] betArray = {"0", "50000"};
        betAmount.setTickMarkTextArray(betArray);
        betAmount.setIndicatorTextDecimalFormat("0");
        betAmount.setSteps(100);
        betAmount.setStepsAutoBonding(true);
        CharSequence[] ageGroupArray = {"13", "99"};
        ageGroup.setTickMarkTextArray(ageGroupArray);
        ageGroup.setIndicatorTextDecimalFormat("0");
        ageGroup.setSteps(86);
        ageGroup.setStepsAutoBonding(true);


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

        ageGroup.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                ((CreateTeamChange) activity).setAgeGroup((int)leftValue + "-" + (int)rightValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });
    }
}
