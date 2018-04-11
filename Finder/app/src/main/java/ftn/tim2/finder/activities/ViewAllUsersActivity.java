package ftn.tim2.finder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ftn.tim2.finder.R;
import ftn.tim2.finder.UsersRecyclerViewAdapter;
import ftn.tim2.finder.model.User;

public class ViewAllUsersActivity extends AppCompatActivity {
    private static final String TAG = "ViewAllUsersActivity";
    private ArrayList<User> mUsers = new ArrayList<>();
    private final int NUMBER_OF_INITIAL_USERS = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_users);
        Log.d(TAG, "view all users activity");
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
        UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mUsers,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
