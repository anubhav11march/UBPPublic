package com.ubptech.unitedbyplayers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kylodroid on 03-07-2020.
 */
class TeamsStackAdapter extends RecyclerView.Adapter<TeamsStackAdapter.ViewHolder> {

    List<TeamCardDetails> teamCardDetails;
    Context context;
    static int picNo = 0;
    ImageView favButton;
    Fragment parentFragment;

    TeamsStackAdapter(Context context, List<TeamCardDetails> teamCardDetails, Fragment parentFragment){
        this.teamCardDetails = teamCardDetails;
        this.context = context;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public TeamsStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_player, parent, false);
        return new TeamsStackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamsStackAdapter.ViewHolder holder, final int position) {
        holder.setMatchesStats(teamCardDetails.get(position).getTotalMatches());
        holder.setDistanceFriendly(teamCardDetails.get(position).getDistance()
                + ", " + teamCardDetails.get(position).getFriendly());
        holder.setTeamName(teamCardDetails.get(position).getName());
        holder.setImageView(teamCardDetails.get(position).getPhotos());
        favButton = holder.itemView.findViewById(R.id.fav_button);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Added team to favorites", Toast.LENGTH_SHORT).show();
                ((AddToFavoritesListener) parentFragment).addToFavorites(teamCardDetails.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamCardDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView, favButton;
        private TextView matchesStats, distanceFriendly, teamName;
        private View p1, p2, p3, p4;

        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
            matchesStats = view.findViewById(R.id.matches_stats);
            distanceFriendly = view.findViewById(R.id.distance_gender);
            teamName = view.findViewById(R.id.player_name);
            favButton = view.findViewById(R.id.fav_button);
            p1 = view.findViewById(R.id.p1);
            p2 = view.findViewById(R.id.p2);
            p3 = view.findViewById(R.id.p3);
            p4 = view.findViewById(R.id.p4);
        }

        void setImageView(final HashMap<String, String> photos){
            setPageNos(photos.size());
            picNo = 0;
            Glide.with(context).load(photos.get((picNo)+"")).centerCrop().into(imageView);
            picNo++;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Clicked" + picNo + "/" + photos.size(), Toast.LENGTH_LONG).show();
                    Glide.with(context).load(photos.get(picNo%photos.size()+"")).centerCrop().into(imageView);
                    picNo++;
                }
            });
        }

        void setPageNos(int nos){
            switch (nos){
                case 1: p2.setVisibility(View.GONE);
                    p3.setVisibility(View.GONE);
                    p4.setVisibility(View.GONE);
                    break;
                case 2:
                    p3.setVisibility(View.GONE);
                    p4.setVisibility(View.GONE);
                case 3:
                    p4.setVisibility(View.GONE);
            }
        }

        void setMatchesStats(String matchesStats){
            this.matchesStats.setText(matchesStats);
        }

        void setDistanceFriendly(String distanceGender){
            this.distanceFriendly.setText(distanceGender);
        }

        void setTeamName(String teamName){
            this.teamName.setText(teamName);
        }
    }
}
