package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by Kylodroid on 20-06-2020.
 */
class ProfilesAdapter extends ArrayAdapter<String> {

    Context context;
    LayoutInflater inflater;
    int textViewId;
    View profileView;

    public ProfilesAdapter(Activity context, int resourceId, int textViewId, List<String> profiles){
        super(context, resourceId, textViewId, profiles);
        inflater = context.getLayoutInflater();
        this.textViewId = textViewId;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null)
            profileView = inflater.inflate(R.layout.item_dropdown_simple, null, true);
        return profileView;
    }

}
