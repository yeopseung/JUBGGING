package org.techtown.my_jubgging;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;


import org.techtown.my_jubgging.auth.SignUp;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private ISessionCallback iSessionCallback;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.result);

        Retrofit retrofit = RetrofitClient.getInstance();
        RetrofitAPI retrofitAPI = RetrofitClient.getApiService();


        iSessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                //로그인 요청
                UserManagement.getInstance().me(new MeV2ResponseCallback() {

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        //세션이 닫힌 상황
                        Log.e("Login_SessionClosed","세션이 닫혔습니다.");
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        //로그인 실패
                        Log.e("Login_Fail","로그인 실패.");

                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        //로그인 성공
                        UserInfo userInfo = new UserInfo(result);
                        Log.i("Login_Success","로그인 성공");
                        Log.i("Login_Info_userID", String.valueOf(userInfo.userId));
                        Log.i("Login_Info_Email",userInfo.email);
                        Log.i("Login_Info_NickName",userInfo.nickName);
                        Log.i("Login_Info_Gender", "Enum 형 Gender");
                        Log.i("Login_Info_ProfileURL",userInfo.profileURL);


                        //회원가입 여부 판단
                        Call<String> call = retrofitAPI.checkUserId(userInfo.getUserId());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                //통신 실패
                                if (!response.isSuccessful()) {
                                    Log.e("Login_Check_UserId_Fail",String.valueOf(response.code()));
                                    return;
                                }

                                String result  = response.body();
                                Intent intent;
                                //기존 회원 -> MainMenu 이동
                                if(result.equals("{\"user\":\"Y\"}"))
                                {
                                    Log.i("Login_Check_UserId_Y",result);

                                    intent = new Intent(MainActivity.this, MainMenu.class);
                                    intent.putExtra("userInfo",userInfo);
                                    startActivity(intent);
                                    finish();

                                }
                                //신규 회원 -> SignUp 이동
                                else if(result.equals("{\"user\":\"N\"}"))
                                {
                                    Log.i("Login_Check_UserId_N",result);

                                    //전달받는 것: 이메일, 닉네임(별명), 성별, 프로필사진
                                    //추가입력 요망: 이름(법적인 이름: legal name),  주소
                                    intent =  new Intent(MainActivity.this, SignUp.class);
                                    intent.putExtra("userInfo",userInfo);
                                    startActivity(intent);
                                    finish();

                                }

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                //통신 실패
                                Log.e("Login_Check_UserId_Fail",t.getLocalizedMessage());
                            }

                        });

                    }


                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                //세션 오픈 실패
                Log.e("Login_SessionOpenFailed","세션 실패.");
            }
        };
        Session.getCurrentSession().addCallback(iSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();


        //해시키 값을 얻고 싶은 경우 주석을 풀어 사용
        //getAppKeyHash();

        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainMenu.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data))
            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(iSessionCallback);
    }

    //카카오 로그인시 필요한 해시키를 얻는 메소드
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }

}