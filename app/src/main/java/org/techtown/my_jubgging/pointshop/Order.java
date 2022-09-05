package org.techtown.my_jubgging.pointshop;

import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("userId")
    private String userId;
    @SerializedName("itemId")
    private long itemId;
    @SerializedName("count")
    private int count;

    @SerializedName("name")
    private String name;
    @SerializedName("orderPrice")
    private long orderPrice;
    @SerializedName("orderDate")
    private String orderDate;
    @SerializedName("itemURL")
    private String itemURL;

    public Order(String userId, long itemId, int count) {
        this.userId = userId;
        this.itemId = itemId;
        this.count = count;
    }

    public Order(String name, long orderPrice, String orderDate, String itemURL) {
        this.name = name;
        this.orderPrice = orderPrice;
        this.orderDate = orderDate;
        this.itemURL = itemURL;
    }
}
