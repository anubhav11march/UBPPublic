package com.ubptech.unitedbyplayers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Created by Kylodroid on 16-07-2020.
 */
public class RequestMatchFragment extends BottomSheetDialogFragment {

    public static RequestMatchFragment newInstance() {
        RequestMatchFragment fragment = new RequestMatchFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_match, container, false);
        return view;
    }
}
