package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.content.Intent;
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
 * Created by Kylodroid on 21-06-2020.
 */
public class DiscoverFragment extends Fragment implements PlayersListReadyListener,
        CardStackListener, TeamsListReadyListener, NoTeamAvailableInGivenRadiusListener,
        AddToFavoritesListener, TeamViewEnabledListener, IsPlayerOrNotListener {

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

    DiscoverFragment(Activity activity, List<PlayerCardDetails> playerCardDetails, DocumentReference documentReference,
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

        if(!isPlayer)
            sportsTabs.setVisibility(View.GONE);

        haveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity.getApplicationContext(), JoinTeamActivity.class));
            }
        });

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

        sportsTabs.selectTab(sportsTabs.getTabAt(2), true);

        sportsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                String sport = "football";
                switch (index) {
                    case 0:
                        sport = "basketball";
                        break;
                    case 1:
                        sport = "football";
                        break;
                    case 2:
                        sport = "cricket";
                        break;
                    case 3:
                        sport = "badminton";
                        break;
                    case 4:
                        sport = "tennis";
                        break;
                }
                loader.setVisibility(View.VISIBLE);

                if (index == 0 || index == 4) {
                    comingSoon("Sport coming soon!");
                    return;
                }
                cardsLayout.setVisibility(View.VISIBLE);
                noTeamLayout.setVisibility(View.GONE);
                if (playersStack.getVisibility() == View.VISIBLE)
                    playersStack.setVisibility(View.GONE);


                ((SportChangeListener) activity).updateSport(sport);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (playerCardDetails.size() > 0) {
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
        playersStack.setAdapter(new PlayersStackAdapter(activity, playerCardDetails));
    }

    int currentPos = -1;

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if(!isPlayer){
            if (direction == Direction.Right) {
                checkIfMatchForTeam(teamCardDetails.get(currentPos));
            } else if (direction == Direction.Left) {
                addResponseToUserForTeams("negative", teamCardDetails.get(currentPos));
            }
            return;
        }
        if (direction == Direction.Right) {
            checkIfMatch(teamCardDetails.get(currentPos));
        } else if (direction == Direction.Left) {
            addResponseToUser("negative", teamCardDetails.get(currentPos));
        }
        Log.v("AAA", teamCardDetails.get(currentPos).getName());
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
        Log.v("AAA", "upar waala card: " + teamCardDetails.get(position).getFullCode());
    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    @Override
    public void updateTeamsList(ArrayList<TeamCardDetails> teamCardDetails) {
        this.teamCardDetails = teamCardDetails;
        loader.setVisibility(View.GONE);
        instructionText.setText("Discover and join teams near you!");
        playersStack.setVisibility(View.VISIBLE);
        cardStackLayoutManager = new CardStackLayoutManager(activity, this);
        cardStackLayoutManager.setStackFrom(StackFrom.Bottom);
        cardStackLayoutManager.setVisibleCount(3);
        cardStackLayoutManager.setScaleInterval(0.95f);
        playersStack.setLayoutManager(cardStackLayoutManager);
        teamsStackAdapter = new TeamsStackAdapter(activity, teamCardDetails, this);
        playersStack.setAdapter(teamsStackAdapter);
    }

    @Override
    public void noTeamUpdate(String message) {
        if (sportsTabs.getSelectedTabPosition() != 0 && sportsTabs.getSelectedTabPosition() != 4) {
            loader.setVisibility(View.GONE);
            cardsLayout.setVisibility(View.GONE);
            noTeamLayout.setVisibility(View.VISIBLE);
            noTeamText.setText(message);
        }
    }

    private void comingSoon(String message) {
        loader.setVisibility(View.GONE);
        cardsLayout.setVisibility(View.GONE);
        noTeamLayout.setVisibility(View.VISIBLE);
        noTeamText.setText(message);
    }

    private void checkIfMatch(final TeamCardDetails teamCardDetails) {
        documentReference.collection("likesMe")
                .document(teamCardDetails.getFullCode())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                addToMatchedTeams(teamCardDetails);
                            } else {
                                addResponseToUser("positive", teamCardDetails);
                                addRequestToTeam(teamCardDetails);
                            }
                        }
                    }
                });
    }

    private void checkIfMatchForTeam(final TeamCardDetails teamCardDetails) {
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("teamsThatLikeMe")
                .document(teamCardDetails.getFullCode())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                addToMatchedTeamsForTeams(teamCardDetails);
                            } else {
                                addResponseToUserForTeams("positive", teamCardDetails);
                                addRequestToTeamForTeams(teamCardDetails);
                            }
                        }
                    }
                });
    }

    private void addResponseToUser(String response, TeamCardDetails teamCardDetails) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", response);
        map.put("fullCode", teamCardDetails.getFullCode());
        map.put("sport", teamCardDetails.getSport());
        documentReference.collection("teamsResponse")
                .document(teamCardDetails.getFullCode())
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

    private void addResponseToUserForTeams(String response, TeamCardDetails teamCardDetails) {
        Log.v("AAA", currentSport + " " + currentProfileCode);
        HashMap<String, String> map = new HashMap<>();
        map.put("response", response);
        map.put("fullCode", teamCardDetails.getFullCode());
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("teamsResponse")
                .document(teamCardDetails.getFullCode())
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

    private void addRequestToTeam(TeamCardDetails teamCardDetails) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        database.collection("teams").document(teamCardDetails.getSport())
                .collection("teams").document(teamCardDetails.getFullCode())
                .collection("requests").document(mAuth.getCurrentUser().getUid())
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

    private void addRequestToTeamForTeams(TeamCardDetails teamCardDetails) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        database.collection("teams").document(teamCardDetails.getSport())
                .collection("teams").document(teamCardDetails.getFullCode())
                .collection("teamsThatLikeMe").document(currentProfileCode)
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

    @Override
    public void addToFavorites(TeamCardDetails teamCardDetails) {
        if(!isPlayer){
            SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            cardStackLayoutManager.setSwipeAnimationSetting(swipeAnimationSetting);
            playersStack.swipe();
            HashMap<String, Object> map = new HashMap<>();
            map.put("timestamp", System.currentTimeMillis());
            map.put("fullCode", teamCardDetails.getFullCode());
            database.collection("teams").document(currentSport)
                    .collection("teams").document(currentProfileCode)
                    .collection("teamsSaved")
                    .document(teamCardDetails.getFullCode())
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
            return;
        }
        SwipeAnimationSetting swipeAnimationSetting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        cardStackLayoutManager.setSwipeAnimationSetting(swipeAnimationSetting);
        playersStack.swipe();
        HashMap<String, Object> map = new HashMap<>();
        map.put("timestamp", System.currentTimeMillis());
        map.put("fullCode", teamCardDetails.getFullCode());
        map.put("sport", teamCardDetails.getSport());
        documentReference.collection("teamsSaved")
                .document(teamCardDetails.getFullCode())
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

    private void addToMatchedTeams(final TeamCardDetails teamCardDetails){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("timestamp", System.currentTimeMillis());
//        map.put("lastMessage", null);
//        map.put("name", teamCardDetails.getName());
//        map.put("photo", teamCardDetails.getPhotos().get("0"));
//        map.put("uid", teamCardDetails.getFullCode());
//        map.put("sport", teamCardDetails.getSport());
//        map.put("newMessage", false);
        final MessageCard messageCard = new MessageCard(System.currentTimeMillis(), null, teamCardDetails.getName(),
                teamCardDetails.getPhotos().get("0"), teamCardDetails.getFullCode(),
                teamCardDetails.getSport(), true);
        documentReference.collection("matches")
                .add(messageCard)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(activity.getApplicationContext(), "You have matched with " +
                                teamCardDetails.getName(), Toast.LENGTH_SHORT).show();
                        database.collection("teams").document(teamCardDetails.getSport())
                                .collection("teams").document(teamCardDetails.getFullCode())
                                .collection("matches").document(documentReference.getId())
                                .set(messageCard)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                    }
                });
    }

    private void addToMatchedTeamsForTeams(final TeamCardDetails teamCardDetails){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("timestamp", System.currentTimeMillis());
//        map.put("lastMessage", null);
//        map.put("name", teamCardDetails.getName());
//        map.put("photo", teamCardDetails.getPhotos().get("0"));
//        map.put("uid", teamCardDetails.getFullCode());
//        map.put("sport", teamCardDetails.getSport());
//        map.put("newMessage", false);
        final MessageCard messageCard = new MessageCard(System.currentTimeMillis(), null, teamCardDetails.getName(),
                teamCardDetails.getPhotos().get("0"), teamCardDetails.getFullCode(),
                teamCardDetails.getSport(), true);
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("matches")
                .add(messageCard)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(activity.getApplicationContext(), "You have matched with " +
                                teamCardDetails.getName(), Toast.LENGTH_SHORT).show();
                        database.collection("teams").document(teamCardDetails.getSport())
                                .collection("teams").document(teamCardDetails.getFullCode())
                                .collection("matches").document(documentReference.getId())
                                .set(messageCard)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                    }
                });
    }

    @Override
    public void hideSportsTabBar() {
        if(sportsTabs!=null)
            sportsTabs.setVisibility(View.GONE);
    }

    @Override
    public void updateIsPlayer(boolean isPlayer, String currentProfileCode, String currentSport) {
        this.isPlayer = isPlayer;
        this.currentProfileCode = currentProfileCode;
        this.currentSport = currentSport;
        Log.v("AAA", "Current profile " + currentProfileCode);
    }
}
