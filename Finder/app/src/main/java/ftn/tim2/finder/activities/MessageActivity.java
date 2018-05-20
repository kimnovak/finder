package ftn.tim2.finder.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.CommentAdapter;
import ftn.tim2.finder.adapters.MessageListAdapter;
import ftn.tim2.finder.model.Comment;
import ftn.tim2.finder.model.Conversation;
import ftn.tim2.finder.model.Message;
import ftn.tim2.finder.model.User;

public class MessageActivity extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private Button mMessageSendBtn;
    private EditText mMessageText;

    private User me;
    private User receiver;

    private String receiverId;
    private String receiverName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;
    private DatabaseReference databaseMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);

        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        mMessageRecycler.setLayoutManager(myLayoutManager);
        mMessageRecycler.setItemAnimator(new DefaultItemAnimator());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        databaseMessages = FirebaseDatabase.getInstance().getReference("messages");

        prepareData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiverName(receiverId);

        messagesBetweenMeAndUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messagesBetweenMeAndUser();
    }

    private void prepareData(){
        //get receiverId from intent
        receiverId = getIntent().getStringExtra("USER_ID");

        mMessageSendBtn = findViewById(R.id.button_chatbox_send);
        mMessageText = findViewById(R.id.edittext_chatbox);

        mMessageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageText.getText().toString().trim();
                String senderId = firebaseAuth.getCurrentUser().getUid();

                if (!message.isEmpty()) {
                    sendMessage(message, senderId, receiverId);
                }
            }
        });
    }

    private void sendMessage(String message, String senderId, String receiverId) {
        Message newMessage = new Message(message, senderId, receiverId, new Date());

        databaseMessages.push().setValue(newMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MessageActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                } else {
                    mMessageText.setText(null);
                    createReceiverConversation();
                    //hideSoftKeyboard()
                }
            }
        });
    }

    private void createReceiverConversation() {
        boolean updateConv = false;
        final Conversation conversation = new Conversation(me);

        if (receiver.getUserProfile().getConversations() == null) {
            receiver.getUserProfile().setConversations(new HashMap<String, Conversation>() {{
                put(databaseUsers.push().getKey(), conversation);
            }});
            updateConv = true;
        } else {
            for (Conversation c : receiver.getUserProfile().getConversations().values()) {
                if (!c.getParticipant().getId().equals(me.getId())) {
                    receiver.getUserProfile().getConversations()
                            .put(databaseUsers.push().getKey(), conversation);
                    updateConv = true;
                    break;
                }
            }
        }

        if (updateConv) {
            databaseUsers.child(receiver.getId()).child("userProfile").child("conversations")
                    .setValue(receiver.getUserProfile().getConversations());
        }
    }

    private void messagesBetweenMeAndUser() {
        databaseMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    Message message = snap.getValue(Message.class);
                    if (message.getSenderId().equals(firebaseAuth.getCurrentUser().getUid()) && message.getReceiverId().equals(receiverId) ||
                            message.getSenderId().equals(receiverId) && message.getReceiverId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        messageList.add(message);
                    }
                }

                /**populate messages**/
                populateMessagesRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void receiverName(final String receiverId) {
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User receiverObj = dataSnapshot.child(receiverId).getValue(User.class);

                receiver = receiverObj;
                receiverName = receiverObj.getFirstName() + " " + receiverObj.getLastName();

                // Get my object
                User myObj = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
                me = myObj;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void populateMessagesRecyclerView() {

        Collections.sort(messageList, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });

        mMessageAdapter = new MessageListAdapter(messageList, receiverName);
        mMessageRecycler.setAdapter(mMessageAdapter);
    }


}
