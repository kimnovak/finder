package ftn.tim2.finder.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.FragmentTransaction;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
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
    private ArrayList<User> mFollowers;
    private ArrayList<User> mFollowing;
    private UsersRecyclerViewAdapter adapt;
    private int selectedTab = 0;

    public UserFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        Log.d(TAG, "view all users activity - oncreate method");
        mUsers = new ArrayList<>();
        mFollowers = new ArrayList<>();
        mFollowing = new ArrayList<>();
        Log.d(TAG, "viewall done");
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
        Log.d(TAG, "initializeUsers");
        final View view = v;
        Log.d(TAG, view.toString());
        Log.d(TAG, databaseUsers.toString());
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "tu");
                // Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
//                    Log.d(TAG, user.getFirstName());
                    addUser(user);
                }
                initRecyclerView(view);
                getFollowers();
                getFollowing();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void initRecyclerView(View v) {
        Log.d(TAG, "initRecyclerVieww");
        RecyclerView recyclerView = v.findViewById(R.id.recyclerusers_view);
        final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mUsers, getContext(), this);
        adapter.notifyDataSetChanged();
        adapt = adapter;
        Log.d(TAG, "ada");
        Log.d(TAG, adapter.toString());
        TabLayout tabLayout = v.findViewById(R.id.users_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, String.valueOf(tab.getPosition()));
                if(tab.getPosition() == 0) {
                    //show all users
                    getAllUsers();
                    adapter.setmUsers(mUsers);
                    adapter.notifyDataSetChanged();
                    selectedTab = 0;
                } else if(tab.getPosition() == 1) {
                    //show followers
                    getFollowers();
                    adapter.setmUsers(mFollowers);
                    adapter.notifyDataSetChanged();
                    selectedTab = 1;
                } else if(tab.getPosition() == 2) {
                    getFollowing();
                    adapter.setmUsers(mFollowing);
                    adapter.notifyDataSetChanged();
                    selectedTab = 2;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Log.d(TAG, "proslo");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final SearchView search = (SearchView) v.findViewById(R.id.search_users_button);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "search");
                Log.d(TAG, query);
                findUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "search2");
                if(newText == null || newText.isEmpty()) {
                    clearFilter();
                }
                return false;
            }
        });

    }
    private void findUsers(String query) {
        ArrayList<User> filteredUsers = new ArrayList<User>();
        if(selectedTab == 0) {
            for(User u: mUsers) {
                if(u.getUsername().contains(query)) {
                    filteredUsers.add(u);
                }
            }
        } else if(selectedTab == 1) {
            for(User f: mFollowers) {
                if(f.getUsername().contains(query)) {
                    filteredUsers.add(f);
                }
            }
        } else if(selectedTab == 2) {
            for(User f: mFollowing) {
                if(f.getUsername().contains(query)) {
                    filteredUsers.add(f);
                }
            }
        }
        if(adapt != null) {
            adapt.setmUsers(filteredUsers);
            adapt.notifyDataSetChanged();
        }
    }

    private void clearFilter() {
        ArrayList<User> filteredUsers = new ArrayList<User>();
        if(selectedTab == 0) {
            filteredUsers = mUsers;
        } else if(selectedTab == 1) {
            filteredUsers = mFollowers;
        } else if(selectedTab == 2) {
            filteredUsers = mFollowing;
        }
        adapt.setmUsers(filteredUsers);
        adapt.notifyDataSetChanged();
    }

    private ArrayList<User> getFollowers() {
        Log.d(TAG, "get Followers");
        String currentUserId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseUsers.child(currentUserId).child("userProfile").child("followers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "new Friend added");
                addFollower(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Friend removed");
                removeFollower(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mFollowers;
    }

    private ArrayList<User> getFollowing() {
        Log.d(TAG, "get Followers");
        String currentUserId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseUsers.child(currentUserId).child("userProfile").child("following").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addFollowing(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Friend removed");
                removeFollowing(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mFollowing;
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

    private void addFollower(String id) {
        for(User f: mFollowers) {
            if(f.getId().equals(id)) {
                return;
            }
        }
        User follower = null;
        for(User user: mUsers) {
            if(user.getId().equals(id)) {
                follower = user;
                break;
            }
        }
        if(follower != null) {
            mFollowers.add(follower);
            adapt.notifyDataSetChanged();
        }
    }

    private void addFollowing(String id) {
        for(User f: mFollowing) {
            if(f.getId().equals(id)) {
                return;
            }
        }
        User following = null;
        for(User user: mUsers) {
            if(user.getId().equals(id)) {
                following = user;
                break;
            }
        }
        if(following != null) {
            mFollowing.add(following);
            adapt.notifyDataSetChanged();
        }
    }


    private void removeFollower(String id) {
        User follower = null;
        for(User user: mFollowers) {
            if(user.getId().equals(id)) {
                follower = user;
                break;
            }
        }
        if(follower != null) {
            mFollowers.remove(follower);
        }
        adapt.notifyDataSetChanged();
    }

    private void removeFollowing(String id) {
        User following = null;
        for(User user: mFollowing) {
            if(user.getId().equals(id)) {
                following = user;
                break;
            }
        }
        if(following != null) {
            mFollowing.remove(following);
        }
        adapt.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "resume v");
        super.onResume();
        Log.d(TAG, "resume");
    }



}
