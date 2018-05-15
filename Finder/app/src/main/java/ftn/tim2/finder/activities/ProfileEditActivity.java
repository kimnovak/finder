package ftn.tim2.finder.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

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

    private TextView nameEditLabel;
    private TextView usernameEditLabel;

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

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });

        prepareData();
    }

    @Override
    public void onBackPressed()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).child("firstName").setValue(firstNameEdit.getText().toString().trim());
                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).child("lastName").setValue(lastNameEdit.getText().toString().trim());
                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).child("username").setValue(usernameEdit.getText().toString().trim());
                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).child("password").setValue(passwordPassword.getText().toString().trim());
                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).child("userProfile").child("city").setValue(cityEdit.getText().toString().trim());
                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).child("userProfile").child("country").setValue(countryEdit.getText().toString().trim());
                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).child("userProfile").child("description").setValue(descriptionEdit.getText().toString().trim());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
                ProfileEditActivity.super.onBackPressed();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save changes?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void prepareData() {
        firstNameEdit = findViewById(R.id.firstname_edit);
        lastNameEdit = findViewById(R.id.lastname_edit);
        usernameEdit = findViewById(R.id.username_edit);
        passwordPassword = findViewById(R.id.password_edit);
        cityEdit = findViewById(R.id.city_edit);
        countryEdit = findViewById(R.id.country_edit);
        descriptionEdit = findViewById(R.id.description_edit);

        nameEditLabel = findViewById(R.id.name_edit_label);
        usernameEditLabel = findViewById(R.id.username_edit_label);
    }


    private void showData(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
        firstNameEdit.setText(user.getFirstName());
        lastNameEdit.setText(user.getLastName());
        usernameEdit.setText(user.getUsername());
        passwordPassword.setText(user.getPassword());
        cityEdit.setText(user.getUserProfile().getCity());
        countryEdit.setText(user.getUserProfile().getCountry());
        descriptionEdit.setText(user.getUserProfile().getDescription());

        nameEditLabel.setText(user.getFirstName() + " " + user.getLastName());
        usernameEditLabel.setText("@" + user.getUsername());
    }
}
