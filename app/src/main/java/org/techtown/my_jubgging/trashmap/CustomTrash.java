package org.techtown.my_jubgging.trashmap;

import com.google.gson.annotations.SerializedName;

public class CustomTrash {
    //커스텀 쓰레기통 아이디
    @SerializedName("id")
    private String customTrashAddressId;

    //유저 아이디
    @SerializedName("userId")
    private String userId;

    //위도
    @SerializedName("latitude")
    private String latitude;

    //경도
    @SerializedName("longitude")
    private String longitude;

    //쓰레기통 종류
    @SerializedName("kind")
    private String kind;

    //유저 프로필 사진
    @SerializedName("profileURL")
    private String profileURL;

    //닉네임
    @SerializedName("nickName")
    private String nickName;

    //좋아요 수
    @SerializedName("heart")
    private int heart;

    //추가한 쓰레기통 개수
    @SerializedName("addPlaceNum")
    private int addPlaceNum;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCustomTrashAddressId() {
        return customTrashAddressId;
    }

    public void setCustomTrashAddressId(String customTrashAddressId) {
        this.customTrashAddressId = customTrashAddressId;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getAddPlaceNum() {
        return addPlaceNum;
    }

    public void setAddPlaceNum(int addPlaceNum) {
        this.addPlaceNum = addPlaceNum;
    }
}
