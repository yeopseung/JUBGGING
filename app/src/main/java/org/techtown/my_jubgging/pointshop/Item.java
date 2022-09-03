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
    private String price;
    @SerializedName("stock")
    private String stock;

    public Item(String itemURL, String name, String information, String price, String stock) {
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
