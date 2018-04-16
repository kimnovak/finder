package ftn.tim2.finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ftn.tim2.finder.MainActivity;
import ftn.tim2.finder.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToRegistration(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
