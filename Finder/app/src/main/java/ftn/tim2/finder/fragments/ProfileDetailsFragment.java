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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.activities.FinderPreferenceActivity;
import ftn.tim2.finder.activities.LoginActivity;
import ftn.tim2.finder.activities.MessageActivity;
import ftn.tim2.finder.activities.ProfileEditActivity;
import ftn.tim2.finder.model.User;
import ftn.tim2.finder.service.ClientNotificationsViaFCMServerHelper;

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
    private ImageView imageProfile;
    private TextView closeRate;
    private Button rateBtn;
    private RatingBar ratingBar;

    private Button profile_follow_btn;
    private Button profile_unfollow_btn;
    private Button profile_comment_btn;
    private Button profile_messages_btn;
    private Button finder_preferences_btn;

    private ImageView edit_profile_img;
    private ImageView profile_star_img;

    private static final String TAG = "ProfileDetailsFragment";
    private String ID;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseUsers;
    private User currentUser;
    private User user;

    private ClientNotificationsViaFCMServerHelper clientNotificationsViaFCMServerHelper;


    public ProfileDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        if(getArguments() != null && getArguments().containsKey("user_ID")){
            ID = getArguments().getString("user_ID");
            hideMyAccountPreferences();
        }
        else{
            ID = firebaseAuth.getCurrentUser().getUid();
            hideMyAccountOptions();
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

        clientNotificationsViaFCMServerHelper = new ClientNotificationsViaFCMServerHelper(getContext());

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
        imageProfile = v.findViewById(R.id.image_profile);

        profile_unfollow_btn = v.findViewById(R.id.unfollow);
        profile_unfollow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfollow();
            }
        });

        profile_follow_btn = v.findViewById((R.id.follow));
        profile_follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });

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
        user = dataSnapshot.child(ID).getValue(User.class);
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
            followersProfile.setText(String.valueOf(user.getUserProfile().getFollowers().size()));
        }
        else{
            followersProfile.setText("0");
        }
        if(user.getUserProfile().getFollowing() != null){
            followingProfile.setText(String.valueOf(user.getUserProfile().getFollowing().size()));
        }
        else{
            followingProfile.setText("0");
        }
        if(isAdded() && !user.getUserProfile().getImage().isEmpty()) {
            Glide.with(this).load(user.getUserProfile().getImage()).into(imageProfile);
        }
        if(currentUser == null) {
            if (user.getUserProfile().getFollowers() != null) {
                if (user.getUserProfile().getFollowers().contains(firebaseAuth.getCurrentUser().getUid())) {
                    profile_follow_btn.setVisibility(View.GONE);
                } else {
                    profile_unfollow_btn.setVisibility(View.GONE);
                }
            } else {
                profile_unfollow_btn.setVisibility(View.GONE);
            }
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
        rateDialog.setContentView(R.layout.rate_popup);
        closeRate = rateDialog.findViewById(R.id.close_rate);
        rateBtn = rateDialog.findViewById(R.id.rate_submit);
        ratingBar = rateDialog.findViewById(R.id.rating_bar);
        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.getUserProfile().getRateCalc().setCount(user.getUserProfile().getRateCalc().getCount() + 1);
                user.getUserProfile().getRateCalc().setScore(user.getUserProfile().getRateCalc().getScore()+ (int)ratingBar.getRating());
                user.getUserProfile().setRate(user.getUserProfile().getRateCalc().getScore()*1.0/user.getUserProfile().getRateCalc().getCount());
                databaseUsers.child(user.getId()).child("userProfile").child("rate").setValue(user.getUserProfile().getRate());
                databaseUsers.child(user.getId()).child("userProfile").child("rateCalc").setValue(user.getUserProfile().getRateCalc());

                notifyReceiever(firebaseAuth.getCurrentUser().getUid(), user.getFcmToken());

                rateDialog.dismiss();
            }
        });
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

    private void follow() {
        Log.d(TAG, "follow");
       readCurrentUserFromDB(true);
    }

    private void readCurrentUserFromDB(boolean follow) {
        Log.d(TAG, "read current user");
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        final boolean tofollow = follow;
        databaseUsers.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                if(tofollow) {
                    followProfileOwner();
                }else {
                    unfollowProfileOwner();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void followProfileOwner() {
        Log.d(TAG, "follow profile owner");
        if(!currentUser.getId().equals(user.getId())) {
            List<String> followers = null;
            if(user.getUserProfile().getFollowers() == null) {
                followers = new ArrayList<String>();
            }else {
                followers = user.getUserProfile().getFollowers();
            }
            if(!followers.contains(currentUser.getId())) {
                followers.add(currentUser.getId());
                user.getUserProfile().setFollowers(followers);
                databaseUsers.child(user.getId()).child("userProfile").child("followers").setValue(followers);
            } else {
                Toast toast = Toast.makeText(getContext(), "You're already following " + user.getUsername(), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            List<String> following = null;
            if(currentUser.getUserProfile().getFollowing() == null) {
                following = new ArrayList<String>();
            } else {
                following = currentUser.getUserProfile().getFollowing();
            }
            following.add(user.getId());
            currentUser.getUserProfile().setFollowing(following);
            databaseUsers.child(currentUser.getId()).child("userProfile").child("following").setValue(following);
            profile_follow_btn.setVisibility(View.GONE);
            profile_unfollow_btn.setVisibility(View.VISIBLE);
        }
    }

    private void unfollow() {
        readCurrentUserFromDB(false);
    }

    private void unfollowProfileOwner() {
        Log.d(TAG, "unfollow profile owner");
        List<String> following = currentUser.getUserProfile().getFollowing();
        following.remove(user.getId());
        databaseUsers.child(currentUser.getId()).child("userProfile").child("following").setValue(following);
        List<String> followers = user.getUserProfile().getFollowers();
        followers.remove(currentUser.getId());
        databaseUsers.child(user.getId()).child("userProfile").child("followers").setValue(followers);
        profile_unfollow_btn.setVisibility(View.GONE);
        profile_follow_btn.setVisibility(View.VISIBLE);
    }


    private void seeMessages() {
        Intent intent = new Intent(getContext(), MessageActivity.class);
        intent.putExtra("USER_ID", getArguments().getString("user_ID"));
        startActivity(intent);
    }

    private void seeComments(){
        CommentFragment commentFragment = new CommentFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("user_ID", ID);
        commentFragment.setArguments(bundle);

        transaction.replace(R.id.content_frame, commentFragment);
        transaction.commit();
    }

    private void editProfile() {
        Intent intent = new Intent(getContext(), ProfileEditActivity.class);
        startActivity(intent);
    }

    private void notifyReceiever(String id, String fcmToken) {
        clientNotificationsViaFCMServerHelper
                .sendNotification("Finder", "Your profile is rated.", id, fcmToken);
    }
}
