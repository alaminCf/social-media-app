package com.softaloy.lossbook.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softaloy.lossbook.Model.Comment;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.R;
import com.softaloy.lossbook.databinding.CommentRvItemBinding;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewHolder> {


    ArrayList<Comment> list;
    Context context;

    public CommentAdapter(ArrayList<Comment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.comment_rv_item, parent, false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Comment comment = list.get(position);
        String time = TimeAgo.using(comment.getCommentedAt());
        holder.binding.time.setText(time);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(comment.getCommentedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Glide.with(context).load(users.getProfileImage())
                                .placeholder(R.drawable.plc_person)
                                .into(holder.binding.profileImage);
                        holder.binding.comment.setText(Html.fromHtml("<b>"+ users.getName() + "</b>" +"  " +comment.getCommentBody()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {


        CommentRvItemBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentRvItemBinding.bind(itemView);
        }
    }

}
