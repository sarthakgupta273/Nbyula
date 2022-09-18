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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText fullName, email, password, confPassword, phoneNo;
    Button signUp;
    TextView textView;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.name);
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.confPassword);
        phoneNo = findViewById(R.id.phone);
        signUp = findViewById(R.id.signup);
        textView = findViewById(R.id.textView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = email.getText().toString();
                String pass = password.getText().toString();
                String confPass = confPassword.getText().toString();
                String name = fullName.getText().toString();
                String phone = phoneNo.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                if (emailId.isEmpty()) {
                    email.setError("Please Enter Email");
                    email.requestFocus();
                }  else if (!(emailId.trim().matches(emailPattern))) {
                    email.setError("Enter in For of user@gmail.com");
                    email.requestFocus();
                } else if (pass.isEmpty()) {
                    password.setError("Please Enter Password");
                    password.requestFocus();
                } else if (password.getText().length() < 6) {
                    password.setError("Invalid Password!");
                    password.requestFocus();
                } else if (confPass.isEmpty()) {
                    confPassword.setError("Please Enter Conform Password");
                    confPassword.requestFocus();
                } else if (name.isEmpty()) {
                    fullName.setError("Please Enter Name");
                    fullName.requestFocus();
                }  else if (phone.isEmpty()) {
                    phoneNo.setError("Please Enter Phone Number");
                    phoneNo.requestFocus();
                } else if (emailId.isEmpty() && pass.isEmpty() && phone.isEmpty() && confPass.isEmpty() && name.isEmpty() ) {
                    Toast.makeText(SignUpActivity.this, "Please Fill All Details!", Toast.LENGTH_SHORT).show();
                }
                else if(!(emailId.isEmpty() && pass.isEmpty() && name.isEmpty() && phone.isEmpty() && confPass.isEmpty())){
                    registerUser(name, phone, emailId, pass);
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Some thing is wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            private void registerUser(String fname, String phoneno, String emailid, String passWord) {
                firebaseAuth.createUserWithEmailAndPassword(emailid,passWord).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        HashMap<String,String> hashMap = new HashMap <>();

                        hashMap.put("EMAIL", emailid);
                        hashMap.put("NAME", fname);
                        hashMap.put("PHONE", phoneno);
                        hashMap.put("PASSWORD", passWord);
                        hashMap.put("ID", firebaseAuth.getCurrentUser().getUid());

                        databaseReference.child("USER").child(firebaseAuth.getCurrentUser().getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener< Void >() {
                            @Override
                            public void onComplete(@NonNull Task< Void > task) {
                                if(!task.isSuccessful()){
                                    email.setError("User Already Exist");
                                    email.requestFocus();}
                                else {
                                    Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}