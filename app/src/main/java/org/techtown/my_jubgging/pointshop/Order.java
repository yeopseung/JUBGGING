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
    private String orderPrice;
    @SerializedName("orderDate")
    private String orderDate;
    @SerializedName("itemURL")
    private String itemURL;

    public Order(String userId, long itemId, int count) {
        this.userId = userId;
        this.itemId = itemId;
        this.count = count;
    }

    public Order(String name, String orderPrice, String orderDate, String itemURL) {
        this.name = name;
        this.orderPrice = orderPrice;
        this.orderDate = orderDate;
        this.itemURL = itemURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }
}
