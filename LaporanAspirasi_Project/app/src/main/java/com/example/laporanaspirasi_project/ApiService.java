package com.example.laporanaspirasi_project;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("save_report.php")
    Call<JsonObject> submitReport(
            @Part("name") RequestBody name,
            @Part("crime") RequestBody crime,
            @Part("tkp") RequestBody tkp,
            @Part("notes") RequestBody notes,
            @Part MultipartBody.Part file
    );
}

