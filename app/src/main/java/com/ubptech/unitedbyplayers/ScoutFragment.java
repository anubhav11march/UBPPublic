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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kylodroid on 20-07-2020.
 */
public class ScoutFragment extends Fragment implements PlayersListReadyListener, CardStackListener,
AddUserToFavoritesListener{

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
        this.playerCardDetails = playerCardDetails;
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
        if (direction == Direction.Right) {
            checkIfMatch(playerCardDetails.get(currentPos));
        } else if (direction == Direction.Left) {
            addResponseToTeam("negative", playerCardDetails.get(currentPos));
        }
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

    private void checkIfMatch(final PlayerCardDetails playerCardDetails) {
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("requests")
                .document(playerCardDetails.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                addToMatchedUsers(playerCardDetails);
                            } else {
                                addResponseToTeam("positive", playerCardDetails);
                                addRequestToUser(playerCardDetails);
                            }
                        }
                    }
                });
    }

    private void addResponseToTeam(String response, PlayerCardDetails playerCardDetails) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", response);
        map.put("fullCode", playerCardDetails.getUid());
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("usersResponse")
                .document(playerCardDetails.getUid())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity.getApplicationContext(),
                                "An error occurred, please try again later", Toast.LENGTH_LONG).show();
                        playersStack.rewind();
                    }
                });

    }

    private void addRequestToUser(PlayerCardDetails playerCardDetails) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        map.put("fullCode", currentProfileCode);
        map.put("sport", currentSport);
        database.collection("users").document(playerCardDetails.getUid())
                .collection("requests").document(currentProfileCode)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity.getApplicationContext(),
                                "An error occurred, please try again later", Toast.LENGTH_LONG).show();
                        playersStack.rewind();
                    }
                });
    }

    private void addToMatchedUsers(final PlayerCardDetails playerCardDetails){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("timestamp", System.currentTimeMillis());
//        map.put("lastMessage", null);
//        map.put("name", teamCardDetails.getName());
//        map.put("photo", teamCardDetails.getPhotos().get("0"));
//        map.put("uid", teamCardDetails.getFullCode());
//        map.put("sport", teamCardDetails.getSport());
//        map.put("newMessage", false);
        MessageCard messageCard1 = new MessageCard(System.currentTimeMillis(), null, playerCardDetails.getName(),
                playerCardDetails.getPhotos().get("0"), playerCardDetails.getUid(),
                null, true);
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("matches")
                .add(messageCard1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        final String messageId = documentReference.getId();
                        Toast.makeText(activity.getApplicationContext(), "You have matched with " +
                                playerCardDetails.getName(), Toast.LENGTH_SHORT).show();
                        database.collection("teams").document(currentSport)
                                .collection("teams").document(currentProfileCode)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            HashMap<String, String> map = (HashMap<String, String>) documentSnapshot.get("pictures");
                                            MessageCard messageCard2 = new MessageCard(System.currentTimeMillis(),
                                                    null, documentSnapshot.get("name").toString(),
                                                    map.get("0"), currentProfileCode,
                                                    currentSport, true);
                                            database.collection("users").document(playerCardDetails.getUid())
                                                    .collection("matches").document(messageId)
                                                    .set(messageCard2)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }

    @Override
    public void addToFavorites(PlayerCardDetails playerCardDetails) {
        SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        cardStackLayoutManager.setSwipeAnimationSetting(swipeAnimationSetting);
        playersStack.swipe();
        HashMap<String, Object> map = new HashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        map.put("fullCode", playerCardDetails.getUid());
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("usersSaved")
                .document(playerCardDetails.getUid())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity.getApplicationContext(),
                                "An error occurred, please try again later", Toast.LENGTH_LONG).show();
                        playersStack.rewind();
                    }
                });
    }
}
