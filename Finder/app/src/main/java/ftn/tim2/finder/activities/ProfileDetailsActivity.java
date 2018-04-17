package ftn.tim2.finder.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ftn.tim2.finder.R;

public class ProfileDetailsActivity extends AppCompatActivity {

    Dialog rateDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        rateDialog = new Dialog(this);
    }

    public void showRatePopup(View v){
        TextView closeRate;
        Button rateBtn;
        rateDialog.setContentView(R.layout.rate_popup);
        closeRate = rateDialog.findViewById(R.id.close_rate);
        rateBtn = rateDialog.findViewById(R.id.rate_submit);
        closeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateDialog.dismiss();
            }
        });
        rateDialog.show();
    }

    public void seeMessages(View v){
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }

    public void seeComments(View v){
        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
    }

    public void home(View v){
        Intent intent = new Intent(ProfileDetailsActivity.this, MapActivity.class);
        startActivity(intent);
    }

    public void people(View v){
        Intent intent = new Intent(ProfileDetailsActivity.this, ViewAllUsersActivity.class);
        startActivity(intent);
    }

    public void messages(View v){
        Intent intent = new Intent(ProfileDetailsActivity.this, ConversationActivity.class);
        startActivity(intent);
    }

    public void settings(View v){
        Intent intent = new Intent(ProfileDetailsActivity.this, FinderPreferenceActivity.class);
        startActivity(intent);
    }

    public void account(View v){
        Intent intent = new Intent(ProfileDetailsActivity.this, ProfileDetailsActivity.class);
        startActivity(intent);
    }
}
