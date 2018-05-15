package ftn.tim2.finder.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ftn.tim2.finder.R;
import ftn.tim2.finder.activities.FinderPreferenceActivity;
import ftn.tim2.finder.activities.LoginActivity;
import ftn.tim2.finder.activities.MessageActivity;
import ftn.tim2.finder.activities.ProfileEditActivity;
import ftn.tim2.finder.model.User;

public class ProfileDetailsFragment extends Fragment {

    private View v;
    private Dialog rateDialog;
    private TextView nameProfile;
    private TextView usernameProfile;
    private TextView emailProfile;
    private TextView addressProfile;
    private TextView rateProfile;
    private TextView registrationDateProfile;
    private TextView descriptionProfile;
    private TextView followersProfile;
    private TextView followingProfile;

    private Button profile_comment_btn;
    private Button profile_messages_btn;
    private Button finder_preferences_btn;
    private ImageView edit_profile_img;
    private TextView sign_out;
    private ImageView profile_star_img;

    private static final String TAG = "ProfileDetailsFragment";

    private boolean showMyAccount;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;

    public ProfileDetailsFragment() {
        showMyAccount = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            showMyAccount = getArguments().getBoolean("myAccount");
        }

        rateDialog = new Dialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile_detail, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });

        if (showMyAccount) {
            hideMyAccountOptions();
        } else {
            hideMyAccountPreferences();
        }

        prepareData(v);

        return v;
    }

    private void prepareData(View v) {
        nameProfile = v.findViewById(R.id.name_profile);
        usernameProfile = v.findViewById(R.id.username_profile);
        emailProfile = v.findViewById(R.id.email_profile);
        addressProfile = v.findViewById(R.id.address_profile);
        rateProfile = v.findViewById(R.id.rate_profile);
        registrationDateProfile = v.findViewById(R.id.reg_date_profile);
        descriptionProfile = v.findViewById(R.id.description_profile);
        followersProfile = v.findViewById(R.id.followers_profile);
        followingProfile = v.findViewById(R.id.following_profile);

        profile_comment_btn = v.findViewById(R.id.profile_comment);
        profile_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeComments();
            }
        });

        profile_messages_btn = v.findViewById(R.id.profile_message);
        profile_messages_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeMessages();
            }
        });

        finder_preferences_btn = v.findViewById(R.id.profile_settings);
        finder_preferences_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreferences();
            }
        });

        sign_out = v.findViewById(R.id.signOut);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        edit_profile_img = v.findViewById(R.id.profile_edit);
        edit_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        profile_star_img = v.findViewById(R.id.profile_star);
        profile_star_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatePopup();
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(User.class);
        nameProfile.setText(user.getFirstName() + " " + user.getLastName());
        usernameProfile.setText("@" + user.getUsername());
        emailProfile.setText(user.getEmail());
        addressProfile.setText(user.getUserProfile().getCity());
        if(!user.getUserProfile().getCity().isEmpty() && !user.getUserProfile().getCountry().isEmpty()){
            addressProfile.append(", ");
        }
        addressProfile.append(user.getUserProfile().getCountry());
        rateProfile.setText(new Double(user.getUserProfile().getRate()).toString());
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        registrationDateProfile.setText(dateFormat.format("yyyy-MM-dd", user.getUserProfile().getRegistrationDate()));
        descriptionProfile.setText(user.getUserProfile().getDescription());
        if(user.getUserProfile().getFollowers() != null){
            followersProfile.setText(user.getUserProfile().getFollowers().size());
        }
        else{
            followersProfile.setText("0");
        }
        if(user.getUserProfile().getFollowing() != null){
            followingProfile.setText(user.getUserProfile().getFollowing().size());
        }
        else{
            followingProfile.setText("0");
        }
    }

    public void hideMyAccountOptions() {
        v.findViewById(R.id.profile_message).setVisibility(View.GONE);
        v.findViewById(R.id.profile_star).setVisibility(View.GONE);
        v.findViewById(R.id.follow).setVisibility(View.GONE);
    }

    public void hideMyAccountPreferences() {
        v.findViewById(R.id.profile_settings).setVisibility(View.GONE);
        v.findViewById(R.id.profile_edit).setVisibility(View.GONE);
    }

    private void showRatePopup(){
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

    private void showPreferences() {
        Intent intent = new Intent(getContext(), FinderPreferenceActivity.class);
        startActivity(intent);
    }

    private void seeMessages() {
        Intent intent = new Intent(getContext(), MessageActivity.class);
        startActivity(intent);
    }

    private void seeComments(){
        CommentFragment commentFragment = new CommentFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, commentFragment);
        transaction.commit();
    }

    private void editProfile() {
        Intent intent = new Intent(getContext(), ProfileEditActivity.class);
        startActivity(intent);
    }

    private void signOut(){
        firebaseAuth.signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}
