package org.techtown.my_jubgging.retrofit;


import org.techtown.my_jubgging.PloggingInfo;
import org.techtown.my_jubgging.ranking.RankInfo;
import org.techtown.my_jubgging.together.Post;
import org.techtown.my_jubgging.together.RegionPost;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.pointshop.Item;
import org.techtown.my_jubgging.pointshop.Order;
import org.techtown.my_jubgging.trashmap.CustomTrash;
import org.techtown.my_jubgging.trashmap.Heart;
import org.techtown.my_jubgging.trashmap.PublicTrash;


import java.util.ArrayList;
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

    //커스텀 쓰레기통에 좋아요를 눌렀는지 체크
    //Input : Heart heart
    //Output : String Y or N
    @POST("map/heart/check")
    Call<String> checkUserHeart(@Body Heart heart);

    //커스텀 쓰레기통 좋아요 추가
    //Input :  Heart heart
    //Output : String heart(개수)
    @POST("map/heart")
    Call<String> addUserHeart(@Body Heart heart);

    //커스텀 쓰레기통 좋아요 취소
    //Input :  Heart heart
    //Output : String heart(개수)
    @POST("map/heart/cancel")
    Call<String> cancelUserHeart(@Body Heart heart);


    //포인트샵 아이템 목록 불러오기
    //Input :
    //Output :HashMap<String, ArrayList<Item>>
    @GET("shop")
    Call<HashMap<String, ArrayList<Item>>> getItemList();

    //포인트샵 아이템 불러오기
    //Input :
    //Output : Item item
    @GET("shop/items/{itemId}")
    Call<Item> getItem(@Path("itemId") long itemId);

    //포인트샵 아이템 주문
    //Input :
    //Output : String userId
    @POST("order")
    Call<String> createOrder(@Body Order order);

    //주문 목록 불러오기
    //Input :
    //Output :HashMap<String, ArrayList<Item>>
    @GET("user/{userId}/orders")
    Call<HashMap<String, ArrayList<Order>>> getOrderList(@Path("userId") String userId);

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

    /* main 줍깅 */
    // 플로깅 기록 조회
    @GET("user/record")
    Call<Map<String, Object>> getPloggingInfo(
            @Query("userId") long userId,
            @Query("date") String date);

    // 플로깅 기록 등록
    @POST("user/record")
    Call<Map<String, Long>> savePloggingInfo(@Body PloggingInfo ploggingInfo);

    // 플로깅 예정 조회
    @GET("user/appointment")
    Call<Map<String, List<Object>>> getReservedPloggingList(
            @Query("userId") long userId);

    /* 랭킹 */
    // 랭킹 조회
    @GET("user/record/rank")
    Call<Map<String, List<RankInfo>>> getRankList();

    // 해당 게시물에 해당 유저가 참여중인지 여부 조회
    @GET("board/check/attending")
    Call<Map<String, String>> isParticipating(
            @Query("userId") long userId,
            @Query("boardId") long boardId);

    // 해당 게시물에 해당 유저를 참여 추가
    @GET("board/attend")
    Call<Map<String, Long>> addParticipant(
            @Query("boardId") long boardId,
            @Query("userId") long userId);

    //
    @POST("user/point")
    Call<Map<String, Integer>> addPoint(
            @Body Map<String, Object> data);

}