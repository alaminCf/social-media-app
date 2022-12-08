package com.softaloy.lossbook.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appbroker.roundedimageview.RoundedImageView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softaloy.lossbook.Adapter.Post_Adapter;
import com.softaloy.lossbook.Adapter.StoryAdapter;
import com.softaloy.lossbook.Model.Post_model;
import com.softaloy.lossbook.Model.Story;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.Model.Users_Story;
import com.softaloy.lossbook.R;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Fragment extends Fragment {


    RecyclerView story_rv, dashboard_rv;
    ArrayList<Story> list;
    ArrayList<Post_model> post_models;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    RoundedImageView addStoryImg;
    CircleImageView ppimg;
    ActivityResultLauncher<String> gallarylauncher;
    ProgressDialog dialog;


    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getContext());

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        story_rv = view.findViewById(R.id.story_rv);
        addStoryImg = view.findViewById(R.id.addStoryImg);
        ppimg = view.findViewById(R.id.tprofile_img);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Story Uploading");
        dialog.setMessage("Please Wait......");
        dialog.setCancelable(false);

        list = new ArrayList<>();

        StoryAdapter adapter = new StoryAdapter(list, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        story_rv.setLayoutManager(linearLayoutManager);
        story_rv.setNestedScrollingEnabled(false);
        story_rv.setAdapter(adapter);

        database.getReference()
                .child("Stories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot storySnapshot : snapshot.getChildren()){
                                Story story = new Story();
                                story.setStoryBy(storySnapshot.getKey());
                                story.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<Users_Story> users_stories = new ArrayList<>();

                                for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()){

                                    Users_Story usersStory = snapshot1.getValue(Users_Story.class);
                                    users_stories.add(usersStory);

                                }
                                story.setStories(users_stories);
                                list.add(story);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        dashboard_rv = view.findViewById(R.id.dashboard_rv);
        post_models = new ArrayList<>();


        Post_Adapter post_adapter = new Post_Adapter(post_models, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        dashboard_rv.setLayoutManager(layoutManager);
        dashboard_rv.setNestedScrollingEnabled(false);
        dashboard_rv.setAdapter(post_adapter);

        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post_models.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post_model model = dataSnapshot.getValue(Post_model.class);
                    model.setPostId(dataSnapshot.getKey());
                    post_models.add(model);
                }
                post_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addStoryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallarylauncher.launch("image/*");
            }
        });

        gallarylauncher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        addStoryImg.setImageURI(result);

                        dialog.show();
                        final StorageReference reference = storage.getReference()
                                .child("Stories")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(new Date().getTime() + "");
                        reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Story story = new Story();
                                        story.setStoryAt(new Date().getTime());

                                        database.getReference()
                                                .child("Stories")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("postedBy")
                                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        Users_Story users_story = new Users_Story(uri.toString(), story.getStoryAt());

                                                        database.getReference()
                                                                .child("Stories")
                                                                .child(FirebaseAuth.getInstance().getUid())
                                                                .child("userStories")
                                                                .push()
                                                                .setValue(users_story).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    }
                });


        database.getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);

                            Glide.with(getContext()).load(users.getProfileImage())
                                    .placeholder(R.drawable.plc_person)
                                    .into(ppimg);


                            Glide.with(getContext()).load(users.getProfileImage())
                                    .placeholder(R.drawable.plc_person)
                                    .into(addStoryImg);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }
}