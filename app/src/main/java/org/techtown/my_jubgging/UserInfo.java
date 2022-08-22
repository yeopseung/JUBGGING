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
    @SerializedName("address")
    String address;
    @SerializedName("gender")
    String gender;



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

    public String getUserId() {return userId;}

    public String getName() {return name;}

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
