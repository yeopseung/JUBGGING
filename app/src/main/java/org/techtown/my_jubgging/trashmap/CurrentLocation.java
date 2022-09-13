package org.techtown.my_jubgging.trashmap;

import com.google.gson.annotations.SerializedName;

public class CurrentLocation {
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("findMeter")
    int findMeter;

    public CurrentLocation(double latitude, double longitude, int findMeter) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.findMeter = findMeter;
    }
}
