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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.UsersRecyclerViewAdapter;
import ftn.tim2.finder.model.User;

public class UserFragment extends Fragment {

     private View v;

    private static final String TAG = "ViewAllUsersActivity";
    private ArrayList<User> mUsers;
    private final int NUMBER_OF_INITIAL_USERS = 5;
    private DatabaseReference databaseUsers;
    private ArrayList<User> mFriends;

    public UserFragment() {
        mUsers = new ArrayList<>();
        mFriends = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        Log.d(TAG, "view all users activity - oncreate method");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_users, container, false);
        Log.d(TAG, "view all users activity - oncreateview method");
        initializeUsers(v);

        //initRecyclerView(v);

        return v;
    }

    private void initializeUsers(View v) {
        final View view = v;
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "tu");
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    Log.d(TAG, user.getFirstName());
                    addUser(user);
                }
                initRecyclerView(view);
                getFriends();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void initRecyclerView(View v) {
        Log.d(TAG, "initRecyclerView");
        RecyclerView recyclerView = v.findViewById(R.id.recyclerusers_view);
        final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mUsers, getContext(), this);
        adapter.notifyDataSetChanged();
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
                getAllUsers();
                adapter.setmUsers(mUsers);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private ArrayList<User> getFriends() {
        Log.d(TAG, "get Friends");
        String currentUserId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseUsers.child(currentUserId).child("friends").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "new Friend added");
                addFriend(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mFriends;
    }

    private void getAllUsers() {
        Log.d(TAG, "get All Users");
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    Log.d(TAG, user.getFirstName());
                    addUser(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addUser(User user) {
        for(User u: mUsers) {
            if(u.getEmail().equals(user.getEmail())) {
                return;
            }
        }
        mUsers.add(user);
    }

    private void addFriend(String id) {
        for(User f: mFriends) {
            if(f.getId().equals(id)) {
                return;
            }
        }
        User friend = null;
        for(User user: mUsers) {
            if(user.getId().equals(id)) {
                friend = user;
                break;
            }
        }
        if(friend != null) {
            mFriends.add(friend);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
