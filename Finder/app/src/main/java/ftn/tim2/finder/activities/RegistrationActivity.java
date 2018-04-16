package ftn.tim2.finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ftn.tim2.finder.R;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void backToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
