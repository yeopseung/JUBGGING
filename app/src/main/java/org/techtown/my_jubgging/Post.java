package org.techtown.my_jubgging;

import com.google.gson.annotations.SerializedName;

public class Post {
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
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
