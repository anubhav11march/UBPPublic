package com.ubptech.unitedbyplayers;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.yuyakaido.android.cardstackview.CardStackView;

/**
 * Created by Kylodroid on 23-06-2020.
 */
class TeamStackAdapter extends RecyclerView.Adapter<TeamStackAdapter.ViewHolder> {

    @NonNull
    @Override
    public TeamStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamStackAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View view){
            super(view);

        }
    }
}
