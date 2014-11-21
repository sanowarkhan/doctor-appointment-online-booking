
package com.android.daob.model;

public class UserMeetingModel {
    String doctorName;

    String hospital;

    String date;

    String hour;

    String status;
    
    int id;
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
