package ftn.tim2.finder.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import ftn.tim2.finder.R;
import ftn.tim2.finder.model.Comment;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userComment, content, dateCreated;

        public ViewHolder(View view) {
            super(view);
            userComment = view.findViewById(R.id.userComment);
            content = view.findViewById(R.id.content);
            dateCreated = view.findViewById(R.id.dateCreated);
        }
    }

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        holder.userComment.setText(comment.getUser().getUsername());
        holder.content.setText(comment.getContent());
        holder.dateCreated.setText(dateFormat.format("yyyy-MM-dd hh:mm", comment.getDateCreated()));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
