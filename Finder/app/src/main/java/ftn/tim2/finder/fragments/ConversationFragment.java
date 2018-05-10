package ftn.tim2.finder.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.ConversationViewAdapter;
import ftn.tim2.finder.model.Conversation;
import ftn.tim2.finder.model.User;

public class ConversationFragment extends Fragment {

    private View v;

    private RecyclerView recyclerView;
    private ConversationViewAdapter conversationViewAdapter;
    private List<Conversation> conversations;

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

        initData();

        recyclerView = v.findViewById(R.id.conversation_recyclerview);
        conversationViewAdapter = new ConversationViewAdapter(getContext(), conversations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(conversationViewAdapter);

        return v;
    }

    private void initData() {
        User sender = new User("1","Username", "email@example.com", "password", "First", "Last" );

        User receiver = new User("1","Receiver1", "email1@example.com", "password", "First", "Last" );
        User receiver1 = new User("1","Receiver2", "email2@example.com", "password", "First1", "Last1" );
        User receiver2 = new User("1","Receiver3", "email3@example.com", "password", "First2", "Last2" );

        conversations.add(new Conversation(sender, receiver, null));
        conversations.add(new Conversation(sender, receiver1, null));
        conversations.add(new Conversation(sender, receiver2, null));
    }
}
