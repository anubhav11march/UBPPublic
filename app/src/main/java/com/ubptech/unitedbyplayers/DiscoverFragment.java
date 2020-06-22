package com.ubptech.unitedbyplayers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by Kylodroid on 21-06-2020.
 */
public class DiscoverFragment extends Fragment {

    TabLayout sportsTabs;
    TabItem cricket, football, badminton, tennis, basketball;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dicover, container, false);
        initializeUIElements(view);
        return view;
    }

    private void initializeUIElements(View view){
        sportsTabs = view.findViewById(R.id.sports_tabs);
        cricket = view.findViewById(R.id.cricket);

        sportsTabs.selectTab(sportsTabs.getTabAt(1), true);
    }
}
