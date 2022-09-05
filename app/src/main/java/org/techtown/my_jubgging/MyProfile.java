package org.techtown.my_jubgging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import org.techtown.my_jubgging.pointshop.OrderList;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;
import org.techtown.my_jubgging.trashmap.CustomTrash;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyProfile extends AppCompatActivity {

    private static final String LOG_TAG = "MyProfile";

    private Retrofit retrofit = RetrofitClient.getInstance();
    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();

    private ImageView profile;
    private TextView name;
    private TextView nickName;
    private TextView gender;
    private TextView email;
    private TextView roadAddress;
    private TextView specificAddress;
    private TextView point;
    private Button logout;
    private Button order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        UserInfo userInfo = (UserInfo)getIntent().getSerializableExtra("userInfo");

        profile = findViewById(R.id.my_profile_profile);
        name = findViewById(R.id.my_profile_name);
        nickName = findViewById(R.id.my_profile_nickName);
        gender = findViewById(R.id.my_profile_gender);
        email = findViewById(R.id.my_profile_email);
        roadAddress = findViewById(R.id.my_profile_road_address);
        specificAddress = findViewById(R.id.my_profile_specific_address);
        point = findViewById(R.id.my_profile_point);
        logout = findViewById(R.id.my_profile_logout);
        order = findViewById(R.id.my_profile_order);


        //커스텀 쓰레기통 불러와서 띄우기
        //커스텀 쓰레기통 목록 GET
        Call<UserInfo> call = retrofitAPI.getUserProfile(userInfo.getUserId());
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                View callOutBalloon;

                //통신 실패
                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG, String.valueOf(response.code()));
                    return;
                }

                //통신 성공시 커스텀마커 (커스텀 쓰레기통) 추가
                UserInfo result = response.body();

                Glide.with(getApplicationContext()).load(result.profileURL).into(profile);

                name.setText("이름: "+result.getName());
                nickName.setText("닉네임: "+result.getNickName());
                switch (result.getGender())
                {
                    case "MALE":
                        gender.setText("성별: 남");
                        break;
                    case "FEMALE":
                        gender.setText("성별: 여");
                        break;
                }
                email.setText("이메일: "+result.getEmail());
                roadAddress.setText("주소: "+result.getRoadAddress());
                specificAddress.setText("상세주소: "+result.getSpecificAddress());
                point.setText("포인트: "+result.getPoint()+" 점");


            }

             @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                //통신 실패
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        //로그아웃 성공시 수행하는 시점
                        Intent intent = new Intent(MyProfile.this,MainActivity.class);
                        ActivityCompat.finishAffinity(MyProfile.this);
                        startActivity(intent);
                    }
                });
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, OrderList.class);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);
            }
        });
    }
}