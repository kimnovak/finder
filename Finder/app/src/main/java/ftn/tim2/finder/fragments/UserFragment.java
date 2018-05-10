package ftn.tim2.finder.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.UsersRecyclerViewAdapter;
import ftn.tim2.finder.model.User;

public class UserFragment extends Fragment {

    private View v;

    private static final String TAG = "ViewAllUsersActivity";
    private ArrayList<User> mUsers;
    private final int NUMBER_OF_INITIAL_USERS = 5;

    public UserFragment() {
        mUsers = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_users, container, false);

        Log.d(TAG, "view all users activity - oncreate method");
        addInitialData();
        initRecyclerView(v);

        return v;
    }

    private void addInitialData() {
        for(int i = 0; i <= NUMBER_OF_INITIAL_USERS; i++) {
            User user = new User("1", "Username" + i, "email" + i + "@example.com", "password", "First" + i, "Last" + i );
            mUsers.add(user);
        }
    }

    private void initRecyclerView(View v) {
        Log.d(TAG, "initRecyclerView");
        RecyclerView recyclerView = v.findViewById(R.id.recyclerusers_view);
        final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mUsers, getContext(), this);
        Button viewFriendsBtn = v.findViewById(R.id.view_friends_button);
        viewFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> friendList = getFriends();
                adapter.setmUsers(friendList);
                adapter.notifyDataSetChanged();
            }
        });
        Button viewAllUsersBtn = v.findViewById(R.id.view_all_users_button);
        viewAllUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<User> users = getAllUsers();
                adapter.setmUsers(users);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private ArrayList<User> getFriends() {
        ArrayList<User> friendList = new ArrayList<>();
        friendList.add(new User("1", "Username2", "email2@example.com", "password", "First2", "Last2"));
        return friendList;
    }

    private ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        for(int i = 0; i <= NUMBER_OF_INITIAL_USERS; i++) {
            User user = new User("1", "Username" + i, "email" + i + "@example.com", "password", "First" + i, "Last" + i );
            users.add(user);
        }
        return users;
    }
}
