package com.android.daob.model;

public class DoctorModel {
	private int doctorId;
	private String doctorName;
	private String doctorWorkingPlace;
	private String description;
	private String education;
	private int specialty;
	
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
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
	public int getSpecialty() {
		return specialty;
	}
	public void setSpecialty(int specialty) {
		this.specialty = specialty;
	}
	
}
