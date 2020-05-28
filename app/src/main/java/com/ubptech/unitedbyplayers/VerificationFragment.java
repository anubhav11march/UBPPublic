package com.ubptech.unitedbyplayers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class VerificationFragment extends Fragment {

    Context context;
    LinearLayout verifyButton;
    User user;
    FirebaseAuth mAuth;

    VerificationFragment(User user){
        this.user = user;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        verifyButton = (LinearLayout) view.findViewById(R.id.verify_button);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ((FragmentChange) context).requestOTP("verifiedAccount");
                    Toast.makeText(context, "Successfully created account, please login", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

}
