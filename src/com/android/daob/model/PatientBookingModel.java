package com.android.daob.model;

import java.io.Serializable;

public class PatientBookingModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String startTime;
    private String endTime;
    private String meetingDate;
    private Boolean isDelegated;
    private String preDescription;
    private String location;
    private String notes;
    private String doctor;
    private String email;
    private String patientId;
    private String delPatName;
    private String delPatAge;
    private String delPatGender;
    private String delPatPhone;
    private String delPatAddress;
    private int confirmKey;
    
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMeetingDate() {
		return meetingDate;
	}
	public void setMeetingDate(String meetingDate) {
		this.meetingDate = meetingDate;
	}
	public Boolean getIsDelegated() {
		return isDelegated;
	}
	public void setIsDelegated(Boolean isDelegated) {
		this.isDelegated = isDelegated;
	}
	public String getPreDescription() {
		return preDescription;
	}
	public void setPreDescription(String preDescription) {
		this.preDescription = preDescription;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getDelPatName() {
		return delPatName;
	}
	public void setDelPatName(String delPatName) {
		this.delPatName = delPatName;
	}
	public String getDelPatAge() {
		return delPatAge;
	}
	public void setDelPatAge(String delPatAge) {
		this.delPatAge = delPatAge;
	}
	public String getDelPatGender() {
		return delPatGender;
	}
	public void setDelPatGender(String delPatGender) {
		this.delPatGender = delPatGender;
	}
	public String getDelPatPhone() {
		return delPatPhone;
	}
	public void setDelPatPhone(String delPatPhone) {
		this.delPatPhone = delPatPhone;
	}
	public String getDelPatAddress() {
		return delPatAddress;
	}
	public void setDelPatAddress(String delPatAddress) {
		this.delPatAddress = delPatAddress;
	}
	public int getConfirmKey() {
		return confirmKey;
	}
	public void setConfirmKey(int confirmKey) {
		this.confirmKey = confirmKey;
	}
    
    
}
