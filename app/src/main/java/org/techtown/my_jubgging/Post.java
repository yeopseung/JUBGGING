package org.techtown.my_jubgging;

import com.google.gson.annotations.SerializedName;

public class Post {
<<<<<<< Updated upstream
    @SerializedName("id")
    int id;
    @SerializedName("text")
    String text;
    @SerializedName("title")
    String title;
    @SerializedName("userId")
    int userId;

    public Post(int id, int userId, String title, String text) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
=======
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
>>>>>>> Stashed changes
    }

    public String getTitle() {
        return title;
    }

<<<<<<< Updated upstream
    public String getText() {
        return text;
=======
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
>>>>>>> Stashed changes
    }
}
