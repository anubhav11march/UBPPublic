package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jaygoo.widget.RangeSeekBar;

/**
 * Created by Kylodroid on 21-06-2020.
 */
public class AppSettingsFragment extends Fragment {

    private Activity activity;
    private RangeSeekBar distance, ageGroup, betAmount;

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
    }
}
