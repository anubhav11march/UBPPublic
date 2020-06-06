package com.ubptech.unitedbyplayers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public class Onboarding2Fragment extends Fragment {
    private ProgressBar progressBar;
    private LinearLayout cricketCard, footballCard, badmintonCard;
    private ImageView cricketTick, footballTick, badmintonTick, cricketImage, footballImage, badmintonImage;
    private TextView cricketTitle, footballTitle, badmintonTitle;
    private boolean cricketSelect = false, footballSelect = false, badmintonSelect = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_2, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setProgress(66);
        initiateCards(view);
        return view;
    }

    private void initiateCards(View view){
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
                }
                else{
                    cricketCard.setBackground(getResources().getDrawable(R.drawable.sports_rounded_blue_card));
                    cricketImage.setColorFilter(getResources().getColor(R.color.white));
                    cricketTitle.setTextColor(getResources().getColor(R.color.white));
                    cricketTick.setVisibility(View.VISIBLE);
                    cricketSelect = true;
                }
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
                }
                else{
                    footballCard.setBackground(getResources().getDrawable(R.drawable.sports_rounded_blue_card));
                    footballImage.setColorFilter(getResources().getColor(R.color.white));
                    footballTitle.setTextColor(getResources().getColor(R.color.white));
                    footballTick.setVisibility(View.VISIBLE);
                    footballSelect = true;
                }
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
                }
                else{
                    badmintonCard.setBackground(getResources().getDrawable(R.drawable.sports_rounded_blue_card));
                    badmintonImage.setColorFilter(getResources().getColor(R.color.white));
                    badmintonTitle.setTextColor(getResources().getColor(R.color.white));
                    badmintonTick.setVisibility(View.VISIBLE);
                    badmintonSelect = true;
                }
            }
        });
    }
}
