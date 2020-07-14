package com.ubptech.unitedbyplayers;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kylodroid on 29-06-2020.
 */
public class MessagesFragment extends Fragment {

    Activity activity;
    RecyclerView messagesRecyclerView;
    FirebaseFirestore database;
    DocumentReference documentReference;
    FirebaseAuth mAuth;
    EditText searchMessages;
    boolean isPlayer;
    String currentProfileCode, currentSport;

    MessagesFragment(Activity activity, FirebaseFirestore database,
                     DocumentReference documentReference, FirebaseAuth mAuth, boolean isPlayer,
                     String currentProfileCode, String currentSport){
        this.activity = activity;
        this.database = database;
        this.documentReference = documentReference;
        this.mAuth = mAuth;
        this.isPlayer = isPlayer;
        this.currentProfileCode = currentProfileCode;
        this.currentSport = currentSport;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        initializeView(view);
        inflateMessagesRecyclerView();
        return view;
    }

    private void initializeView(View view){
        messagesRecyclerView = view.findViewById(R.id.messages_recycler_view);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        searchMessages = view.findViewById(R.id.search_messages);
        searchMessages.setHint("\uD83D\uDD0D Search Messages");
    }

    private void inflateMessagesRecyclerView(){
        Query query = documentReference.collection("matches").orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MessageCard> options = new FirestoreRecyclerOptions.Builder<MessageCard>()
                .setQuery(query, MessageCard.class)
                .build();
        FirestoreRecyclerAdapter firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<MessageCard, MessageCardViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageCardViewHolder holder, int position, @NonNull final MessageCard model) {
                final String messageId =getSnapshots().getSnapshot(position).getId();
                Log.v("AAA", model.toString());
                holder.setImage(model.getPhoto());
                if(model.getLastMessage()!=null)
                    holder.setMessage(model.getLastMessage());
                holder.setNewMessage(model.isNewMessage());
                holder.setName(model.getName());
                holder.setTime(model.getTimestamp());
                LinearLayout messageCard = holder.mView.findViewById(R.id.message_card);
                messageCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isPlayer)
                        ((ChangeFragmentListener) activity).changeFragment(new ChatFragment(activity,
                                documentReference, mAuth, database, model, messageId));
                        else {
                            //match request waala chat fragment
                        }
                    }
                });

            }

            @NonNull
            @Override
            public MessageCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false);
                return new MessageCardViewHolder(view);
            }
        };
        messagesRecyclerView.setAdapter(firestoreRecyclerAdapter);
        firestoreRecyclerAdapter.startListening();
    }

    private class MessageCardViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView time, lastMessage, name;
        private LinearLayout isNew;
        private ImageView messagePhoto;

        public MessageCardViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            this.messagePhoto = itemView.findViewById(R.id.message_dp);
            this.isNew = itemView.findViewById(R.id.is_new);
            this.time = itemView.findViewById(R.id.time);
            this.name = itemView.findViewById(R.id.name);
            this.lastMessage = itemView.findViewById(R.id.last_message);
        }

        void setImage(String url){
            Glide.with(mView).load(url).centerCrop().circleCrop().into(messagePhoto);
        }

        void setTime(long timestamp){
            long currentTime = System.currentTimeMillis();
            if(currentTime - timestamp > 86400000){
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy", Locale.US);
                Date date = new Date(timestamp);
                time.setText(sdf.format(date));
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
                Date date = new Date(timestamp);
                time.setText(sdf.format(date));
            }
        }

        void setMessage(String message){
            lastMessage.setText(message);
            lastMessage.setTypeface(lastMessage.getTypeface(), Typeface.NORMAL);
        }

        void setName(String name){
            this.name.setText(name);
        }

        void setNewMessage(boolean isNew){
            if(isNew)
                this.isNew.setVisibility(View.VISIBLE);
            else this.isNew.setVisibility(View.INVISIBLE);
        }
    }
}
