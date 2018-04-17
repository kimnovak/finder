package ftn.tim2.finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ftn.tim2.finder.R;

public class ProfileEditActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
    }

    public void home(View v){
        Intent intent = new Intent(ProfileEditActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void people(View v){
        Intent intent = new Intent(ProfileEditActivity.this, ViewAllUsersActivity.class);
        startActivity(intent);
    }

    public void messages(View v){
        Intent intent = new Intent(ProfileEditActivity.this, ConversationActivity.class);
        startActivity(intent);
    }

    public void settings(View v){
        Intent intent = new Intent(ProfileEditActivity.this, FinderPreferenceActivity.class);
        startActivity(intent);
    }

    public void account(View v){
        Intent intent = new Intent(ProfileEditActivity.this, ProfileDetailsActivity.class);
        startActivity(intent);
    }
}
