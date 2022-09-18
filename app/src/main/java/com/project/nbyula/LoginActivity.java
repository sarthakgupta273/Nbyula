package com.project.nbyula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView textView;
    EditText email,password;
    Button login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = findViewById(R.id.textView);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        firebaseAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = email.getText().toString();
                String pass = password.getText().toString();

                if(emailId.isEmpty()){
                    email.setError("Please Enter Email");
                    email.requestFocus();
                }
                else if(pass.isEmpty()){
                    password.setError("Please Enter Password");
                    password.requestFocus();
                }
                else if(emailId.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please Enter E-mail & Password", Toast.LENGTH_SHORT).show();
                }
                else if(!(emailId.isEmpty() && pass.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(emailId, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    firebaseAuth.signInWithEmailAndPassword(emailId, pass).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "No such user is Registered!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "Some thing is wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, BookingActivity.class));
            finish();
        }
    }
}