package org.techtown.my_jubgging.retrofit;


import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.trashmap.CustomTrash;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


//RetrofitAPI Interface 정의
public interface RetrofitAPI {


    //기존에 존재하는 User 인지 UserId를 통해 체크
    //Input : String userId
    //Output : String "Y" or "N"
    @GET("user/member")
    Call<String> checkUserId(@Query("userId") String userId);


    //기존에 존재하는 User 인지 UserId를 통해 체크
    //Input : String nickName
    //Output : String "Y" or "N"
    @GET("user/signup")
    Call<String> checkNickName(@Query("nickName") String nickName);

    //회원 정보를 User table 에 저장
    //Input : UserInfo userinfo
    //Output : String userId
    @POST("user/member")
    Call<String> createUserInfo(@Body UserInfo userInfo);


    //커스텀 쓰레기통을 Custom Trash Table 에 저장
    //Input :  CustomTrashAddress customTrashAddress
    //Output : String userId
    @POST("map/trash")
    Call<String> createCustomTrash(@Body CustomTrash customTrash);
}