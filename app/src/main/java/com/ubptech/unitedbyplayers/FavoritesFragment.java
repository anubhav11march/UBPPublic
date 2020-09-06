package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by Kylodroid on 03-09-2020.
 */
public class FavoritesFragment extends Fragment{

    Activity activity;
    RecyclerView cardsRecyclerView;
    FirebaseFirestore database;
    DocumentReference documentReference;
    FirebaseAuth mAuth;
    boolean isPlayer;
    String currentProfileCode, currentSport;

    FavoritesFragment(Activity activity, FirebaseFirestore database,
                      DocumentReference documentReference, FirebaseAuth mAuth, boolean isPlayer,
                      String currentProfileCode, String currentSport){
        this.activity = activity;
        this.database = database;
        this.documentReference = documentReference;
        this.mAuth = mAuth;
        this.isPlayer = isPlayer;
        this.currentProfileCode = currentProfileCode;
        this.currentSport = currentSport;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        initializeViews(view);
//        if(isPlayer)
//            inflateRecyclerView();
//        else inflateRecyclerview();
        return view;

    }

    void initializeViews(View view){
        cardsRecyclerView = view.findViewById(R.id.recycler_view);
        ((TitleChangeListener)activity).updateTitle("Saved");
    }
}
