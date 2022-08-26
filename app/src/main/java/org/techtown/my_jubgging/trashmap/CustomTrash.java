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
}
