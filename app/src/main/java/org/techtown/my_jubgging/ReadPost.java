package org.techtown.my_jubgging;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReadPost {
    @SerializedName("nickNAme")
    String nickName;

    @SerializedName("address")
    String address;

    @SerializedName("modifiedTime")
    LocalDateTime modifiedTime;

    @SerializedName("peopleNum")
    int    peopleNum;

    @SerializedName("nowAttendingNum")
    int     nowAttendingNum;

    @SerializedName("date")
    LocalDate date;

    @SerializedName("title")
    String title;

    @SerializedName("content")
    String content = "Content";

    @SerializedName("possibleGender")
    String possibleGender = "All";

    @SerializedName("place")
    String place = "Place";

    @SerializedName("attendingPeopleProfileURL")
    List attendingPeopleProfileURL;

    public String getNickName() {
        return nickName;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public int getNowAttendingNum() {
        return nowAttendingNum;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPossibleGender() {
        return possibleGender;
    }

    public String getPlace() {
        return place;
    }

    public List getAttendingPeopleProfileURL() {
        return attendingPeopleProfileURL;
    }
}
