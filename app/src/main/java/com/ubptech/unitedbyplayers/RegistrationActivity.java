package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class RegistrationActivity extends AppCompatActivity {

    EditText fullName, phoneNumber, email, password, confirmPassword;
    User user;
    ArrayList<User> users = new ArrayList<>();
    int RC_SIGNIN = 0;
    CallbackManager callbackManager;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;
    ImageView fbButton, googleButton;
    View loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        Window window = this.getWindow();
        Utils.setStatusBarDarkColor(window, this);
        fullName = (EditText) findViewById(R.id.full_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        fbButton = (ImageView) findViewById(R.id.fb_button);
        googleButton = (ImageView) findViewById(R.id.google_button);
        loadingView = (View) findViewById(R.id.loading_view);


        FacebookSdk.sdkInitialize(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("898532588623-0ej1hpm98ga9uiv57264vluuo05a1cnr.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbLogin();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Cancelled Login", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        loadingView.setVisibility(View.GONE);
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(), "This email already exists with another login method, " +
                                "please login with that method", Toast.LENGTH_SHORT).show();
                    }
                }
        );

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
    }

    void googleLogin(){
        loadingView.setVisibility(View.VISIBLE);
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 9001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 9001){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e){
                Toast.makeText(this, "Failed Login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                            intent.putExtra("method", "google");
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_LONG).show();
                        loadingView.setVisibility(View.GONE);
                    }

                })
                .addOnCanceledListener(this, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
//        loadingView.setVisibility(View.GONE);
    }

    void fbLogin(){
        loadingView.setVisibility(View.VISIBLE);
        LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("email", "public_profile")
        );
    }

    void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                            intent.putExtra("method", "google");
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                        loadingView.setVisibility(View.GONE);
                    }
                })
                .addOnCanceledListener(this, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Login cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
//        loadingView.setVisibility(View.GONE);
    }

    public void signupClicked(View view) {
        if (!checkConstraints())
            return;
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("type", "verify");
        intent.putExtra("userData", user);
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

    public void loginClicked(View view){
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        finish();
    }
}
