package ftn.tim2.finder;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import ftn.tim2.finder.activities.FinderPreferenceActivity;
import ftn.tim2.finder.fragments.ConversationFragment;
import ftn.tim2.finder.fragments.MapFragment;
import ftn.tim2.finder.fragments.ProfileDetailsFragment;
import ftn.tim2.finder.fragments.SettingsFragment;
import ftn.tim2.finder.fragments.UserFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                String tag = null;
                switch (item.getItemId()) {
                    case R.id.home_toolbar:
                        selectedFragment = new MapFragment();
                        tag = "MAP_FRAGMENT";
                        break;
                    case R.id.users_toolbar:
                        selectedFragment = new UserFragment();
                        tag = "USER_FRAGMENT";
                        break;
                    case R.id.conversation_toolbar:
                        selectedFragment = new ConversationFragment();
                        tag = "CONVERSATION_FRAGMENT";
                        break;
                    case R.id.account_toolbar:
                        selectedFragment = new ProfileDetailsFragment();
                        tag = "PROFILE_DETAILS_FRAGMENT";
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, selectedFragment, tag);
                transaction.commit();
                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, new MapFragment());
        transaction.commit();
    }


}
