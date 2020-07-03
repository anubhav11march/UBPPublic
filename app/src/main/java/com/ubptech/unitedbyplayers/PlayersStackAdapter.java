package com.ubptech.unitedbyplayers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kylodroid on 23-06-2020.
 */
class PlayersStackAdapter extends RecyclerView.Adapter<PlayersStackAdapter.ViewHolder> {

    List<PlayerCardDetails> playerCardDetails;
    Context context;
    static int picNo = 0;

    PlayersStackAdapter(Context context, List<PlayerCardDetails> playerCardDetails){
        this.playerCardDetails = playerCardDetails;
        this.context = context;
    }



    @NonNull
    @Override
    public PlayersStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersStackAdapter.ViewHolder holder, int position) {
        holder.setMatchesStats(playerCardDetails.get(position).getTotalMatches());
        holder.setDistanceGender(playerCardDetails.get(position).getDistance()
                + ", " + playerCardDetails.get(position).getGender());
        holder.setPlayerName(playerCardDetails.get(position).getName());
        holder.setImageView(playerCardDetails.get(position).getPhotos());

    }

    @Override
    public int getItemCount() {
        return playerCardDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView matchesStats, distanceGender, playerName;

        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
            matchesStats = view.findViewById(R.id.matches_stats);
            distanceGender = view.findViewById(R.id.distance_gender);
            playerName = view.findViewById(R.id.player_name);
        }

        void setImageView(final HashMap<String, String> photos){
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

        void setMatchesStats(String matchesStats){
            this.matchesStats.setText(matchesStats);
        }

        void setDistanceGender(String distanceGender){
            this.distanceGender.setText(distanceGender);
        }

        void setPlayerName(String playerName){
            this.playerName.setText(playerName);
        }
    }
}
