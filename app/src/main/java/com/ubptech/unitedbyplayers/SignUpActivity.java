package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.facebook.login.LoginManager;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class SignUpActivity extends AppCompatActivity {

    LinearLayout signUpButton, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        Utils.setStatusBarDarkColor(window, this);
        signUpButton = (LinearLayout) findViewById(R.id.signup_button);
        loginButton = (LinearLayout) findViewById(R.id.login_button);

        if(LoginManager.getInstance()!=null){
            LoginManager.getInstance().logOut();
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, RegistrationActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });


    }
}
