package ftn.tim2.finder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ftn.tim2.finder.activities.CommentActivity;
import ftn.tim2.finder.activities.ProfileDetailsActivity;
import ftn.tim2.finder.activities.ProfileEditActivity;
import ftn.tim2.finder.activities.ViewAllUsersActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.viewallusersbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewAllUsersActivity.class);
                startActivity(intent);
            }
        });
        Button commentsBtn = findViewById(R.id.viewallcommentsbtn);
        commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                startActivity(intent);
            }
        });
        Button profileBtn = findViewById(R.id.viewprofile);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileDetailsActivity.class);
                startActivity(intent);
            }
        });
        Button profileEditBtn = findViewById(R.id.viewprofileedit);
        profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }
}
