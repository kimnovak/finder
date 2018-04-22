package ftn.tim2.finder.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ftn.tim2.finder.R;
import ftn.tim2.finder.activities.MessageActivity;
import ftn.tim2.finder.activities.ProfileEditActivity;

public class ProfileDetailsFragment extends Fragment {

    private View v;
    private Dialog rateDialog;

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

        Button profile_comment_btn = v.findViewById(R.id.profile_comment);
        profile_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeComments();
            }
        });

        Button profile_messages_btn = v.findViewById(R.id.profile_message);
        profile_messages_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeMessages();
            }
        });

        ImageView edit_profile_img = v.findViewById(R.id.profile_edit);
        edit_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        ImageView profile_star_img = v.findViewById(R.id.profile_star);
        profile_star_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatePopup();
            }
        });

        return v;
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
}
