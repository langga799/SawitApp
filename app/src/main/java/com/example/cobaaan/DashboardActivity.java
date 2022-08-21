package com.example.cobaaan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

        CardView imagetruck,imageprofil, btnlaporan;
        ImageView iconlogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        iconlogout = findViewById(R.id.iconlogout);
        imageprofil = findViewById(R.id.imageprofil);
        imagetruck =  findViewById(R.id.imagetruck);
        btnlaporan = findViewById(R.id.btnLaporan);

        imagetruck.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
//                startActivity( new Intent(DashboardActivity.this,ProduksiActivity.class));
                startActivity( new Intent(DashboardActivity.this,MainActivity.class));
            }
        });


        imageprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(DashboardActivity.this,ViewprofilActivity.class));
            }
        });


        btnlaporan.setOnClickListener(v -> {
            startActivity(new Intent(this, ProduksiActivity.class));
        });



        iconlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });




    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }



}