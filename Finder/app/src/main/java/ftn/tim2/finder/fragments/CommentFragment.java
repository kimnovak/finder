package ftn.tim2.finder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
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
        commentAdapter = new CommentAdapter(commentList);
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

        commentAdapter.notifyDataSetChanged();
    }

    private void postComment() {
        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Comment comment = new Comment(comment_content.getText().toString().trim(), new Date(), user);
                String id = databaseComments.push().getKey();
                databaseComments.child(id).setValue(comment);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
