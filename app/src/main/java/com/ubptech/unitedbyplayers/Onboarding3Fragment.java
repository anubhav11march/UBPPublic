package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public class Onboarding3Fragment extends Fragment {

    private RangeSeekBar distanceSeek, ageGroupSeek;
    private LinearLayout pickLayout, male, female;
    private TextView done, maleText, femaleText, hiPlayer;
    private int gender = 0;
    private Activity activity;
    private EditText ageText;

    Onboarding3Fragment(Activity activity) {
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

    private void initiate(View view) {
        ageText = view.findViewById(R.id.age_text);
        done = view.findViewById(R.id.done);
        pickLayout = view.findViewById(R.id.age_pick);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        maleText = view.findViewById(R.id.male_text);
        femaleText = view.findViewById(R.id.female_text);
        hiPlayer = view.findViewById(R.id.hi_player);
        ageGroupSeek = view.findViewById(R.id.age_group_seek);

        CharSequence[] ageArray = {"13", "99"};
        ageGroupSeek.setTickMarkTextArray(ageArray);
        ageGroupSeek.setIndicatorTextDecimalFormat("0");

        hiPlayer.setText("Hi " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + ".");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLayout.setVisibility(View.GONE);
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender == 0) {
                    male.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                    maleText.setTextColor(getResources().getColor(R.color.white));
                    gender = 1;
                } else if (gender == 2) {
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
                if (gender == 0) {
                    female.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                    femaleText.setTextColor(getResources().getColor(R.color.white));
                    gender = 2;
                } else if (gender == 1) {
                    female.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                    femaleText.setTextColor(getResources().getColor(R.color.white));
                    gender = 2;
                    male.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
                    maleText.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
                }
                ((ViewPageChange) activity).addGender(2);
            }
        });

        distanceSeek.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                ((ViewPageChange) activity).addDistance((int) leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        ageGroupSeek.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                ((ViewPageChange) activity).addAgeGroup((int)leftValue + "-" + (int)rightValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        ageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((ViewPageChange) activity).addAge(Integer.parseInt(ageText.getText().toString().trim()));
            }
        });
    }

}
