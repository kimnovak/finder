package ftn.tim2.finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ftn.tim2.finder.MainActivity;
import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.ConversationViewAdapter;
import ftn.tim2.finder.model.Conversation;
import ftn.tim2.finder.model.User;

public class ConversationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConversationViewAdapter conversationViewAdapter;
    private List<Conversation> conversations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversations);

        initData();

        recyclerView = findViewById(R.id.conversation_recyclerview);
        conversationViewAdapter = new ConversationViewAdapter(this, conversations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(conversationViewAdapter);
    }

    private void initData() {
        conversations = new ArrayList<>();

        User sender = new User("Username", "email@example.com", "password", "First", "Last" );

        User receiver = new User("Receiver1", "email1@example.com", "password", "First", "Last" );
        User receiver1 = new User("Receiver2", "email2@example.com", "password", "First1", "Last1" );
        User receiver2 = new User("Receiver3", "email3@example.com", "password", "First2", "Last2" );

        conversations.add(new Conversation(sender, receiver, null));
        conversations.add(new Conversation(sender, receiver1, null));
        conversations.add(new Conversation(sender, receiver2, null));
    }

    public void home(View v){
        Intent intent = new Intent(ConversationActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void people(View v){
        Intent intent = new Intent(ConversationActivity.this, ViewAllUsersActivity.class);
        startActivity(intent);
    }

    public void messages(View v){
        Intent intent = new Intent(ConversationActivity.this, ConversationActivity.class);
        startActivity(intent);
    }

    public void settings(View v){
        Intent intent = new Intent(ConversationActivity.this, FinderPreferenceActivity.class);
        startActivity(intent);
    }

    public void account(View v){
        Intent intent = new Intent(ConversationActivity.this, ProfileDetailsActivity.class);
        startActivity(intent);
    }
}
