package com.ubptech.unitedbyplayers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by Kylodroid on 06-09-2020.
 */
class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{

    List<TeamCardDetails> teamCardDetails;
    Context context;
    Fragment parentFragment;

    FavoritesAdapter(Context context, List<TeamCardDetails> teamCardDetails, Fragment parentFragment){
        this.teamCardDetails = teamCardDetails;
        this.context = context;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_saved, parent, false);
        return new FavoritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return teamCardDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        ViewHolder(View view){
            super(view);
            itemView = view;
        }


    }
}
