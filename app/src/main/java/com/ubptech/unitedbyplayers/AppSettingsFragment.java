package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

/**
 * Created by Kylodroid on 21-06-2020.
 */
public class AppSettingsFragment extends Fragment {

    private Activity activity;
    private RangeSeekBar distance, ageGroup, betAmount;
    private SharedPreferences preferences;
    private int ageLeft, ageRight, maxDistance, maxBet;
    private SharedPreferences.Editor editor;

    AppSettingsFragment(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_settings, container, false);
        initializeUIElements(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!(preferences.getInt("ageRight", 0) == 0)){
//            ageGroup.setRight(preferences.getInt("ageRight", 0));
//            ageGroup.setLeft(preferences.getInt("ageLeft", 0));
            ageGroup.setProgress(preferences.getInt("ageLeft", 0), preferences.getInt("ageRight", 0));
        }
        if(!(preferences.getInt("maxDistance", 0) == 0)){
//            ageGroup.invalidate();
//            ageGroup.setLeft(preferences.getInt("maxDistance", 0));
            distance.setProgress(preferences.getInt("maxDistance", 0));
        }
        Toast.makeText(activity.getApplicationContext(), preferences.getInt("maxDistance", 0)+"", Toast.LENGTH_SHORT).show();
        if(!(preferences.getInt("maxBet", 0) == 0)){
//            ageGroup.setLeft(preferences.getInt("maxBet", 0));
            betAmount.setProgress(preferences.getInt("maxBet", 0));
        }
    }

    private void initializeUIElements(View view){
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

        ageGroup.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if(isFromUser) {
                    editor.putInt("ageLeft", (int) leftValue);
                    editor.putInt("ageRight", (int) rightValue);
                    editor.apply();
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        distance.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if(isFromUser) {
                    editor.putInt("maxDistance", (int) leftValue);
                    editor.apply();
                }
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
                if(isFromUser) {
                    editor.putInt("maxBet", (int) leftValue);
                    editor.apply();
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        preferences = activity.getSharedPreferences("appSettings", Context.MODE_PRIVATE);
        editor = preferences.edit();

    }
}
