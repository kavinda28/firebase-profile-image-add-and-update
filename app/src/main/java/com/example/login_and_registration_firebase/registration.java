package com.example.login_and_registration_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class registration extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    EditText ed_fullname,ed_Email,ed_password,ed_conform_password;
    Button register_user;
    ProgressBar progressBar;
    TextView have_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen window open

        mAuth = FirebaseAuth.getInstance();

        ed_fullname = findViewById(R.id.ed_full_name);
        ed_Email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_conform_password = findViewById(R.id.ed_conform_password);

        register_user = findViewById(R.id.bt_Register);
        register_user.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        have_account = findViewById(R.id.already_account);
        have_account.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_Register:
                registerUser();
                break;
            case R.id.already_account:
                startActivity(new Intent(registration.this,MainActivity.class));
                break;   
                
        }
        
    }

    private void registerUser() {
        String fullname = ed_fullname.getText().toString().trim();
        String email = ed_Email.getText().toString().trim();
        String password = ed_password.getText().toString().trim();
        String conform_password = ed_conform_password.getText().toString().trim();

        if (fullname.isEmpty()){
           ed_fullname.setError("Full name is required!");
           ed_fullname.requestFocus();
           return;
        }


        if (email.isEmpty()){
            ed_Email.setError("Email is required!");
            ed_Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ed_Email.setError("Please provide valid Email");
            ed_Email.requestFocus();
            return;
        }


        if (password.isEmpty()){
            ed_password.setError("Password is required!");
            ed_password.requestFocus();
            return;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
        {
            ed_password.setError("Password must have atleast one uppercase character!");
            ed_password.requestFocus();
            return;
        }
        if (password.length() < 6 ){
            ed_password.setError("Password length should be 6 Characters!");
            ed_password.requestFocus();
            return;
        }
        if (conform_password.isEmpty()){
            ed_conform_password.setError("Conform Password is required!");
            ed_conform_password.requestFocus();
            return;
        }
        if(!password.equals(conform_password))
            {
                ed_conform_password.setError("Password Not matching!");
                ed_conform_password.requestFocus();
                return;
            }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                       User user=new User(fullname,email,password);

                     FirebaseDatabase.getInstance().getReference("Users")
                             .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                             .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){
                                 Toast.makeText(registration.this,"user has been Registered successfully!",Toast.LENGTH_LONG).show();
                                 progressBar.setVisibility(View.GONE);
                             }else {
                                 Toast.makeText(registration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                 progressBar.setVisibility(View.GONE);
                             }
                         }
                     });

                     }else {
                         Toast.makeText(registration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                         progressBar.setVisibility(View.GONE);
                     }
                 }
             });

    }






}
