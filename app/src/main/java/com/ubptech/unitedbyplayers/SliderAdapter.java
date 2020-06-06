package com.ubptech.unitedbyplayers;

import android.content.Context;
import android.text.Layout;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public class SliderAdapter extends FragmentPagerAdapter {

    SliderAdapter(FragmentManager fragmentManager){
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Onboarding1Fragment();
            case 1:
                return new Onboarding2Fragment();
            default:
                return new Onboarding1Fragment();
        }

    }

}
