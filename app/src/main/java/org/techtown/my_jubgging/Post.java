package org.techtown.my_jubgging;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("userId")
    String userId = "NULL";

    @SerializedName("region1")
    String region1 = "";

    @SerializedName("region2")
    String region2 = "";

    @SerializedName("region3")
    String region3 = "";

    @SerializedName("title")
    String title = "Title";

    @SerializedName("content")
    String content = "Content";

    @SerializedName("peopleNum")
    int    peopleNum = 3;

    @SerializedName("possibleGender")
    String possibleGender = "All";

    @SerializedName("localDate")
    String localDate = "YYYY MM DD";

    @SerializedName("localTime")
    String localTime = "HH MM";

    @SerializedName("kakaoChatAddress")
    String kakaoChatAddress = "Address";

    @SerializedName("place")
    String place = "Place";

    public String getUserId() {
        return userId;
    }

    public String getRegion1() {
        return region1;
    }

    public String getRegion2() {
        return region2;
    }

    public String getRegion3() {
        return region3;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public String getPossibleGender() {
        return possibleGender;
    }

    public String getLocalDate() {
        return localDate;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getKakaoChatAddress() {
        return kakaoChatAddress;
    }

    public String getPlace() {
        return place;
    }
}
