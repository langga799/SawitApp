package com.example.cobaaan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        findViewById(R.id.imageView).setOnClickListener( v->{
            startActivity(new Intent(this, DashboardActivity.class));
        });

        reference = FirebaseDatabase.getInstance().getReference();


        progressBar = findViewById(R.id.progressBar);
        mEmail = findViewById(R.id.Email);
        fAuth = FirebaseAuth.getInstance();
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), NextloginActivity.class));
                            String email = String.valueOf(Objects.requireNonNull(task.getResult().getUser()).getEmail());

//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            AuthCredential credential = EmailAuthProvider.getCredential(email, String.valueOf(mPassword));
//                            assert user != null;
//                            user.reauthenticate(credential)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                        }
//                                    });

                            Log.d("PERSON", String.valueOf(Objects.requireNonNull(task.getResult().getUser()).getEmail()));

//                            HashMap<String, String> person = new HashMap<>();
//                            person.put("nama", "");
//                            person.put("tanggal_lahir", "");
//                            person.put("alamat", "");
//                            person.put("email", String.valueOf(Objects.requireNonNull(task.getResult().getUser()).getEmail()));
//                            person.put("password", "");
//
//                            reference.child("person").child(Objects.requireNonNull(fAuth.getCurrentUser()).getUid())
//                                            .setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                            startActivity(new Intent(getApplicationContext(), NextloginActivity.class));
//                                        }
//                                    });



                        } else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });



//        mLoginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openNextLogin();
//            }
//        });
    }
//    public void openNextLogin(){
//        Intent buka = new Intent(this, NextloginActivity.class);
//        startActivity(buka);
//    }
}