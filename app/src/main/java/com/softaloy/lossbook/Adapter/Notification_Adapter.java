package com.softaloy.lossbook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softaloy.lossbook.Comment_Activity;
import com.softaloy.lossbook.Model.Notification;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.R;
import com.softaloy.lossbook.databinding.Notification2RvItemBinding;

import java.util.ArrayList;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.viewHolder>{

    ArrayList<Notification> list;
    Context context;

    public Notification_Adapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification2_rv_item, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Notification notification = list.get(position);
        String type = notification.getType();

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(notification.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        Glide.with(context).load(users.getProfileImage())
                                .placeholder(R.drawable.plc_person)
                                .into(holder.binding.profileImage);

                        if (type.equals("Love")){
                            holder.binding.notiText.setText(Html.fromHtml("<b>"+ users.getName()+ "</b>"+"  "  +"Loved Your Post"));
                        }else if (type.equals("Comment")){

                            holder.binding.notiText.setText(Html.fromHtml("<b>"+ users.getName()+"</b>"+"  "  +"Commented on Your Post"));
                        }else {

                            holder.binding.notiText.setText(Html.fromHtml("<b>"+ users.getName()+"</b>"+"  "  +"start Following You"));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.binding.openNotificition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("Comment")){
                    holder.binding.openNotificition.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(context, Comment_Activity.class);
                    intent.putExtra("postId",notification.getPostId());
                    intent.putExtra("postedBy",notification.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {


        Notification2RvItemBinding binding;

        public viewHolder(@NonNull View itemView) {

            super(itemView);

            binding = Notification2RvItemBinding.bind(itemView);


        }
    }

}
