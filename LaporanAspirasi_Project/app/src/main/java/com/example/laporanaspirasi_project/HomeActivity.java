package com.example.laporanaspirasi_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button makeReportButton;
    private ImageButton bookImageButton;
    private ImageButton homeImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        makeReportButton = findViewById(R.id.btn_makereport);
        bookImageButton = findViewById(R.id.book);
        homeImageButton = findViewById(R.id.home); // Inisialisasi ImageButton Home

        makeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ke halaman ReportActivity
                Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        bookImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ke halaman InformationActivity
                Intent intent = new Intent(HomeActivity.this, InformationActivity.class);
                startActivity(intent);
            }
        });

        homeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // menampilkan pesan jika pengguna berada di halaman Home
                Toast.makeText(HomeActivity.this, "Anda sudah berada di halaman Home", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
