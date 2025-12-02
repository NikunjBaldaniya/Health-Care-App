package com.example.healthcareapp.Model;

import android.graphics.Bitmap;

public class DoctorListModel {
    public int doctorId;
    public Bitmap imgDoctor;
    public String txtDoctorName;
    public String txtDoctorSpeciality;
    public Double txtRating;

    public DoctorListModel(int doctorId, Bitmap imgDoctor, String txtDoctorName, String txtDoctorSpeciality, Double txtRating) {
        this.doctorId = doctorId;
        this.imgDoctor = imgDoctor;
        this.txtDoctorName = txtDoctorName;
        this.txtDoctorSpeciality = txtDoctorSpeciality;
        this.txtRating = txtRating;
    }
}
