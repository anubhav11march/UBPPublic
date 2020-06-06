package com.ubptech.unitedbyplayers;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class VerificationForgotPassFragment extends Fragment {

    private Context context;
    private LinearLayout submitButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification_forgot_pass, container, false);
        submitButton = (LinearLayout) view.findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ((FragmentChange) context).requestOTP("verificationSuccessful", null);
                }catch (Exception e){
                    Toast.makeText(context, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
