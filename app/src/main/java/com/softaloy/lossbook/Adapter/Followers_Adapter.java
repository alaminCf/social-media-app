package com.softaloy.lossbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softaloy.lossbook.Model.FollowModel;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.R;
import com.softaloy.lossbook.databinding.FriendsRvItemBinding;

import java.util.ArrayList;

public class Followers_Adapter extends RecyclerView.Adapter<Followers_Adapter.viewHolder>{

ArrayList<FollowModel> list;
Context context;

    public Followers_Adapter(ArrayList<FollowModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friends_rv_item, parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        FollowModel followModel = list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(followModel.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        Glide.with(context).load(users.getProfileImage())
                                .placeholder(R.drawable.plc_person)
                                .into(holder.binding.profileImage);

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


        FriendsRvItemBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = FriendsRvItemBinding.bind(itemView);

        }
    }

}
