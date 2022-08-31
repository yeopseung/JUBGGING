package org.techtown.my_jubgging.retrofit;


import org.techtown.my_jubgging.Post;
import org.techtown.my_jubgging.RegionPost;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.trashmap.CustomTrash;
import org.techtown.my_jubgging.trashmap.PublicTrash;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    //커스텀 쓰레기통 목록을 불러옴
    //Input :
    //Output : String userId
    @GET("map/trash/info")
    Call<List<CustomTrash>> getCustomTrashList();

    //커스텀 쓰레기통 등록자 정보를 불러옴
    //Input : String customTrashAddressId
    //Output : String userId
    @GET("map/trash/user")
    Call<UserInfo> getCustomTrashUser(@Query("customTrashAddressId") String customTrashAddressId);

    //공공 쓰레기통 목록을 불러옴
    //Input :
    //Output : UserInfo userInfo
    @GET("map/trash/publicInfo")
    Call<HashMap<String, List<PublicTrash>>> getPublicTrashList();

    //유저의 프로필 정보를 불러옴
    //Input :
    //Output : UserInfo userInfo
    @GET("user/{userId}/profiles")
    Call<UserInfo> getUserProfile(@Path("userId") String userId);



    // 새로운 게시물을 작성
    // Input : Post post
    // Output : boardId
    @POST("board")
    Call<Map<String, Long>> postNewPosting(@Body Post post);

    // 지역 게시물 조회
    @GET("board")
    Call<Map<String, List<RegionPost>>> getPosts(@Query("regionName") String regionName);

    // 게시물 세부 조회
    @GET("board/spec")
    Call<Map<String, Object>> getPostDetail(@Query(value = "boardId") long boardId);
}