package ftn.tim2.finder;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

    @Override
    public void onBackPressed()
    {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            finishAffinity();
                            System.exit(0);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            getFragmentManager().popBackStack();
        }
    }

}
