package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Kylodroid on 08-06-2020.
 */
public class CreateTeam1Fragment extends Fragment {

    LinearLayout football, cricket, badminton, yes, no;
    EditText teamName;
    TextView footballTitle, cricktTitle, badmintonTitle, yesTitle, noTitle;
    ImageView footballTick, cricketTick, badmintonTick;
    Activity activity;

    CreateTeam1Fragment(Activity activity){
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_team_1, container, false);
        initiate(view);
        return view;
    }

    private void initiate(View view){
        football = view.findViewById(R.id.football);
        footballTitle = view.findViewById(R.id.football_title);
        footballTick = view.findViewById(R.id.football_tick);
        cricket = view.findViewById(R.id.cricket);
        cricktTitle = view.findViewById(R.id.cricket_title);
        cricketTick = view.findViewById(R.id.cricket_tick);
        badminton = view.findViewById(R.id.badminton);
        badmintonTitle = view.findViewById(R.id.badminton_title);
        badmintonTick = view.findViewById(R.id.badminton_tick);
        yes = view.findViewById(R.id.yes);
        yesTitle = view.findViewById(R.id.yes_title);
        no = view.findViewById(R.id.no);
        noTitle = view.findViewById(R.id.no_title);
        teamName = view.findViewById(R.id.team_name);

        teamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((CreateTeamChange) activity).setTeamName(teamName.getText().toString().trim());
            }
        });

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelectedSports();
                football.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                footballTitle.setTextColor(getResources().getColor(R.color.white));
                footballTick.setVisibility(View.VISIBLE);
                ((CreateTeamChange) activity).setSport("football");
            }
        });

        cricket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelectedSports();
                cricket.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                cricktTitle.setTextColor(getResources().getColor(R.color.white));
                cricketTick.setVisibility(View.VISIBLE);
                ((CreateTeamChange) activity).setSport("cricket");
            }
        });

        badminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeSelectedSports();
                badminton.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                badmintonTitle.setTextColor(getResources().getColor(R.color.white));
                badmintonTick.setVisibility(View.VISIBLE);
                ((CreateTeamChange) activity).setSport("badminton");
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
                noTitle.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
                yes.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                yesTitle.setTextColor(getResources().getColor(R.color.white));
                ((CreateTeamChange) activity).setOtherPlayers(true);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
                yesTitle.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
                no.setBackground(getResources().getDrawable(R.drawable.blue_selected_button));
                noTitle.setTextColor(getResources().getColor(R.color.white));
                ((CreateTeamChange) activity).setOtherPlayers(false);
            }
        });
    }

    private void removeSelectedSports(){
        football.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        footballTitle.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        footballTick.setVisibility(View.GONE);
        cricket.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        cricktTitle.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        cricketTick.setVisibility(View.GONE);
        badminton.setBackground(getResources().getDrawable(R.drawable.white_selectable_button));
        badmintonTitle.setTextColor(getResources().getColor(R.color.onboarding_button_blue));
        badmintonTick.setVisibility(View.GONE);
    }
}
