
package com.android.daob.model;

public class SpecialtyModel {
    private String specialtyId;

    private String specialtyName;

    public String getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(String specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.specialtyName;
    }

}
