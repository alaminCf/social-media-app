package com.softaloy.lossbook.Fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.softaloy.lossbook.Adapter.Followers_Adapter;
import com.softaloy.lossbook.Model.FollowModel;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.R;
import com.softaloy.lossbook.SingIn_Activity;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile_Fragment extends Fragment {


    public Profile_Fragment() {
        // Required empty public constructor
    }

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;

    StorageReference storageReferenceCover;
    StorageReference storageReferenceProfile;

    RecyclerView recyclerView;
    ArrayList<FollowModel> list;
    Toolbar toolbar;

    ImageView coverImage, profileImage, camCover, camProfile;
    TextView username, useremail, followers;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewf = inflater.inflate(R.layout.fragment_profile_, container, false);


        database = FirebaseDatabase.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();

        storageReferenceProfile = FirebaseStorage.getInstance().getReference("profile_Uploads").child("Profile_Image");
        storageReferenceCover = FirebaseStorage.getInstance().getReference("Cover_Uploads").child("Cover_image");

        coverImage = viewf.findViewById(R.id.coverimage);
        profileImage = viewf.findViewById(R.id.profile_image);
        camCover = viewf.findViewById(R.id.camara_cover);
        camProfile = viewf.findViewById(R.id.camara_profile);
        username = viewf.findViewById(R.id.username);
        useremail = viewf.findViewById(R.id.useremail);
        followers = viewf.findViewById(R.id.followers);


        toolbar = viewf.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("My Profile");

        recyclerView = viewf.findViewById(R.id.friend_rv);

        list = new ArrayList<>();



        Followers_Adapter adapter = new Followers_Adapter(list, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        database.getReference()
                        .child("Users")
                                .child(auth.getUid()).child("followers")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                                    FollowModel followModel = dataSnapshot.getValue(FollowModel.class);
                                    list.add(followModel);

                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


        /////////upload imageeeee//////////////

        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid())

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Users users = snapshot.getValue(Users.class);
                        username.setText(users.getName());
                        useremail.setText(users.getEmail());


                        Glide.with(Profile_Fragment.this).load(users.getProfileImage()).placeholder(R.drawable.plc_person).into(profileImage);
                        Glide.with(Profile_Fragment.this).load(users.getCoverImage()).placeholder(R.drawable.chocolate).into(coverImage);

                        followers.setText(users.getFollowerCount()+"");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        camCover.setOnClickListener(view -> ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start(101));


        camProfile.setOnClickListener(view -> {

            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start(102);

        });




        return viewf;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101 && resultCode==RESULT_OK && data != null){

            Uri coverUri = data.getData();
            coverImage.setImageURI(coverUri);

            uploadCoverImage(coverUri);

        }

        if (requestCode==102 && resultCode==RESULT_OK && data !=null){

            Uri profileUri = data.getData();
            profileImage.setImageURI(profileUri);

            uploadProfileImage(profileUri);

        }

    }

    private void uploadProfileImage(Uri profileUri) {

        StorageReference pRef = storageReferenceProfile.child("p_img"+firebaseUser.getUid());
        pRef.putFile(profileUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                pRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    String profileUrl = String.valueOf(uri);


                    HashMap<String, Object> profileMap = new HashMap<>();
                    profileMap.put("profileImage", profileUrl);

                    FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid())
                            .updateChildren(profileMap);
                });
            }
        });

    }

    private void uploadCoverImage(Uri coverUri) {

        StorageReference cRef = storageReferenceCover.child("c_img"+firebaseUser.getUid());
        cRef.putFile(coverUri).addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                cRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    String imageUrl = String.valueOf(uri);

                    HashMap<String, Object> coverMap = new HashMap<>();
                    coverMap.put("coverImage", imageUrl);

                    FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid())
                            .updateChildren(coverMap);

                });
            }

        });


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_profile,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        
        int id = item.getItemId();
        if (id == R.id.logoutMenu) {

            Singout();
            Toast.makeText(getActivity(), "Menu selected", Toast.LENGTH_SHORT).show();


        }

        if (id == R.id.myProfileMenu) {

            Toast.makeText(getActivity(), "Menu selected", Toast.LENGTH_SHORT).show();
           // startActivity(new Intent(getContext(), SingIn_Activity.class));

        }
        
        return super.onOptionsItemSelected(item);
    }
    private void Singout() {

        auth = FirebaseAuth.getInstance();
        auth.signOut();
        startActivity(new Intent(getContext(), SingIn_Activity.class));
    }

}