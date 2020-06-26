package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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
 * Created by Kylodroid on 21-06-2020.
 */
public class DiscoverFragment extends Fragment implements PlayersListReadyListener, CardStackListener {

    TabLayout sportsTabs;
    TabItem cricket, football, badminton, tennis, basketball;
    CardStackView playersStack;
    Activity activity;
    ProgressBar loader;
    List<PlayerCardDetails> playerCardDetails;
    ImageView rejectButton, acceptButton, rewindButton;
    CardStackLayoutManager cardStackLayoutManager;

    DiscoverFragment(Activity activity, List<PlayerCardDetails> playerCardDetails){
        this.activity = activity;
        this.playerCardDetails = playerCardDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dicover, container, false);
        initializeUIElements(view);
        return view;
    }

    private void initializeUIElements(View view){
        loader = view.findViewById(R.id.loader);
        sportsTabs = view.findViewById(R.id.sports_tabs);
        cricket = view.findViewById(R.id.cricket);
        playersStack = view.findViewById(R.id.players_stack);
        rejectButton = view.findViewById(R.id.reject_button);
        acceptButton = view.findViewById(R.id.accept_button);
        rewindButton = view.findViewById(R.id.rewind_button);


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

        sportsTabs.selectTab(sportsTabs.getTabAt(1), true);

        if(playerCardDetails.size()>0){
            updatePlayersList((ArrayList<PlayerCardDetails>) playerCardDetails);
        }
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
        playersStack.setAdapter(new TeamStackAdapter(activity, playerCardDetails));
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
