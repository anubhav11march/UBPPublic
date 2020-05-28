package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("requestPass").equals("true")){
                displayFragment("requestPass");
            }
        }
    }

    public void displayFragment(String fragmentId){
        Fragment fragment = null;
        switch (fragmentId){
            case "requestPass":
                fragment = new ForgotPassRequestFragment();
                break;
        }
        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentLayout, fragment);
            ft.commitAllowingStateLoss();
        }
    }
}
