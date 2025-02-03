package com.example.laporanaspirasi_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laporanaspirasi_project.ApiClient;
import com.example.laporanaspirasi_project.ApiService;
import com.example.laporanaspirasi_project.R;
import com.example.laporanaspirasi_project.ResultActivity;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private EditText nameEditText, crimeEditText, tkpEditText, notesEditText;
    private Button reportButton, selectImageButton;
    private ImageView imagePreview;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);

        nameEditText = findViewById(R.id.fullname);
        crimeEditText = findViewById(R.id.suspect);
        tkpEditText = findViewById(R.id.location);
        notesEditText = findViewById(R.id.report);
        reportButton = findViewById(R.id.btn_send);
        selectImageButton = findViewById(R.id.btn_select_image);
        imagePreview = findViewById(R.id.image_preview);

        selectImageButton.setOnClickListener(v -> openGallery());
        reportButton.setOnClickListener(v -> saveToApi());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imagePreview.setImageURI(selectedImageUri);  // Set the image in ImageView
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String fileName = "Unknown";
        if (uri != null) {
            try {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    private MultipartBody.Part prepareFilePart(Uri uri) {
        String realPath = getRealPathFromURI(uri);
        if (realPath != null) {
            File imageFile = new File(realPath);

            // Validate file size
            if (imageFile.length() > MAX_FILE_SIZE) {
                showToast("Image size must not exceed 5MB.");
                return null;
            }

            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            return MultipartBody.Part.createFormData("file", imageFile.getName(), fileBody);
        }
        showToast("Failed to process the image.");
        return null;
    }

    @SuppressLint("Range")
    private String getRealPathFromURI(Uri uri) {
        String filePath = null;
        if (uri != null) {
            try {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    File tempFile = new File(getCacheDir(), fileName);
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        FileOutputStream outputStream = new FileOutputStream(tempFile);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        inputStream.close();
                        outputStream.close();
                        filePath = tempFile.getAbsolutePath();
                    }
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private void saveToApi() {
        String name = nameEditText.getText().toString().trim();
        String crime = crimeEditText.getText().toString().trim();
        String tkp = tkpEditText.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();

        if (name.isEmpty() || crime.isEmpty() || tkp.isEmpty() || notes.isEmpty() || selectedImageUri == null) {
            showToast("Please fill all fields and select an image!");
            return;
        }

        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody crimeBody = RequestBody.create(MediaType.parse("text/plain"), crime);
        RequestBody tkpBody = RequestBody.create(MediaType.parse("text/plain"), tkp);
        RequestBody notesBody = RequestBody.create(MediaType.parse("text/plain"), notes);
        MultipartBody.Part imagePart = prepareFilePart(selectedImageUri);

        if (imagePart != null) {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<JsonObject> call = apiService.submitReport(nameBody, crimeBody, tkpBody, notesBody, imagePart);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        showToast("Report submitted successfully!");
                        Intent intent = new Intent(ReportActivity.this, ResultActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorBody = response.errorBody() != null ? response.errorBody().toString() : "Unknown error";
                        Log.e("ResponseError", "Code: " + response.code() + ", Error: " + errorBody);
                        showToast("Failed to submit report. Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("RequestFailure", t.getMessage() != null ? t.getMessage() : "Unknown error");
                    showToast("Error: " + (t.getMessage() != null ? t.getMessage() : "Unknown error"));
                }
            });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
