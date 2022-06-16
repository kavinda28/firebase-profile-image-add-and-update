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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button login;
    EditText ed_email, ed_password;
    TextView register, forgot_password;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //full screen window open

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.bt_login);
        login.setOnClickListener(this);

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this);

         progressBar = findViewById(R.id.progressBar2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this,registration.class));
                break;
            case R.id.bt_login:
                login_user();
                break;
            case R.id.forgot_password:
                startActivity(new Intent(this,forgot_password.class));
                break;
        }
    }

    private void login_user() {
        String email = ed_email.getText().toString().trim();
        String password = ed_password.getText().toString().trim();



        if (email.isEmpty()){
            ed_email.setError("Email is required!");
            ed_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ed_email.setError("Please provide valid Email");
            ed_email.requestFocus();
            return;
        }


        if (password.isEmpty()){
            ed_password.setError("Password is required!");
            ed_password.requestFocus();
            return;
        }
        if (password.length() < 6 ){
            ed_password.setError("Password length should be 6 Characters!");
            ed_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()){
                     startActivity(new Intent(MainActivity.this,profile.class));
                     progressBar.setVisibility(View.GONE);
                 }else {
                     Toast.makeText(MainActivity.this,"Failed to login! Please check your credentials",Toast.LENGTH_LONG).show();
                     progressBar.setVisibility(View.GONE);
                 }
            }
        });

    }
}
