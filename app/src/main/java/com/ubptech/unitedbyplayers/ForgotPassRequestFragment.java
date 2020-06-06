package com.ubptech.unitedbyplayers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class ForgotPassRequestFragment extends Fragment {

    private Context context;
    private LinearLayout sendButton;
    FirebaseAuth mAuth;
    EditText email;

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
        View view =  inflater.inflate(R.layout.fragment_forgot_pass_request, container, false);
        sendButton = (LinearLayout) view.findViewById(R.id.send_button);
        email = (EditText) view.findViewById(R.id.email);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = email.getText().toString().trim();
                if(isValid(emailAddress)){
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Please check your email to reset password.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    try{
                        ((FragmentChange) context).requestOTP("forgotPassword", null);
                    }catch (Exception e){
                        Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    email.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
                    Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
