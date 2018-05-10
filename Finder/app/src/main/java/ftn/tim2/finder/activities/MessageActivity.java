package ftn.tim2.finder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.CommentAdapter;
import ftn.tim2.finder.adapters.MessageListAdapter;
import ftn.tim2.finder.model.Message;
import ftn.tim2.finder.model.User;

public class MessageActivity extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(messageList);
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        mMessageRecycler.setLayoutManager(myLayoutManager);
        mMessageRecycler.setItemAnimator(new DefaultItemAnimator());
        mMessageRecycler.setAdapter(mMessageAdapter);

        prepareData();
    }

    private void prepareData(){
        User user = new User("1", "username", "email", "password", "firstName", "lastName");
        Message message = new Message("First", user, user, new Date());
        messageList.add(message);

        message = new Message("second", user, user, new Date());
        messageList.add(message);

        message = new Message("third", user, user, new Date());
        messageList.add(message);

        message = new Message("helllooo", user, user, new Date());
        messageList.add(message);

        message = new Message("hey", user, user, new Date());
        messageList.add(message);

        mMessageAdapter.notifyDataSetChanged();
    }
}
