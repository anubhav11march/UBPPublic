package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class VerificationFragment extends Fragment {

    Context context;
    LinearLayout verifyButton;
    TextView sendAgain;
    User user;
    FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationId, otp, pass;
    EditText a1, a2, a3, a4, a5, a6;

    VerificationFragment(User user, String pass){
        this.user = user;
        this.pass = pass;
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

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(context, "Code sent", Toast.LENGTH_SHORT).show();
                verificationId = s;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        verifyButton = (LinearLayout) view.findViewById(R.id.verify_button);
        sendAgain = (TextView) view.findViewById(R.id.send_again);
        a1 = (EditText) view.findViewById(R.id.a1);
        a2 = (EditText) view.findViewById(R.id.a2);
        a3 = (EditText) view.findViewById(R.id.a3);
        a4 = (EditText) view.findViewById(R.id.a4);
        a5 = (EditText) view.findViewById(R.id.a5);
        a6 = (EditText) view.findViewById(R.id.a6);

        a1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(a1.getText().toString().length()==1){
                    a2.requestFocus();
                }
            }
        });

        a2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(a2.getText().toString().length()==1){
                    a3.requestFocus();
                }
            }
        });

        a3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(a3.getText().toString().length()==1){
                    a4.requestFocus();
                }
            }
        });

        a4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(a4.getText().toString().length()==1){
                    a5.requestFocus();
                }
            }
        });

        a5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(a5.getText().toString().length()==1){
                    a6.requestFocus();
                }
            }
        });

        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "OTP has been resent to you", Toast.LENGTH_SHORT).show();
            }
        });


        startAuth();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOtp();
            }
        });
        return view;
    }

    void startAuth(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+user.getPhoneNumber(),
                10,
                TimeUnit.SECONDS,
                (Activity) context,
                mCallbacks
        );
    }

    void verifyOtp(){
        otp = a1.getText().toString() + a2.getText().toString() + a3.getText().toString() +
                a4.getText().toString() + a5.getText().toString() + a6.getText().toString();
        if(verificationId==null){
            Toast.makeText(context, "Please wait while the otp is being sent to you", Toast.LENGTH_SHORT).show();
            a1.setText("");
            a2.setText("");
            a3.setText("");
            a4.setText("");
            a5.setText("");
            a6.setText("");
            return;
        }
        if(otp.length()!=6) {
            Toast.makeText(context, "Please enter the otp sent to you", Toast.LENGTH_SHORT).show();
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                        }
                        else{
                            Toast.makeText((Activity) context,
                                    "Please enter the correct otp received", Toast.LENGTH_SHORT).show();
                            a1.setText("");
                            a2.setText("");
                            a3.setText("");
                            a4.setText("");
                            a5.setText("");
                            a6.setText("");
                        }
                    }
                });
        mAuth.signOut();
        FirebaseAuth.getInstance().signOut();
//        AuthCredential credential1 = EmailAuthProvider.getCredential();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            try{
                                ((FragmentChange) context).requestOTP("verifiedAccount", user);
                                Toast.makeText(context, "Successfully created account", Toast.LENGTH_SHORT).show();

                            }catch (Exception e){
                                Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(context, "This email id already exists with another login method," +
                                        "Please sign in using that", Toast.LENGTH_SHORT).show();
                            }
                            else
                            Toast.makeText(context, "Problem " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mAuth.signOut();
        FirebaseAuth.getInstance().signOut();
    }

}
