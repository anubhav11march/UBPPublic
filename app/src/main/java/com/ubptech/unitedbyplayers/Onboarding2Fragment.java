package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public class Onboarding2Fragment extends Fragment {

    private LinearLayout cricketCard, footballCard, badmintonCard;
    private ImageView cricketTick, footballTick, badmintonTick, cricketImage, footballImage, badmintonImage;
    private TextView cricketTitle, footballTitle, badmintonTitle;
    private boolean cricketSelect = false, footballSelect = false, badmintonSelect = false;
    private HashMap<String, Boolean> preferences = new HashMap<>();
    private Activity activity;

    Onboarding2Fragment(Activity activity){
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_2, container, false);
        initiateCards(view);
        return view;
    }

    private void initiateCards(View view){
        preferences.put("Cricket", false);
        preferences.put("Football", false);
        preferences.put("Badminton", false);

        cricketCard = view.findViewById(R.id.cricket_card);
        footballCard = view.findViewById(R.id.football_card);
        badmintonCard = view.findViewById(R.id.badminton_card);
        cricketImage = view.findViewById(R.id.cricket_image);
        cricketTick = view.findViewById(R.id.cricket_tick);
        cricketTitle = view.findViewById(R.id.cricket_title);
        footballImage = view.findViewById(R.id.football_image);
        footballTick = view.findViewById(R.id.football_tick);
        footballTitle = view.findViewById(R.id.foorball_title);
        badmintonImage = view.findViewById(R.id.badminton_image);
        badmintonTick = view.findViewById(R.id.badminton_tick);
        badmintonTitle = view.findViewById(R.id.badminton_title);

        cricketCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cricketSelect){
                    cricketCard.setBackground(getResources().getDrawable(R.drawable.rounded_corner_card));
                    cricketImage.setColorFilter(getResources().getColor(R.color.unselected_sport));
                    cricketTitle.setTextColor(getResources().getColor(R.color.onboarding_blue));
                    cricketTick.setVisibility(View.GONE);
                    cricketSelect = false;
                    preferences.put("Cricket", false);
                }
                else{
                    cricketCard.setBackground(getResources().getDrawable(R.drawable.sports_rounded_blue_card));
                    cricketImage.setColorFilter(getResources().getColor(R.color.white));
                    cricketTitle.setTextColor(getResources().getColor(R.color.white));
                    cricketTick.setVisibility(View.VISIBLE);
                    cricketSelect = true;
                    preferences.put("Cricket", true);
                }
                ((ViewPageChange) activity).addPreferences(preferences);
                if(cricketSelect||footballSelect||badmintonSelect)
                    ((ViewPageChange) activity).check(true);
                else ((ViewPageChange) activity).check(false);
            }
        });

        footballCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(footballSelect){
                    footballCard.setBackground(getResources().getDrawable(R.drawable.rounded_corner_card));
                    footballImage.setColorFilter(getResources().getColor(R.color.unselected_sport));
                    footballTitle.setTextColor(getResources().getColor(R.color.onboarding_blue));
                    footballTick.setVisibility(View.GONE);
                    footballSelect = false;
                    preferences.put("Football", false);
                }
                else{
                    footballCard.setBackground(getResources().getDrawable(R.drawable.sports_rounded_blue_card));
                    footballImage.setColorFilter(getResources().getColor(R.color.white));
                    footballTitle.setTextColor(getResources().getColor(R.color.white));
                    footballTick.setVisibility(View.VISIBLE);
                    footballSelect = true;
                    preferences.put("Football", true);
                }
                ((ViewPageChange) activity).addPreferences(preferences);
                if(cricketSelect||footballSelect||badmintonSelect)
                    ((ViewPageChange) activity).check(true);
                else ((ViewPageChange) activity).check(false);
            }
        });

        badmintonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(badmintonSelect){
                    badmintonCard.setBackground(getResources().getDrawable(R.drawable.rounded_corner_card));
                    badmintonImage.setColorFilter(getResources().getColor(R.color.unselected_sport));
                    badmintonTitle.setTextColor(getResources().getColor(R.color.onboarding_blue));
                    badmintonTick.setVisibility(View.GONE);
                    badmintonSelect = false;
                    preferences.put("Badminton", true);
                }
                else{
                    badmintonCard.setBackground(getResources().getDrawable(R.drawable.sports_rounded_blue_card));
                    badmintonImage.setColorFilter(getResources().getColor(R.color.white));
                    badmintonTitle.setTextColor(getResources().getColor(R.color.white));
                    badmintonTick.setVisibility(View.VISIBLE);
                    badmintonSelect = true;
                    preferences.put("Badminton", true);
                }
                ((ViewPageChange) activity).addPreferences(preferences);
                if(cricketSelect||footballSelect||badmintonSelect)
                    ((ViewPageChange) activity).check(true);
                else ((ViewPageChange) activity).check(false);
            }
        });
    }
}
