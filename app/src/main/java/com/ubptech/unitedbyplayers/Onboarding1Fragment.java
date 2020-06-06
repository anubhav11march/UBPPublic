package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import static android.app.Activity.RESULT_OK;

/**
 * Created by Kylodroid on 06-06-2020.
 */
public class Onboarding1Fragment extends Fragment{

    Activity activity;
    boolean check = false;

    Onboarding1Fragment(Activity activity){
        this.activity = activity;
    }

    private static final int GALLERY_REQUEST = 2;
    LinearLayout pic1, pic2, pic3, pic4;
    ImageView image1, image2, image3, image4, img1, img2, img3, img4, currImage, currToRemove;
    Uri uri = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_1, container, false);
        initiate(view);
        return view;
    }

    void initiate(View view){
        pic1 = view.findViewById(R.id.pic1);
        pic2 = view.findViewById(R.id.pic2);
        pic3 = view.findViewById(R.id.pic3);
        pic4 = view.findViewById(R.id.pic4);
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);
        image4 = view.findViewById(R.id.image4);
        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonClicked(image1, img1);
            }
        });

        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonClicked(image2, img2);
            }
        });

        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonClicked(image3, img3);
            }
        });

        pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButtonClicked(image4, img4);
            }
        });
    }

    private void imageButtonClicked(ImageView image, ImageView img){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST);
        currImage = img;
        currToRemove = image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            currToRemove.setVisibility(View.GONE);
            currImage.setImageURI(uri);
            currImage.setVisibility(View.VISIBLE);
            ((ViewPageChange)activity).check(true);
            ((ViewPageChange)activity).addPictureUri(uri);
        }
    }
}
