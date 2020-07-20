package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kylodroid on 20-07-2020.
 */
public class ScoutFragment extends Fragment implements PlayersListReadyListener, CardStackListener,
AddToFavoritesListener{

    TabLayout sportsTabs;
    TabItem cricket, football, badminton, tennis, basketball;
    CardStackView playersStack;
    Activity activity;
    ProgressBar loader;
    List<PlayerCardDetails> playerCardDetails;
    ImageView rejectButton, acceptButton, rewindButton;
    CardStackLayoutManager cardStackLayoutManager;
    TextView instructionText, noTeamText, haveCode;
    LinearLayout noTeamLayout, cardsLayout;
    DocumentReference documentReference;
    FirebaseFirestore database;
    FirebaseAuth mAuth;
    List<TeamCardDetails> teamCardDetails;
    TeamsStackAdapter teamsStackAdapter;
    boolean isPlayer;
    String currentProfileCode, currentSport;

    ScoutFragment(Activity activity, List<PlayerCardDetails> playerCardDetails, DocumentReference documentReference,
                  FirebaseFirestore database, FirebaseAuth mAuth, boolean isPlayer,
                  String currentProfileCode, String currentSport) {
        this.activity = activity;
        this.playerCardDetails = playerCardDetails;
        this.documentReference = documentReference;
        this.database = database;
        this.mAuth = mAuth;
        this.isPlayer = isPlayer;
        this.currentProfileCode =currentProfileCode;
        this.currentSport = currentSport;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dicover, container, false);
        initializeUIElements(view);
        return view;
    }

    private void initializeUIElements(View view) {
        loader = view.findViewById(R.id.loader);
        sportsTabs = view.findViewById(R.id.sports_tabs);
        cricket = view.findViewById(R.id.cricket);
        playersStack = view.findViewById(R.id.players_stack);
        rejectButton = view.findViewById(R.id.reject_button);
        acceptButton = view.findViewById(R.id.accept_button);
        rewindButton = view.findViewById(R.id.rewind_button);
        instructionText = view.findViewById(R.id.instruction_text);
        noTeamLayout = view.findViewById(R.id.no_team);
        cardsLayout = view.findViewById(R.id.cards_layout);
        noTeamText = view.findViewById(R.id.no_team_text);
        haveCode = view.findViewById(R.id.have_code);

        haveCode.setVisibility(View.GONE);
        sportsTabs.setVisibility(View.GONE);
        instructionText.setText("Scout players near you");

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playersStack.rewind();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                cardStackLayoutManager.setSwipeAnimationSetting(swipeAnimationSetting);
                playersStack.swipe();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                cardStackLayoutManager.setSwipeAnimationSetting(swipeAnimationSetting);
                playersStack.swipe();
            }
        });

    }

    @Override
    public void updatePlayersList(ArrayList<PlayerCardDetails> playerCardDetails) {
        loader.setVisibility(View.GONE);
        playersStack.setVisibility(View.VISIBLE);
        cardStackLayoutManager = new CardStackLayoutManager(activity, this);
        cardStackLayoutManager.setStackFrom(StackFrom.Bottom);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setScaleInterval(0.95f);
        playersStack.setLayoutManager(cardStackLayoutManager);
        playersStack.setAdapter(new PlayersStackAdapter(activity, playerCardDetails));
    }

    int currentPos = -1;

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
//        if (direction == Direction.Right) {
//            checkIfMatch(teamCardDetails.get(currentPos));
//        } else if (direction == Direction.Left) {
//            addResponseToUser("negative", teamCardDetails.get(currentPos));
//        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {
        currentPos = position;
    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    @Override
    public void addToFavorites(TeamCardDetails teamCardDetails) {

    }
}
