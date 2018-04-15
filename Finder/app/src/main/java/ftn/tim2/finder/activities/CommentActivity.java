package ftn.tim2.finder.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
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
import ftn.tim2.finder.model.Comment;
import ftn.tim2.finder.model.User;

public class CommentActivity extends AppCompatActivity {

    private List<Comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        recyclerView = findViewById(R.id.recycler_comments_view);
        commentAdapter = new CommentAdapter(commentList);
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(commentAdapter);


        prepareData();
    }

    private void prepareData(){
        User user = new User("username", "email", "password", "firstName", "lastName");
        Comment comment = new Comment("This is some comment", new Date(), user);
        commentList.add(comment);

        Comment comment2 = new Comment("This is second comment", new Date(), user);
        commentList.add(comment2);

        Comment comment3 = new Comment("This is third comment", new Date(), user);
        commentList.add(comment3);

        Comment comment4 = new Comment("Some text text, some comment....", new Date(), user);
        commentList.add(comment4);

        Comment comment5 = new Comment("Last comment", new Date(), user);
        commentList.add(comment5);

        commentAdapter.notifyDataSetChanged();
    }
}
