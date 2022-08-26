package org.techtown.my_jubgging;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegionPost {
    @SerializedName("boardId")
    long userId = 123L;

    @SerializedName("peopleNum")
    int peopleNum = 4;

    @SerializedName("nowAttendingNum")
    int nowAttendingNum = 2;

    @SerializedName("title")
    String title = "Title";

    @SerializedName("content")
    String content = "Content";

    @SerializedName("region1")
    String region1 = "";

    @SerializedName("region2")
    String region2 = "";

    @SerializedName("region3")
    String region3 = "";

    @SerializedName("modifiedTime")
    LocalDateTime modifiedTime;

    @SerializedName("date")
    LocalDate date;

    public long getUserId() {
        return userId;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public int getNowAttendingNum() {
        return nowAttendingNum;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public LocalDate getDate() {
        return date;
    }
}
