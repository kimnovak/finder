package ftn.tim2.finder.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

import ftn.tim2.finder.MainActivity;
import ftn.tim2.finder.R;
import ftn.tim2.finder.model.User;
import ftn.tim2.finder.model.UserProfile;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailRegistration;
    private EditText passwordRegistration;
    private EditText firstNameRegistration;
    private EditText lastNameRegistration;
    private EditText usernameRegistration;
    private static final String TAG = "RegistrationActivity";

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        progressDialog = new ProgressDialog(this);

        emailRegistration = findViewById(R.id.email_registration);
        passwordRegistration = findViewById(R.id.password_registration);
        firstNameRegistration = findViewById(R.id.firstname_registration);
        lastNameRegistration = findViewById(R.id.lastname_registration);
        usernameRegistration = findViewById(R.id.username_registration);
    }

    public void registration(View v){
        final String email = emailRegistration.getText().toString().trim();
        final String password = passwordRegistration.getText().toString().trim();
        final String firstName = firstNameRegistration.getText().toString().trim();
        final String lastName = lastNameRegistration.getText().toString().trim();
        final String username = usernameRegistration.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) || TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please input all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(firebaseAuth.getCurrentUser().getUid(), username, email,
                                password, firstName, lastName, new HashMap<String, String>());

                        UserProfile userProfile = new UserProfile("", "", new Date(), 0, "", "");
                        user.setUserProfile(userProfile);

                        databaseUsers.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);

                        Log.d(TAG, "Registration successful!");

                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(RegistrationActivity.this, "Could not register, please try again!", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
        });

    }

    public void backToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
