package com.ubptech.unitedbyplayers;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangePassFragment extends Fragment {

    Context context;
    LinearLayout continueButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        continueButton = (LinearLayout) view.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ((FragmentChange) context).requestOTP("passwordChanged");
                    Toast.makeText(context, "Successfully changed password, please login", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}
