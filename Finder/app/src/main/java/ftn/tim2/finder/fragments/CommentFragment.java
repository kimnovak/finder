package ftn.tim2.finder.fragments;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.adapters.CommentAdapter;
import ftn.tim2.finder.model.Comment;
import ftn.tim2.finder.model.User;

public class CommentFragment extends Fragment {

    private View v;

    private List<Comment> commentList;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;

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

        prepareData();

        return v;
    }

    private void prepareData(){
        User user = new User("1", "username", "email", "password", "firstName", "lastName");
        Comment comment = new Comment("This is some comment", new Date(), user);
        commentList.add(comment);

        comment = new Comment("This is second comment", new Date(), user);
        commentList.add(comment);

        comment = new Comment("This is third comment", new Date(), user);
        commentList.add(comment);

        comment = new Comment("Some text text, some comment....", new Date(), user);
        commentList.add(comment);

        comment = new Comment("Last comment", new Date(), user);
        commentList.add(comment);


        commentAdapter.notifyDataSetChanged();
    }
}
