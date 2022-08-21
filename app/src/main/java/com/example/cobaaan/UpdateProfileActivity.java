package com.example.cobaaan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText namaUpdate, alamatUpdate, tanggalUpdate, emailUpdate, passwordUpdate;
    FirebaseAuth auth;
    DatabaseReference reference;

    MaterialButton btnUpdateEmail, btnUpdatePassword;
    Button btnUpdateProfilInfo, btnDateUpdateProfil;

    String sigEmail, sigPassword;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateprofile);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        namaUpdate = findViewById(R.id.namaUpdate);
        tanggalUpdate = findViewById(R.id.tanggalLahirUpdate);
        alamatUpdate = findViewById(R.id.alamat_update);
        emailUpdate = findViewById(R.id.email_update);
        passwordUpdate = findViewById(R.id.password_update);
        btnUpdateEmail = findViewById(R.id.btnUpdateEmail);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        btnUpdateProfilInfo = findViewById(R.id.btnUpdateProfilInfo);
        btnDateUpdateProfil = findViewById(R.id.btnDateupdateprofil);


        Intent intent = getIntent();
        String nama = intent.getStringExtra("DATA_NAMA");
        String tanggal = intent.getStringExtra("DATA_TANGGAL");
        String alamat = intent.getStringExtra("DATA_ALAMAT");
        String email = intent.getStringExtra("DATA_EMAIL");
        String password = intent.getStringExtra("DATA_PASSWORD");


        namaUpdate.setText(nama);
        tanggalUpdate.setText(tanggal);
        alamatUpdate.setText(alamat);
        emailUpdate.setText(email);
        passwordUpdate.setText(password);


        sigEmail = email;
        sigPassword = password;


        btnDateUpdateProfil.setOnClickListener(v ->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    calendar.set(year, month, dayOfMonth);
                    Locale id = new Locale("in", "ID");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
                    tanggalUpdate.setText(simpleDateFormat.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });


        btnUpdateEmail.setOnClickListener(v -> {
            Objects.requireNonNull(auth.getCurrentUser()).reauthenticate(EmailAuthProvider.getCredential(sigEmail, sigPassword))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Objects.requireNonNull(auth.getCurrentUser()).updateEmail(emailUpdate.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User email address updated.");
                                                btnUpdateEmail.setText("Berhasil");
                                                sigEmail = emailUpdate.getText().toString();
                                            }
                                        }
                                    });
                        }
                    });
        });


        btnUpdatePassword.setOnClickListener(v -> {
            Objects.requireNonNull(auth.getCurrentUser()).reauthenticate(EmailAuthProvider.getCredential(sigEmail, sigPassword))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Objects.requireNonNull(auth.getCurrentUser()).updatePassword(passwordUpdate.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User password updated.");
                                                btnUpdatePassword.setText("Berhasil");
                                                sigPassword = passwordUpdate.getText().toString();
                                            }
                                        }
                                    });
                        }
                    });
        });


        btnUpdateProfilInfo.setOnClickListener(v -> {

            HashMap<String, String> newProfile = new HashMap<>();
            newProfile.put("nama", namaUpdate.getText().toString());
            newProfile.put("tanggal_lahir", tanggalUpdate.getText().toString());
            newProfile.put("alamat", alamatUpdate.getText().toString());
            newProfile.put("email", emailUpdate.getText().toString());
            newProfile.put("password", passwordUpdate.getText().toString());

            reference.child("person").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(newProfile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UpdateProfileActivity.this, "Profil Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateProfileActivity.this, ViewprofilActivity.class));
                        }
                    });
        });

    }
}