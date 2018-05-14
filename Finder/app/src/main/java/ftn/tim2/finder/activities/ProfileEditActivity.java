package ftn.tim2.finder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ftn.tim2.finder.R;
import ftn.tim2.finder.model.User;

public class ProfileEditActivity extends AppCompatActivity{

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText usernameEdit;
    private EditText passwordPassword;
    private EditText cityEdit;
    private EditText countryEdit;
    private EditText descriptionEdit;
    private static final String TAG = "ProfileEditActivity";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
                firstNameEdit.setText(user.getFirstName());
                lastNameEdit.setText(user.getLastName());
                usernameEdit.setText(user.getUsername());
                passwordPassword.setText(user.getPassword());
                cityEdit.setText(user.getUserProfile().getCity());
                countryEdit.setText(user.getUserProfile().getCountry());
                descriptionEdit.setText(user.getUserProfile().getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });

        firstNameEdit = findViewById(R.id.firstname_edit);
        lastNameEdit = findViewById(R.id.lastname_edit);
        usernameEdit = findViewById(R.id.username_edit);
        passwordPassword = findViewById(R.id.password_edit);
        cityEdit = findViewById(R.id.city_edit);
        countryEdit = findViewById(R.id.country_edit);
        descriptionEdit = findViewById(R.id.description_edit);
    }
}
