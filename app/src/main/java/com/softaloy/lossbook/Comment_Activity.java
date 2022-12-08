package com.softaloy.lossbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softaloy.lossbook.Adapter.CommentAdapter;
import com.softaloy.lossbook.Model.Comment;
import com.softaloy.lossbook.Model.Notification;
import com.softaloy.lossbook.Model.Post_model;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.databinding.ActivityCommentBinding;
import com.softaloy.lossbook.databinding.ActivitySingUpBinding;

import java.util.ArrayList;
import java.util.Date;

public class Comment_Activity extends AppCompatActivity {

    ActivityCommentBinding binding;
    Intent intent;
    String postId;
    String postedBy;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Comment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar2);
        Comment_Activity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        intent = getIntent();


        postId = intent.getStringExtra("postId");
        postedBy = intent.getStringExtra("postedBy");

        database.getReference().child("posts")
                .child(postId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Post_model postModel = snapshot.getValue(Post_model.class);
                        Glide.with(getApplicationContext()).load(postModel.getPostImage())
                                .placeholder(R.drawable.plc_person)
                                .into(binding.postImg);
                        binding.description.setText(postModel.getPostDescription());
                        binding.love.setText(postModel.getPostLove() + "");
                        binding.comment.setText(postModel.getCommentCount()+"");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users")
                .child(postedBy).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        Glide.with(getApplicationContext()).load(users.getProfileImage())
                                .placeholder(R.drawable.plc_person)
                                .into(binding.profileImage);
                        binding.name.setText(users.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Comment comment = new Comment();
                comment.setCommentBody(binding.commentEt.getText().toString());
                comment.setCommentedAt(new Date().getTime());
                comment.setCommentedBy(FirebaseAuth.getInstance().getUid());

                database.getReference()
                        .child("posts")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                database.getReference()
                                        .child("posts")
                                        .child(postId)
                                        .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int commentCount = 0;
                                                if (snapshot.exists()) {

                                                    commentCount = snapshot.getValue(Integer.class);
                                                }
                                                database.getReference()
                                                        .child("posts")
                                                        .child(postId)
                                                        .child("commentCount")
                                                        .setValue(commentCount + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                binding.commentEt.setText("");
                                                                Toast.makeText(Comment_Activity.this, "Commented", Toast.LENGTH_SHORT).show();


                                                                Notification notification = new Notification();
                                                                notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                notification.setNotificationAt(new Date().getTime());
                                                                notification.setPostId(postId);
                                                                notification.setPostedBy(postedBy);
                                                                notification.setType("Comment");

                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("Notification")
                                                                        .child(postedBy)
                                                                        .push()
                                                                        .setValue(notification);

                                                            }
                                                        });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }
                        });

            }
        });

       CommentAdapter adapter = new CommentAdapter(list, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.commentRv.setLayoutManager(layoutManager);
        binding.commentRv.setAdapter(adapter);

        database.getReference().child("posts")
                .child(postId)
                .child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                           Comment comment = dataSnapshot.getValue(Comment.class);
                            list.add(comment);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}