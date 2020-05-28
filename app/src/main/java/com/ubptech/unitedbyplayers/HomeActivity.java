package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
            finish();
        }

    }

    public void click(View view){
        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
