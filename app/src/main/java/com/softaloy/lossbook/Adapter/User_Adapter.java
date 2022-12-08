package com.softaloy.lossbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softaloy.lossbook.Model.FollowModel;
import com.softaloy.lossbook.Model.Notification;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.R;
import com.softaloy.lossbook.databinding.UsersRvItemBinding;

import java.util.ArrayList;
import java.util.Date;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.viewHolder> {

    ArrayList<Users> list;
    Context context;

    public User_Adapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.users_rv_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Users users = list.get(position);
        Glide.with(context).load(users.getProfileImage())
                .placeholder(R.drawable.plc_person)
                .into(holder.binding.profileImage);
        holder.binding.name.setText(users.getName());
        holder.binding.email.setText(users.getEmail());

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(users.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_cng_bg));
                            holder.binding.followBtn.setText("Following");
                            holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.gray));
                            holder.binding.followBtn.setEnabled(false);

                        }else {

                            holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FollowModel followModel = new FollowModel();
                                    followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    followModel.setFollowedAt(new Date().getTime());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(users.getUserId())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(followModel)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users")
                                                            .child(users.getUserId())
                                                            .child("followerCount")
                                                            .setValue(users.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.button_cng_bg));
                                                                    holder.binding.followBtn.setText("Following");
                                                                    holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.gray));
                                                                    holder.binding.followBtn.setEnabled(false);

                                                                    Toast.makeText(context, "You Followed" + users.getName(), Toast.LENGTH_SHORT).show();


                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setType("follow");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("Notification")
                                                                            .child(users.getUserId())
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





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        UsersRvItemBinding binding;
        ;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = UsersRvItemBinding.bind(itemView);

        }
    }

}
