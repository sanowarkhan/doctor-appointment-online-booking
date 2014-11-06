
package com.android.daob.model;

public class SpecialtyModel {
    private int specialtyId;

    private String specialtyName;

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
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
