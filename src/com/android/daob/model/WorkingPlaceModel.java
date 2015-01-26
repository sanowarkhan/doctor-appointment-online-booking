
package com.android.daob.model;

public class WorkingPlaceModel {
    private String workingPlaceId;

    private String workingPlaceName;

    private String address;

    public String getWorkingPlaceId() {
        return workingPlaceId;
    }

    public void setWorkingPlaceId(String workingPlaceId) {
        this.workingPlaceId = workingPlaceId;
    }

    public String getWorkingPlaceName() {
        return workingPlaceName;
    }

    public void setWorkingPlaceName(String workingPlaceName) {
        this.workingPlaceName = workingPlaceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.workingPlaceName;
    }

}
