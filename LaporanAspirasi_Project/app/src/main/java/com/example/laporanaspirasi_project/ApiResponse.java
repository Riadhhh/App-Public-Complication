package com.example.laporanaspirasi_project;

public class ApiResponse {
    private String status;
    private ReportData data;

    // Constructor tanpa argumen (default constructor) - diperlukan untuk serialisasi/deserialisasi
    public ApiResponse() {
    }

    // Constructor dengan argumen
    public ApiResponse(String status, ReportData data) {
        this.status = status;
        this.data = data;
    }

    // Getter untuk status
    public String getStatus() {
        return status;
    }

    // Setter untuk status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter untuk data
    public ReportData getData() {
        return data;
    }

    // Setter untuk data
    public void setData(ReportData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
