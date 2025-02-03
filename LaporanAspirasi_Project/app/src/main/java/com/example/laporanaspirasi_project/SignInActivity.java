package com.example.laporanaspirasi_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button signUpButton;

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        emailEditText = findViewById(R.id.email_signin);
        passwordEditText = findViewById(R.id.password_signin);
        signInButton = findViewById(R.id.btn_signin);
        signUpButton = findViewById(R.id.btn_signuplogin);

        sharedPrefManager = new SharedPrefManager(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    // ambil data dari SharedPreferences
                    String storedEmail = sharedPrefManager.getEmail();
                    String storedPassword = sharedPrefManager.getPassword();

                    if (storedEmail != null && storedPassword != null) {
                        if (email.equals(storedEmail) && password.equals(storedPassword)) {
                            // Login berhasil
                            Toast.makeText(SignInActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                            //  ke halaman Home
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish(); // Akhiri activity agar tidak kembali ke halaman login
                        } else {
                            // jika email atau password salah
                            Toast.makeText(SignInActivity.this, "Email atau Password salah", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // jika belum ada user yang terdaftar
                        Toast.makeText(SignInActivity.this, "Belum ada akun terdaftar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //  untuk tombol Sign Up
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ke halaman Sign Up
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
