package ftn.tim2.finder.adapters;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ftn.tim2.finder.MainActivity;
import ftn.tim2.finder.R;
import ftn.tim2.finder.fragments.ProfileDetailsFragment;
import ftn.tim2.finder.fragments.UserFragment;
import ftn.tim2.finder.model.User;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<User> mUsers = new ArrayList<User>();
    private Context mContext;
    private Fragment mFragment;

    public void setmUsers(ArrayList<User> mUsers) {
        this.mUsers = mUsers;
    }

    public UsersRecyclerViewAdapter(ArrayList<User> mUsers, Context mContext, Fragment fragment) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.mFragment = fragment;
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_users_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "on BindViewHolder: called");
        holder.username.setText(mUsers.get(position).getUsername());
        holder.email.setText(mUsers.get(position).getEmail());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeProfile();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    private void seeProfile() {
        ProfileDetailsFragment profileDetailsFragment = new ProfileDetailsFragment();
        FragmentManager fragmentManager = mFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, profileDetailsFragment);
        transaction.commit();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView email;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.user_item_username);
            email = itemView.findViewById(R.id.user_item_email);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
