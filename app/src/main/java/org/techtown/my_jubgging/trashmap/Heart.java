package org.techtown.my_jubgging.trashmap;

import com.google.gson.annotations.SerializedName;

public class Heart {
    @SerializedName("customTrashAddressId")
    private String customTrashAddressId;
    @SerializedName("userId")
    private String userId;

    public Heart(String customTrashAddressId, String userId) {
        this.customTrashAddressId = customTrashAddressId;
        this.userId = userId;
    }

    public String getCustomTrashAddressId() {
        return customTrashAddressId;
    }

    public void setCustomTrashAddressId(String customTrashAddressId) {
        this.customTrashAddressId = customTrashAddressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
