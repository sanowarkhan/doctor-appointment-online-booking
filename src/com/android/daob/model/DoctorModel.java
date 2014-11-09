package com.android.daob.model;

public class DoctorModel {
	private int doctorId;
	private String doctorName;
	private int doctorWorkingPlace;
	private String description;
	private String education;
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
	public int getDoctorWorkingPlace() {
		return doctorWorkingPlace;
	}
	public void setDoctorWorkingPlace(int doctorWorkingPlace) {
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
	
}
