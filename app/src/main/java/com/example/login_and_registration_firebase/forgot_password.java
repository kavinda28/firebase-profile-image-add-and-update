package com.example.login_and_registration_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {
EditText ed_email;
Button rest_password;
ProgressBar progressBar;

FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ed_email = findViewById(R.id.ed_email);
        rest_password = findViewById(R.id.bt_Reset);
        progressBar = findViewById(R.id.progressBar3);

        auth = FirebaseAuth.getInstance();

        rest_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = ed_email.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          Toast.makeText(forgot_password.this,"Check your email to rest your password!",Toast.LENGTH_LONG).show();
                          progressBar.setVisibility(View.GONE);
                      }else {
                          Toast.makeText(forgot_password.this,"Try again! Something wrong happened!",Toast.LENGTH_LONG).show();
                          progressBar.setVisibility(View.GONE);
                      }
            }
        });

    }
}
