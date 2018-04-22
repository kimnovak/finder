package ftn.tim2.finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.UsersRecyclerViewAdapter;
import ftn.tim2.finder.model.User;

public class ViewAllUsersActivity extends AppCompatActivity {
    private static final String TAG = "ViewAllUsersActivity";
    private ArrayList<User> mUsers = new ArrayList<>();
    private final int NUMBER_OF_INITIAL_USERS = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_users);
        Log.d(TAG, "view all users activity - oncreate method");
        addInitialData();
        initRecyclerView();
    }

    private void addInitialData() {
        for(int i = 0; i <= NUMBER_OF_INITIAL_USERS; i++) {
            User user = new User("Username" + i, "email" + i + "@example.com", "password", "First" + i, "Last" + i );
            mUsers.add(user);
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView");
        RecyclerView recyclerView = findViewById(R.id.recyclerusers_view);
        final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mUsers,this);
        Button viewFriendsBtn = findViewById(R.id.view_friends_button);
        viewFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> friendList = getFriends();
                adapter.setmUsers(friendList);
                adapter.notifyDataSetChanged();
            }
        });
        Button viewAllUsersBtn = findViewById(R.id.view_all_users_button);
        viewAllUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> users = getAllUsers();
                adapter.setmUsers(users);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private ArrayList<User> getFriends() {
        ArrayList<User> friendList = new ArrayList<>();
        friendList.add(new User("Username2", "email2@example.com", "password", "First2", "Last2"));
        return friendList;
    }

    private ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        for(int i = 0; i <= NUMBER_OF_INITIAL_USERS; i++) {
            User user = new User("Username" + i, "email" + i + "@example.com", "password", "First" + i, "Last" + i );
            users.add(user);
        }
        return users;
    }

    public void home(View v){
        Intent intent = new Intent(ViewAllUsersActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void people(View v){
        Intent intent = new Intent(ViewAllUsersActivity.this, ViewAllUsersActivity.class);
        startActivity(intent);
    }

    public void messages(View v){
        Intent intent = new Intent(ViewAllUsersActivity.this, ConversationActivity.class);
        startActivity(intent);
    }

    public void settings(View v){
        Intent intent = new Intent(ViewAllUsersActivity.this, FinderPreferenceActivity.class);
        startActivity(intent);
    }

    public void account(View v){
        Intent intent = new Intent(ViewAllUsersActivity.this, ProfileDetailsActivity.class);
        startActivity(intent);
    }
}
