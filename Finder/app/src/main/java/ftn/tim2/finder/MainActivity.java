package ftn.tim2.finder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ftn.tim2.finder.activities.CommentActivity;
import ftn.tim2.finder.activities.ConversationActivity;
import ftn.tim2.finder.activities.FinderPreferenceActivity;
import ftn.tim2.finder.activities.LoginActivity;
import ftn.tim2.finder.activities.MapActivity;
import ftn.tim2.finder.activities.ProfileDetailsActivity;
import ftn.tim2.finder.activities.ProfileEditActivity;
import ftn.tim2.finder.activities.RegistrationActivity;
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
        Button loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        Button registrationBtn = findViewById(R.id.registration);
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        Button preferencesBtn = findViewById(R.id.preferences);
        preferencesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FinderPreferenceActivity.class);
                startActivity(intent);
            }
        });
        Button conversationsBtn = findViewById(R.id.conversation);
        conversationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
                startActivity(intent);
            }
        });
        Button mapBtn = findViewById(R.id.mapbtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public void home(View v){
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void people(View v){
        Intent intent = new Intent(MainActivity.this, ViewAllUsersActivity.class);
        startActivity(intent);
    }

    public void messages(View v){
        Intent intent = new Intent(MainActivity.this, ConversationActivity.class);
        startActivity(intent);
    }

    public void settings(View v){
        Intent intent = new Intent(MainActivity.this, FinderPreferenceActivity.class);
        startActivity(intent);
    }

    public void account(View v){
        Intent intent = new Intent(MainActivity.this, ProfileDetailsActivity.class);
        startActivity(intent);
    }
}
