package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class AuthActivity extends AppCompatActivity implements FragmentChange {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("type").equals("requestPass"))
                displayFragment("requestPass");
            else if(bundle.getString("type").equals("verify"))
                displayFragment("verify");
        }
    }

    public void displayFragment(String fragmentId){
        Fragment fragment = null;
        switch (fragmentId){
            case "requestPass":
                fragment = new ForgotPassRequestFragment();
                break;
            case "forgotPassVerify":
                fragment = new VerificationForgotPassFragment();
                break;
            case "verificationSuccessful":
                fragment = new ChangePassFragment();
                break;
            case "verify":
                user = (User) getIntent().getParcelableExtra("userData");
                String pass = getIntent().getExtras().getString("pass");
                fragment = new VerificationFragment(user, pass);
                break;
        }
        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentLayout, fragment);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void requestOTP(String source) {
        if(source.equals("forgotPassword")) {
//            displayFragment("forgotPassVerify");
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        else if(source.equals("verificationSuccessful"))
            displayFragment("verificationSuccessful");
        else if(source.equals("passwordChanged") || source.equals("verifiedAccount")){
            startActivity(new Intent(AuthActivity.this, HomeActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}
