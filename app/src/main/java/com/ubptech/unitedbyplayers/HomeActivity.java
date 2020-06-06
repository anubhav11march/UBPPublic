package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Kylodroid on 27-05-2020.
 */
public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore database;
    String phoneNumber, method, name;
    DocumentReference mRef;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
            finish();
            return;
        }

        currentUser = mAuth.getCurrentUser();
        database = FirebaseFirestore.getInstance();
        mRef = database.collection("users").document(currentUser.getUid());
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.getString("phone") != null) {
                phoneNumber = bundle.getString("phone");
                name = bundle.getString("name");
            }
            if(bundle.getString("method") != null)
                method = bundle.getString("method");
        }

        mRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        if(document.get("onboarding").equals("false")){
                            Intent intent = new Intent(HomeActivity.this, OnboardingActivity.class);
                            startActivity(intent);
                        }

                    }
                    else {
                        addToDatabase();
                        Intent intent = new Intent(HomeActivity.this, OnboardingActivity.class);
                        startActivity(intent);
                    }
                }
                else
                    Toast.makeText(HomeActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
            }
        });


//        Toast.makeText(this, currentUser.getUid(), Toast.LENGTH_SHORT).show();

    }

    public void click(View view){
        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void addToDatabase(){
        Map<String, String> user = new HashMap<>();
        user.put("uid", currentUser.getUid());
        user.put("method", method);
        user.put("onboarding", "false");
        if(name!=null){
            user.put("name", name);
            user.put("phone", phoneNumber);
            user.put("email", currentUser.getEmail());
        }
        else {
            user.put("name", currentUser.getDisplayName());
            user.put("email", currentUser.getEmail());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US),
        sdf1 = new SimpleDateFormat("dd/MM/yy, HH:mm", Locale.US);
        Date date = new Date();
        user.put("dateOfCreation", sdf.format(date));
        user.put("lastActive", sdf1.format(date));
        database.collection("users").document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("AAA", "User created");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HomeActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
