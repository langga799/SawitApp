package com.example.cobaaan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class NextloginActivity extends AppCompatActivity {
//    imageView8
private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextlogin);

        imageButton = (ImageButton)  findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selanjutnya();
            }
        });
    }
    public void selanjutnya(){
        Intent buka = new Intent(this, DashboardActivity.class);
        startActivity(buka);
    }
}