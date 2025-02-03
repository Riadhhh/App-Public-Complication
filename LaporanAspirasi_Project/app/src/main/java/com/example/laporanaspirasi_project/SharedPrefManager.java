package com.example.laporanaspirasi_project;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String PREF_NAME = "UserPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Simpan data pengguna
    public void saveUser(String username, String email, String password) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    // Ambil username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    // Ambil email
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Ambil password
    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    // Hapus data pengguna (Logout)
    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}
