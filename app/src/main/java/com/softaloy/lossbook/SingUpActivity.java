package com.softaloy.lossbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.softaloy.lossbook.Model.Users;
import com.softaloy.lossbook.databinding.ActivitySingUpBinding;

public class SingUpActivity extends AppCompatActivity {


    ActivitySingUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        progressDialog = new ProgressDialog(SingUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We'r Creating Your Account");


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                auth.createUserWithEmailAndPassword
                                (binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();
                                if (task.isSuccessful()){

                                    Users users = new Users(binding.edtUsername.getText().toString(), binding.edtEmail.getText().toString(),
                                            binding.edtPassword.getText().toString());

                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(users);

                                    startActivity(new Intent(SingUpActivity.this, SingIn_Activity.class));
                                    Toast.makeText(SingUpActivity.this, "User Created Successful", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SingUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });

        binding.havenAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingUpActivity.this, SingIn_Activity.class));
            }
        });



    }

}