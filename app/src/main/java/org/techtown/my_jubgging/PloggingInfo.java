package org.techtown.my_jubgging;

import com.google.gson.annotations.SerializedName;

public class PloggingInfo {
    @SerializedName("userId")
    public long userId;

    @SerializedName("walkingNum")
    public int walkingNum;

    @SerializedName("walkingTime")
    public String walkingTime;

    public long getUserId() {
        return userId;
    }

    public int getWalkingNum() {
        return walkingNum;
    }

    public String getWalkingTime() {
        return walkingTime;
    }
}
