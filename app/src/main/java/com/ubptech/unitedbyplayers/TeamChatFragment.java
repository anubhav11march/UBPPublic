package com.ubptech.unitedbyplayers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kylodroid on 06-07-2020.
 */
public class TeamChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ImageView attachIcon, emojiIcon, swiperIcon, swiperDownIcon;
    private EditText messageEdittext;
    private Activity activity;
    private DocumentReference documentReference;
    private FirebaseFirestore database;
    private FirebaseAuth mAuth;
    private MessageCard messageCard;
    private String messageId, currentUserUid, currentProfileCode, currentSport;
    private RelativeLayout matchRequestView;
    private FrameLayout backgroundMatchRequest;
    private View swiperTextContainerView;

    TeamChatFragment(Activity activity, DocumentReference documentReference, FirebaseAuth mAuth,
                 FirebaseFirestore database, MessageCard messageCard, String messagesId,
                     String currentProfileCode, String currentSport){
        this.activity = activity;
        this.documentReference = documentReference;
        this.mAuth = mAuth;
        this.database = database;
        this.messageCard = messageCard;
        this.messageId = messagesId;
        currentUserUid = mAuth.getCurrentUser().getUid();
        this.currentProfileCode = currentProfileCode;
        this.currentSport = currentSport;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_team, container, false);
        initializeUIElements(view);
        return view;
    }

    private void initializeUIElements(View view){
        chatRecyclerView = view.findViewById(R.id.chats_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setHasFixedSize(false);

        swiperIcon = view.findViewById(R.id.swiper_icon);
        attachIcon = view.findViewById(R.id.attach_icon);
        emojiIcon = view.findViewById(R.id.emoji_icon);
        messageEdittext = view.findViewById(R.id.message_edittext);
        matchRequestView = view.findViewById(R.id.request_match_view);
        backgroundMatchRequest = view.findViewById(R.id.background_request);
        swiperTextContainerView = view.findViewById(R.id.swiper_text_container_icon);
        swiperDownIcon = view.findViewById(R.id.swiper_down_icon);

        swiperDownIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    matchRequestView.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    matchRequestView.setVisibility(View.GONE);
                                }
                            });
                    backgroundMatchRequest.animate()
                            .alpha(0.0f)
                            .setDuration(500)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    backgroundMatchRequest.setVisibility(View.GONE);
                                }
                            });
                    backgroundMatchRequest.setVisibility(View.GONE);
                    swiperDownIcon.setVisibility(View.GONE);
                    return true;
                }
                return true;
            }
        });

        swiperTextContainerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.v("AAA", motionEvent.getAction()+"");
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(matchRequestView.getVisibility()!=View.VISIBLE) {
                        matchRequestView.setVisibility(View.VISIBLE);
                        matchRequestView.animate()
                                .alpha(1.0f)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                    }
                                });
                    }
                    backgroundMatchRequest.setVisibility(View.VISIBLE);
                    backgroundMatchRequest.setAlpha(0.5f);
                    swiperDownIcon.setVisibility(View.VISIBLE);
                    return true;
                }
                return true;
            }
        });

        swiperIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.v("AAA", motionEvent.getAction()+"swiper ka");
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(matchRequestView.getVisibility()!=View.VISIBLE) {
                        matchRequestView.setVisibility(View.VISIBLE);
                        matchRequestView.animate()
                                .alpha(1.0f)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                    }
                                });
                    }
                    backgroundMatchRequest.setVisibility(View.VISIBLE);
                    backgroundMatchRequest.setAlpha(0.5f);
                    swiperDownIcon.setVisibility(View.VISIBLE);
                    return true;
                }
                return true;
            }
        });

        ((MessageFragmentInstanceListener) activity).changeMessageIcon(true);
        inflateChats();

        emojiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OpenRequestMatchFragment) activity).openRequestMatch();
            }
        });

        messageEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(messageEdittext.getText().toString().trim().length()==0){
                    attachIcon.setImageDrawable(getResources().getDrawable(R.drawable.attach));
                }
                else {
                    attachIcon.setImageDrawable(getResources().getDrawable(R.drawable.send_icon));

                    attachIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sendMessage(messageEdittext.getText().toString().trim());
                            messageEdittext.setText("");
                            Utils.hideSoftKeyboard(activity);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((TitleChangeListener) activity).updateTitle(messageCard.getName());
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("matches").document(messageId)
                .update("newMessage", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    private void sendMessage(final String messageText){
        Message message = new Message(currentProfileCode, messageCard.getUid(),
                "text", messageText, System.currentTimeMillis());
        database.collection("messages").document(messageId)
                .collection("messages")
                .add(message)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        updateMyTeamMessage(messageText, System.currentTimeMillis());
                        updateOtherTeamMessage(messageText, System.currentTimeMillis());
                    }
                });
    }

    private void updateMyTeamMessage(String message, long timestamp){
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("matches").document(messageId)
                .update("lastMessage", message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
        database.collection("teams").document(currentSport)
                .collection("teams").document(currentProfileCode)
                .collection("matches").document(messageId)
                .update("timestamp", timestamp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("AAA", "Message sent");
                    }
                });
    }

    private void updateOtherTeamMessage(String message, long timestamp){
        database.collection("teams").document(messageCard.getSport())
                .collection("teams").document(messageCard.getUid())
                .collection("matches").document(messageId)
                .update("lastMessage", message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
        database.collection("teams").document(messageCard.getSport())
                .collection("teams").document(messageCard.getUid())
                .collection("matches").document(messageId)
                .update("newMessage", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
        database.collection("teams").document(messageCard.getSport())
                .collection("teams").document(messageCard.getUid())
                .collection("matches").document(messageId)
                .update("timestamp", timestamp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("AAA", "Message sent");
                    }
                });
    }

    private void inflateChats(){
        Log.v("AAA", messageId);
        Query query = database.collection("messages").document(messageId)
                .collection("messages").orderBy("timestamp");
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        FirestoreRecyclerAdapter firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
                holder.setTime(model.getTimestamp());
                if(model.getMessageType().equals("text")){
                    holder.setMessageText(model.getMessageData().toString());
                }
                else if(model.getMessageType().equals("image")){

                }
                if(model.getSenderUid().equals(currentProfileCode)){
                    holder.setBackground(R.color.me_color);
                    holder.setAlignments(true);
                }
                else {
                    holder.setBackground(R.color.other_color);
                    holder.setAlignments(false);
                    holder.setImage(messageCard.getPhoto());
                }
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_chat, parent, false);
                return new MessageViewHolder(view);
            }
        };

        chatRecyclerView.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
        firestoreRecyclerAdapter.notifyDataSetChanged();
        firestoreRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                chatRecyclerView.scrollToPosition(chatRecyclerView.getAdapter().getItemCount() - 1);
                database.collection("teams").document(currentSport)
                        .collection("teams").document(currentProfileCode)
                        .collection("matches").document(messageId)
                        .update("newMessage", false)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
            }
        });
        chatRecyclerView.scrollToPosition(chatRecyclerView.getAdapter().getItemCount() - 1);
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private LinearLayout chatBg, chatContainer;
        private ImageView otherPhoto, chatImage;
        private TextView chatMessage, chatTime;

        MessageViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            chatBg = itemView.findViewById(R.id.chat_bg);
            chatContainer = itemView.findViewById(R.id.chat_container);
            otherPhoto = itemView.findViewById(R.id.other_photo);
            chatImage = itemView.findViewById(R.id.chat_image);
            chatMessage = itemView.findViewById(R.id.chat_message);
            chatTime = itemView.findViewById(R.id.chat_time);
        }

        void setBackground(int color){
            if(color == R.color.me_color) {
                chatBg.setBackground(getResources().getDrawable(R.drawable.me_card));
                otherPhoto.setVisibility(View.GONE);
            }
            else chatBg.setBackground(getResources().getDrawable(R.drawable.other_card));
        }

        void setImage(String url){
            otherPhoto.setVisibility(View.VISIBLE);
            Glide.with(activity).load(url).circleCrop().into(otherPhoto);
        }

        void setMessageText(String message){
            chatMessage.setText(message);
        }

        void setTime(long timestamp){
            long currentTime = System.currentTimeMillis();
            if(currentTime - timestamp < 60000)
                chatTime.setText("now");
            else if(currentTime - timestamp > 86400000){
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy", Locale.US);
                Date date = new Date(timestamp);
                chatTime.setText(sdf.format(date));
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
                Date date = new Date(timestamp);
                chatTime.setText(sdf.format(date));
            }
        }

        void setAlignments(boolean meOrNot){
            if(meOrNot){
                chatContainer.setGravity(Gravity.END);
                chatBg.setGravity(Gravity.END);
                chatContainer.setPadding(150, 0, 0, 0);
            }
            else {
                chatContainer.setGravity(Gravity.START);
                chatBg.setGravity(Gravity.START);
                chatContainer.setPadding(0, 0, 150, 0);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MessageFragmentInstanceListener) activity).changeMessageIcon(false);
    }
}
