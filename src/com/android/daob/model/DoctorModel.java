package com.android.daob.model;

import java.io.Serializable;

public class DoctorModel implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String doctorId;
	private String doctorName;
	private String doctorWorkingPlace;
	private String description;
	private String education;
	private String specialty;
	
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDoctorWorkingPlace() {
		return doctorWorkingPlace;
	}
	public void setDoctorWorkingPlace(String doctorWorkingPlace) {
		this.doctorWorkingPlace = doctorWorkingPlace;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	
}
