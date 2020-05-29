package com.ubptech.unitedbyplayers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class RegistrationActivity extends AppCompatActivity {

    EditText fullName, phoneNumber, email, password, confirmPassword;
    User user;
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        fullName = (EditText) findViewById(R.id.full_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                fullName.setBackgroundTintList(
                        getApplicationContext().getResources()
                                .getColorStateList(R.color.white));
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                phoneNumber.setBackgroundTintList(
                        getApplicationContext().getResources()
                                .getColorStateList(R.color.white));
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email.setBackgroundTintList(
                        getApplicationContext().getResources()
                                .getColorStateList(R.color.white));
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password.setBackgroundTintList(
                        getApplicationContext().getResources()
                                .getColorStateList(R.color.white));
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                confirmPassword.setBackgroundTintList(
                        getApplicationContext().getResources()
                                .getColorStateList(R.color.white));
            }
        });

        ImageView imageView = findViewById(R.id.googlesignin);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLoginClicked();
            }
        });
    }
    public void googleLoginClicked() {

    }

    public void signupClicked(View view){
        if(!checkConstraints())
            return;
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("type", "verify");
        intent.putExtra("userData",  user);
        intent.putExtra("pass", password.getText().toString().trim());
        startActivity(intent);
    }

    boolean checkConstraints(){
        String tFullname = fullName.getText().toString().trim(),
        tPhoneNumber = phoneNumber.getText().toString().trim(),
        tEmail = email.getText().toString().trim(),
        tPassword = password.getText().toString().trim(),
        tConfirmPassword = confirmPassword.getText().toString().trim();

        if(tFullname.length()<2){
            fullName.setBackgroundTintList(this.getResources().getColorStateList(R.color.red));
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tPhoneNumber.length()!=10){
            phoneNumber.setBackgroundTintList(this.getResources().getColorStateList(R.color.red));
            Toast.makeText(this, "Phone Number should be of 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!isValid(tEmail)){
            email.setBackgroundTintList(this.getResources().getColorStateList(R.color.red));
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(tPassword.length() <= 6){
            password.setBackgroundTintList(this.getResources().getColorStateList(R.color.red));
            Toast.makeText(this, "Password length should be more than 6", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!tPassword.equals(tConfirmPassword)){
            confirmPassword.setBackgroundTintList(this.getResources().getColorStateList(R.color.red));
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US),
                sdfTime = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.US);
        Date date = new Date();

        user = new User(tFullname, tPhoneNumber, tEmail, "email", sdfDate.format(date),
                sdfTime.format(date));
        return true;
    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
