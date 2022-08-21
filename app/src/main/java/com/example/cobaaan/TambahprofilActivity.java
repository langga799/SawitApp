package com.example.cobaaan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;


public class TambahprofilActivity extends AppCompatActivity {

    EditText fullname_input, username_input, password_input, alamatInput, tanggalInput;
    Button simpanprofile, btnDateNewProfile;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    DatabaseReference reference;
    Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambahprofil);

        fullname_input = findViewById(R.id.fullname_input);
        tanggalInput = findViewById(R.id.tgl_profil_input);
        alamatInput = findViewById(R.id.alamat_input);
        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);
        btnDateNewProfile = findViewById(R.id.btnDateNewProfil);
        simpanprofile = findViewById(R.id.btnSimpanDataUserBaru);


        reference = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();


        btnDateNewProfile.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    calendar.set(year, month, dayOfMonth);
                    Locale id = new Locale("in", "ID");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
                    tanggalInput.setText(simpleDateFormat.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        simpanprofile.setOnClickListener(v -> {

            if (fullname_input.getText().toString().isEmpty()){
                fullname_input.setError("Nama tidak boleh kosong");
                fullname_input.requestFocus();
            } else  if (tanggalInput.getText().toString().isEmpty()){
                tanggalInput.setError("Tanggal tidak boleh kosong");
                tanggalInput.requestFocus();
            } else if (alamatInput.getText().toString().isEmpty()){
                tanggalInput.setError("Alamat tidak boleh kosong");
                tanggalInput.requestFocus();
            } else if(username_input.getText().toString().isEmpty()){
                username_input.setError("Email tidak boleh kosong");
                username_input.requestFocus();
            } else if (password_input.getText().toString().isEmpty()){
                password_input.setError("Password harus diisi");
                password_input.requestFocus();
            } else if (password_input.getText().toString().length() < 6){
                password_input.setError("Password setidaknya berisi 6 karakter");
            } else {
                fAuth.createUserWithEmailAndPassword(username_input.getText().toString(), password_input.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                HashMap<String, String> newPerson = new HashMap<>();
                                newPerson.put("nama", fullname_input.getText().toString());
                                newPerson.put("tanggal_lahir",tanggalInput.getText().toString());
                                newPerson.put("alamat", alamatInput.getText().toString());
                                newPerson.put("email", username_input.getText().toString());
                                newPerson.put("password", password_input.getText().toString());

                                reference.child("person").child(Objects.requireNonNull(task.getResult().getUser()).getUid()).setValue(newPerson)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(TambahprofilActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(TambahprofilActivity.this, LoginActivity.class));
                                                finishAffinity();
                                            }
                                        });
                            }
                        });
            }


        });



    }
}


