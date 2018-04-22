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

        holder.tv_name.setText(conversations.get(position).getReceiever().getFirstName() + " " +
                            conversations.get(position).getReceiever().getLastName());
        holder.tv_email.setText(conversations.get(position).getReceiever().getEmail());
        //holder.img.setImageResource(0); //messages.get(position).getSender().get
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
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
        private TextView tv_email;
        private ImageView img;
        private LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.name_message);
            tv_email = itemView.findViewById(R.id.email_message);
            img = itemView.findViewById(R.id.img_message);
            parentLayout = itemView.findViewById(R.id.parent_layout_conversation);
        }
    }

}
