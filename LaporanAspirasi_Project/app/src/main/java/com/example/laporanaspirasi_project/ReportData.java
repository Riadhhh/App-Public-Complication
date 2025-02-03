package com.example.laporanaspirasi_project;

public class ReportData {
    private String name;
    private String crime;
    private String tkp;
    private String notes;

    public ReportData() {
    }

    public ReportData(String name, String crime, String tkp, String notes) {
        this.name = name;
        this.crime = crime;
        this.tkp = tkp;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    // Setter untuk name
    public void setName(String name) {
        this.name = name;
    }

    // Getter untuk crime
    public String getCrime() {
        return crime;
    }

    // Setter untuk crime
    public void setCrime(String crime) {
        this.crime = crime;
    }

    // Getter untuk tkp
    public String getTkp() {
        return tkp;
    }

    // Setter untuk tkp
    public void setTkp(String tkp) {
        this.tkp = tkp;
    }

    // Getter untuk notes
    public String getNotes() {
        return notes;
    }

    // Setter untuk notes
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "ReportData{" +
                "name='" + name + '\'' +
                ", crime='" + crime + '\'' +
                ", tkp='" + tkp + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
