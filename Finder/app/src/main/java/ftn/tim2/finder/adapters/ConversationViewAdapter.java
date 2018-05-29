package ftn.tim2.finder.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.activities.MessageActivity;
import ftn.tim2.finder.model.Conversation;

public class ConversationViewAdapter extends RecyclerView.Adapter<ConversationViewAdapter.ViewHolder> {

    private Context context;
    private List<Conversation> conversations;

    public ConversationViewAdapter(Context context, List<Conversation> conversations) {
        this.context = context;
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.conversations_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Conversation conversation = conversations.get(position);

        holder.tv_name.setText(conversation.getParticipant().getFirstName() + " " +
                conversation.getParticipant().getLastName());
        holder.tv_last.setText(conversation.getLastMessage());
        if(!conversation.getParticipant().getUserProfile().getImage().isEmpty()) {
            Glide.with(context).load(conversation.getParticipant().getUserProfile().getImage()).into(holder.img);
        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("USER_ID",conversation.getParticipant().getId());
                ActivityCompat.startActivity(context, intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_last;
        private ImageView img;
        private LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.name_message);
            tv_last= itemView.findViewById(R.id.last_message);
            img = itemView.findViewById(R.id.img_message);
            parentLayout = itemView.findViewById(R.id.parent_layout_conversation);
        }
    }

}
