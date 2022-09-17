package org.techtown.my_jubgging;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import org.techtown.my_jubgging.trashmap.DBHelper;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 10;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private static final String LOG_TAG = "MainActivity: ";
    GoogleSignInClient mGoogleSingInClient;
    GoogleSignInAccount account;

    private ISessionCallback iSessionCallback;

    private DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleSingIn();

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
                        Log.e(LOG_TAG,"로그인 세션이 닫혔습니다.");
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        //로그인 실패
                        Log.e(LOG_TAG,"로그인 실패.");

                    }

                    @Override
                    public void onSuccess(MeV2Response result) {
                        //로그인 성공
                        UserInfo userInfo = new UserInfo(result);
                        Log.i(LOG_TAG,"로그인 성공");
                        Log.i(LOG_TAG, "userId "+String.valueOf(userInfo.userId));
                        Log.i(LOG_TAG,"email " + userInfo.email);
                        Log.i(LOG_TAG,"nickName "+userInfo.nickName);
                        Log.i(LOG_TAG, "gender "+"Enum 형 Gender");
                        Log.i(LOG_TAG,"profileURL "+userInfo.profileURL);


                        //회원가입 여부 판단
                        Call<String> call = retrofitAPI.checkUserId(userInfo.getUserId());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                //통신 실패
                                if (!response.isSuccessful()) {
                                    Log.e(LOG_TAG,"회원가입여부 판단 실패 "+String.valueOf(response.code()));
                                    return;
                                }

                                String result  = response.body();
                                Intent intent;
                                //기존 회원 -> MainMenu 이동
                                if(result.equals("{\"user\":\"Y\"}"))
                                {
                                    Log.i(LOG_TAG,"기존 회원 "+ result);

                                    intent = new Intent(MainActivity.this, MainMenu.class);
                                    intent.putExtra("userInfo",userInfo);
                                    startActivity(intent);
                                    finish();

                                }
                                //신규 회원 -> SignUp 이동
                                else if(result.equals("{\"user\":\"N\"}"))
                                {
                                    Log.i(LOG_TAG,"신규 회원 "+result);

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
                                Log.e(LOG_TAG,"회원가입여부 판단 실패 "+t.getLocalizedMessage());

                            }

                        });

                    }


                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                //세션 오픈 실패
                Log.e(LOG_TAG,"로그인 세션 실패.");
            }
        };
        Session.getCurrentSession().addCallback(iSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();


        //해시키 값을 얻고 싶은 경우 주석을 풀어 사용
        //getAppKeyHash();
    }

    /* Jaewoo added for Google Fit */
    private void googleSingIn() {
        // 필요한 권한들 정의
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        mGoogleSingInClient = GoogleSignIn.getClient(this, gso);

        account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Intent signInIntent = mGoogleSingInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
            fitnessConnect();
        }
    }

    private void fitnessConnect() {
        FitnessOptions fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                        .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            subscribe();
        }
    }

    public void subscribe() {
        Fitness.getRecordingClient(this,
                        GoogleSignIn.getLastSignedInAccount(this))
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE)
                subscribe();
        }

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