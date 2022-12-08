package com.softaloy.lossbook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softaloy.lossbook.Comment_Activity;
import com.softaloy.lossbook.Model.Notification;
import com.softaloy.lossbook.Model.Post_model;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.R;
import com.softaloy.lossbook.databinding.DashboardRvItemBinding;

import java.util.ArrayList;
import java.util.Date;

public class Post_Adapter extends RecyclerView.Adapter<Post_Adapter.viewHolder> {

    ArrayList<Post_model> list;
    Context context;

    public Post_Adapter(ArrayList<Post_model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_item, parent, false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Post_model model = list.get(position);
        Glide.with(context).load(model.getPostImage())
                .placeholder(R.drawable.plecholder)
                .into(holder.binding.postImge);

        holder.binding.love.setText(model.getPostLove() + "");
        holder.binding.comment.setText(model.getCommentCount()+"");

        String description = model.getPostDescription();
        if (description.equals("")) {
            holder.binding.postDescription.setVisibility(View.GONE);
        } else {
            holder.binding.postDescription.setText(model.getPostDescription());
            holder.binding.postDescription.setVisibility(View.VISIBLE);
        }


        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(model.getPostedBy()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Glide.with(context).load(users.getProfileImage())
                                .placeholder(R.drawable.plc_person)
                                .into(holder.binding.profileImg);
                        holder.binding.userName.setText(users.getName());
                        holder.binding.userAbout.setText(users.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference()
                .child("posts")
                .child(model.getPostId())
                .child("loves")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.love.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lover, 0, 0, 0);
                        } else {

                            holder.binding.love.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts")
                                            .child(model.getPostId())
                                            .child("loves")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("posts")
                                                            .child(model.getPostId())
                                                            .child("postLove")
                                                            .setValue(model.getPostLove() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.love.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lover, 0, 0, 0);


                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setPostId(model.getPostId());
                                                                    notification.setType("Love");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("Notification")
                                                                            .child(model.getPostedBy())
                                                                            .push()
                                                                            .setValue(notification);

                                                                }
                                                            });
                                                }
                                            });


                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.binding.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Comment_Activity.class);
                intent.putExtra("postId", model.getPostId());
                intent.putExtra("postedBy", model.getPostedBy());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        DashboardRvItemBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DashboardRvItemBinding.bind(itemView);
        }
    }

}
