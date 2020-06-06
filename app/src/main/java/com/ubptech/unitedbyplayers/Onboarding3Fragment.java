package com.ubptech.unitedbyplayers;

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

import com.jaygoo.widget.RangeSeekBar;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public class Onboarding3Fragment extends Fragment{

    private RangeSeekBar distanceSeek;
    private LinearLayout age, pickLayout;
    private TextView ageText, done;
    private NumberPicker agePicker;

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
                Toast.makeText(getContext(), "dafa", Toast.LENGTH_SHORT).show();
            }
        });

        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                ageText.setText(i1+"");
            }
        });
    }

    private void showPicker(){
        pickLayout.setVisibility(View.VISIBLE);
        agePicker.setMinValue(13);
        agePicker.setMaxValue(80);
    }

}
