package com.example.cobaaan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ViewprofilActivity extends AppCompatActivity {
    Button updateprofil, kembali, hapusData;
    ImageButton tambahdata, hapusdata;
    DatabaseReference reference;
    FirebaseAuth auth;

    TextView nama, tanggalLahir, alamat, email, password;

    String namaData= "", tanggalData = "", alamatData = "", emailData = "", passwordData = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewprofil);

        updateprofil = findViewById(R.id.updateprofile);
        kembali = findViewById(R.id.Kembali);
        tambahdata = findViewById(R.id.tambahdata);
        hapusdata = findViewById(R.id.hapusdata);

        nama = findViewById(R.id.namaLengkapProfile);
        tanggalLahir = findViewById(R.id.tanggalLahirProfile);
        alamat = findViewById(R.id.alamatProfile);
        email = findViewById(R.id.emailProfile);
        password = findViewById(R.id.passwordProfile);
        hapusdata = findViewById(R.id.hapusdata);

        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();
        // Get User Profile
        reference.child("person").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //   Log.d("DATA", snapshot.getValue().toString());

                        if (namaData == null){
                            namaData = "";
                        } else  {
                            namaData = (String) snapshot.child("nama").getValue();
                        }

                        if (tanggalData == null){
                            tanggalData = "";
                        } else {
                            tanggalData = (String)snapshot.child("tanggal_lahir").getValue();
                        }
                        if (alamat == null){
                            alamatData = "";
                        } else {
                            alamatData = (String) snapshot.child("alamat").getValue();
                        }

                        if (emailData == null){
                            emailData = "";
                        } else {
                            emailData = (String) snapshot.child("email").getValue();
                        }

                        if (passwordData == null){
                             passwordData = "";
                        } else  {
                            passwordData =(String) snapshot.child("password").getValue();
                        }






                        nama.setText(namaData);
                        tanggalLahir.setText(tanggalData);
                        alamat.setText(alamatData);
                        email.setText(emailData);
                        password.setText(passwordData);

                        updateprofil.setOnClickListener(v -> {
                            startActivity(new Intent(ViewprofilActivity.this, UpdateProfileActivity.class)
                                    .putExtra("DATA_NAMA", namaData)
                                    .putExtra("DATA_TANGGAL", tanggalData)
                                    .putExtra("DATA_ALAMAT", alamatData)
                                    .putExtra("DATA_EMAIL", emailData)
                                    .putExtra("DATA_PASSWORD", passwordData)
                            );
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        tambahdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewprofilActivity.this, TambahprofilActivity.class));
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewprofilActivity.this, DashboardActivity.class));
            }
        });


        hapusdata.setOnClickListener(v -> {
            String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            String email = auth.getCurrentUser().getEmail();
            Log.d("aa", "runn");
            Log.d("uui", uid);
            Log.d("uuemmmi", email);

            try {
                reference.child("person").child(uid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        auth.getCurrentUser().delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ViewprofilActivity.this, "Hapus data berhasil", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ViewprofilActivity.this, LoginActivity.class));
                                        finishAffinity();
                                    }
                                });
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }

//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Log.d("SSSS", snapshot.getRef().toString());
//
//                           Log.d("UUU",  snapshot.getRef().orderByChild("email").equalTo(email).toString());
//                            for (DataSnapshot delete : snapshot.getChildren()){
//
//                                delete.getRef().removeValue();
//
//                               auth.getCurrentUser().delete()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                Toast.makeText(ViewprofilActivity.this, "Hapus data berhasil", Toast.LENGTH_SHORT).show();
//                                                startActivity(new Intent(ViewprofilActivity.this, LoginActivity.class));
//                                            }
//                                        });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });


        });


    }
}