package org.techtown.my_jubgging;

import com.google.gson.annotations.SerializedName;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Gender;

import java.io.Serializable;

//로그인, 로그아웃, 회원가입에서 사용할 유저 정보 클래스
//Json 데이터 통신에 사용
public class UserInfo implements Serializable {
    @SerializedName("userId")
    String userId;
    @SerializedName("name")
    String name;
    @SerializedName("nickName")
    String nickName;
    @SerializedName("email")
    String email;
    @SerializedName("profileURL")
    String profileURL;
    @SerializedName("roadAddress")
    String roadAddress;
    @SerializedName("specificAddress")
    String specificAddress;
    @SerializedName("dong")
    String dong;
    @SerializedName("gender")
    String gender;

    @SerializedName("point")
    int point;

    @SerializedName("addPlaceNum")
    int addPlaceNum;
    @SerializedName("heart")
    int heart;



    UserInfo(MeV2Response result)
    {
        this.userId = String.valueOf(result.getId());
        this.nickName = result.getKakaoAccount().getProfile().getNickname();
        this.email = result.getKakaoAccount().getEmail();
        this.profileURL = result.getKakaoAccount().getProfile().getProfileImageUrl();

        if(result.getKakaoAccount().getGender()==Gender.MALE)
        {
            this.gender = "MALE";
        }
        else if(result.getKakaoAccount().getGender()==Gender.FEMALE)
        {
            this.gender = "FEMALE";
        }

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getSpecificAddress() {
        return specificAddress;
    }

    public void setSpecificAddress(String specificAddress) {
        this.specificAddress = specificAddress;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPoint() { return point;}

    public void setPoint(int point) {this.point = point;}

    public int getAddPlaceNum() {return addPlaceNum;}

    public void setAddPlaceNum(int addPlaceNum) {this.addPlaceNum = addPlaceNum;}

    public int getHeart() {return heart;}

    public void setHeart(int heart) {this.heart = heart;}
}
