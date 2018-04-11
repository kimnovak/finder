package ftn.tim2.finder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import ftn.tim2.finder.R;
import ftn.tim2.finder.UsersRecyclerViewAdapter;

public class ViewAllUsersActivity extends AppCompatActivity {
    private static final String TAG = "ViewAllUsersActivity";
    private ArrayList<String> mNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_users);
        Log.d(TAG, "view all users activity");
        mNames.add("bla");
        mNames.add("ssss");
        mNames.add("gggg");
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView");
        RecyclerView recyclerView = findViewById(R.id.recyclerusers_view);
        UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mNames,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
