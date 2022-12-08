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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.softaloy.lossbook.databinding.ActivitySingInBinding;


public class SingIn_Activity extends AppCompatActivity {


    ActivitySingInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySingInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        progressDialog = new ProgressDialog(SingIn_Activity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login to Your Account");





        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtEmail.getText().toString().isEmpty()){
                    binding.edtEmail.setError("Enter your email");

                    return;
                }
                if (binding.edtPassword.getText().toString().isEmpty()){
                    binding.edtPassword.setError("Enter your password");
                    return;
                }

                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressDialog.dismiss();

                                if (task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }else {
                                    Toast.makeText(SingIn_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
        binding.createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingIn_Activity.this, SingUpActivity.class));
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser != null){
            startActivity(new Intent(SingIn_Activity.this, MainActivity.class));
            finish();
        }

    }
}