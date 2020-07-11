package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private SwitchCompat hideProfile, appNotifs;
    private DocumentReference documentReference;

    AppSettingsFragment(Activity activity, DocumentReference documentReference){
        this.activity = activity;
        this.documentReference = documentReference;
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
        if(!(preferences.getInt("ageRight", 0) == 0))
            ageGroup.setProgress(preferences.getInt("ageLeft", 0), preferences.getInt("ageRight", 0));
        if(!(preferences.getInt("maxDistance", 0) == 0))
            distance.setProgress(preferences.getInt("maxDistance", 0));
        if(!(preferences.getInt("maxBet", 0) == 0))
            betAmount.setProgress(preferences.getInt("maxBet", 0));
        if((preferences.getBoolean("hideProfile", false)))
            hideProfile.setChecked(true);
        if((preferences.getBoolean("appNotifs", false)))
            appNotifs.setChecked(true);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            long distanceInt = (long) documentSnapshot.get("distance");
//                            int ageGroupLeft = Integer.parseInt(documentSnapshot.get("ageGroup").toString().substring(0, 2));
//                            int ageGroupRight = Integer.parseInt(documentSnapshot.get("ageGroup").toString().substring(3));
//                            if(ageGroupLeft<ageGroupRight)
//
                            distance.setProgress(distanceInt);
                        }
                    }
                });
    }

    private void initializeUIElements(View view){
        distance = view.findViewById(R.id.distance_seek);
        betAmount = view.findViewById(R.id.bet_seek);
        ageGroup = view.findViewById(R.id.age_group_seek);
        appNotifs = view.findViewById(R.id.app_notifs);
        hideProfile = view.findViewById(R.id.hide_profile);

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
        ageGroup.setProgress(15, 25);

        appNotifs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    editor.putBoolean("appNotifs", (boolean)b);
                    editor.apply();
            }
        });

        hideProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean("hideProfile", (boolean)b);
                editor.apply();
            }
        });

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
                    documentReference.update("distance", (int) leftValue)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
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
