package com.ubptech.unitedbyplayers;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by Kylodroid on 15-06-2020.
 */
class CreateTeamSliderAdapter extends FragmentPagerAdapter {

    Activity activity;

    CreateTeamSliderAdapter(FragmentManager fragmentManager, Activity activity){
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CreateTeam1Fragment(activity);
            case 1:
                return new CreateTeam2Fragment(activity);
            case 2:
                return new CreateTeam3Fragment(activity);
            default:
                return new CreateTeam1Fragment(activity);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
