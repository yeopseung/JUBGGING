package org.techtown.my_jubgging.ranking;

import com.google.gson.annotations.SerializedName;

public class RankInfo {
    @SerializedName("rank")
    int rank;

    @SerializedName("userName")
    String userName;

    @SerializedName("userNickName")
    String userNickName;

    @SerializedName("walkingNum")
    int walkingNum;

    @SerializedName("profileURL")
    String profileURL;

    @SerializedName("userId")
    Long userId;

    public int getRank() {
        return rank;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public int getWalkingNum() {
        return walkingNum;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public Long getUserId() {
        return userId;
    }
}
