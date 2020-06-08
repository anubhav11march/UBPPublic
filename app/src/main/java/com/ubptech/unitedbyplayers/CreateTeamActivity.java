package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class CreateTeamActivity extends AppCompatActivity {

    LinearLayout nextButton;
    static int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        getSupportActionBar().hide();
        nextButton = (LinearLayout) findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(++pos);
            }
        });

        displayFragment(pos);
    }

    void displayFragment(int position){
        Fragment fragment = null;
        switch (position){
            case 0: fragment = new CreateTeam1Fragment();
                        break;
            case 1: fragment = new CreateTeam2Fragment();
                break;
            case 2: fragment = new CreateTeam3Fragment();
                break;
        }
        if(fragment!=null){
            FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.options, fragment);
            ft.commitAllowingStateLoss();
        }

    }
}
