package ftn.tim2.finder.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.ConversationViewAdapter;
import ftn.tim2.finder.model.Conversation;
import ftn.tim2.finder.model.User;

public class ConversationFragment extends Fragment {

    private View v;

    private RecyclerView recyclerView;
    private ConversationViewAdapter conversationViewAdapter;
    private List<Conversation> conversations;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;

    private static final String TAG = "ConversationFragment";

    public ConversationFragment() {
        conversations = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_conversations, container, false);

        recyclerView = v.findViewById(R.id.conversation_recyclerview);
        conversationViewAdapter = new ConversationViewAdapter(getContext(), conversations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(conversationViewAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        prepareData();

        return v;
    }

    private void prepareData() {
        databaseUsers.child(firebaseAuth.getCurrentUser().getUid())
                .child("userProfile")
                .child("conversations")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot convSnapshot: dataSnapshot.getChildren()) {
                            Conversation conversation = convSnapshot.getValue(Conversation.class);

                            conversations.add(conversation);
                        }

                        conversationViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "The read failed: " + databaseError.getCode());
                    }
                });

    }
}
