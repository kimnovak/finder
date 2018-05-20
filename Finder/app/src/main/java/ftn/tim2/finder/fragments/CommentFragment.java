package ftn.tim2.finder.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.activities.LoginActivity;
import ftn.tim2.finder.adapters.CommentAdapter;
import ftn.tim2.finder.model.Comment;
import ftn.tim2.finder.model.User;

public class CommentFragment extends Fragment {

    private View v;

    private List<Comment> commentList;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;

    private ImageButton postNewComment;
    private EditText comment_content;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseComments;
    private DatabaseReference databaseUsers;
    private static final String TAG = "CommentFragment";


    public CommentFragment() {
        commentList = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_comments, container, false);

        recyclerView = v.findViewById(R.id.recycler_comments_view);
        commentAdapter = new CommentAdapter(commentList, getContext());
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(commentAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        databaseComments = FirebaseDatabase.getInstance().getReference("comments");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        prepareData(v);

        return v;
    }

    private void prepareData(View v){
        postNewComment = v.findViewById(R.id.postNewComment);
        postNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
        comment_content = v.findViewById(R.id.comment_content);

        databaseUsers.child(getArguments().getString("user_ID")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();
                User usersProfile = dataSnapshot.getValue(User.class);
                if(usersProfile.getUserProfile().getComments() != null) {
                    for (String key : usersProfile.getUserProfile().getComments().keySet()) {
                        commentList.add(usersProfile.getUserProfile().getComments().get(key));
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });
    }

    private void postComment() {
        if(TextUtils.isEmpty(comment_content.getText().toString().trim())){
            return;
        }
        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User commenter = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
                Comment comment = new Comment(comment_content.getText().toString().trim(), new Date(), commenter);
                String id = databaseComments.push().getKey();
                databaseComments.child(id).setValue(comment);

                User usersProfile = dataSnapshot.child(getArguments().getString("user_ID")).getValue(User.class);
                if(usersProfile.getUserProfile().getComments() == null){
                    usersProfile.getUserProfile().setComments(new HashMap<String, Comment>());
                }
                usersProfile.getUserProfile().getComments().put(id, comment);
                databaseUsers.child(getArguments().getString("user_ID")).child("userProfile").child("comments").setValue(usersProfile.getUserProfile().getComments());
                comment_content.setText("");
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });
    }
}
