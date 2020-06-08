package com.ubptech.unitedbyplayers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class JoinTeamActivity extends AppCompatActivity {

    FirebaseFirestore database;
    DocumentReference mRef;
    TextView name;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);
        getSupportActionBar().hide();
        name = findViewById(R.id.name);
        photo = findViewById(R.id.photo);

        database = FirebaseFirestore.getInstance();
        mRef = database.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            name.setText(document.get("name").toString());
                            HashMap<String, String> pic = (HashMap<String, String>)document.get("pictures");
                            Picasso.get().load(pic.get("0")).into(photo);
                        }
                    }
                });
    }
}
