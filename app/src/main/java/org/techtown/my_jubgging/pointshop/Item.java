package org.techtown.my_jubgging.pointshop;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("itemURL")
    private String itemURL;
    @SerializedName("name")
    private String name;
    @SerializedName("information")
    private String information;
    @SerializedName("price")
    private int price;
    @SerializedName("stock")
    private int stock;

    public Item(String itemURL, String name, String information, int price, int stock) {
        this.itemURL = itemURL;
        this.name = name;
        this.information = information;
        this.price = price;
        this.stock = stock;
    }

    public String getItemURL() {
        return itemURL;
    }

    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
