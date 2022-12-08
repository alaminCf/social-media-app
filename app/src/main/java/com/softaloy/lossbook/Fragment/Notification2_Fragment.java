package com.softaloy.lossbook.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softaloy.lossbook.Adapter.Notification_Adapter;
import com.softaloy.lossbook.Model.Notification;
import com.softaloy.lossbook.R;

import java.util.ArrayList;


public class Notification2_Fragment extends Fragment {


    RecyclerView recyclerView;
    ArrayList<Notification> list;
    FirebaseDatabase database;



    public Notification2_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_notification2_, container, false);

      recyclerView = view.findViewById(R.id.notificition2_rv);

      list = new ArrayList<>();

      /*
      list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
              "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        list.add(new Notification(R.drawable.proyojon, "<b>Alamin</b> This is new notify and please check it",
                "just now"));
        */

        Notification_Adapter adapter = new Notification_Adapter(list, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        database.getReference()
                .child("Notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            notification.setNotificationId(dataSnapshot.getKey());
                            list.add(notification);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


      return view;
    }
}