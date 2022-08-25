package org.techtown.my_jubgging.trashmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.techtown.my_jubgging.MainMenu;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.auth.SignUp;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomTrashAdd extends AppCompatActivity {
    private static final String LOG_TAG = "CustomTrashAdd";

    private CustomTrash customTrash = new CustomTrash();

    private EditText address;
    private RadioGroup kind_group;
    private RadioButton general;
    private RadioButton recycle;
    private RadioButton smoking;
    private Button save;

    private Retrofit retrofit = RetrofitClient.getInstance();
    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutstom_trash_add);

        address = findViewById(R.id.custom_trash_address);
        kind_group = findViewById(R.id.custom_trash_kind_group);
        general = findViewById(R.id.custom_trash_general);
        recycle = findViewById(R.id.custom_trash_recycle);
        smoking = findViewById(R.id.custom_trash_smoking);
        save= findViewById(R.id.custom_trash_save);





        //변환된 주소를 담고있는 리스트
        List<Address> addressList;
        double latitude,longitude;


        UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);

        customTrash.setUserId(userInfo.getUserId());
        customTrash.setLatitude(String.valueOf(latitude));
        customTrash.setLatitude(String.valueOf(longitude));

        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        try {
            // 위도/경도 -> 주소로 변환
            Log.i(LOG_TAG,latitude+""+longitude);
            addressList = geocoder.getFromLocation(latitude,longitude,1);

            // EditText 에 띄워줌
            StringBuffer adr = new StringBuffer();

            adr.append(addressList.get(0).getAdminArea()+" ");
            adr.append(addressList.get(0).getSubLocality()+" ");
            adr.append(addressList.get(0).getThoroughfare()+" ");
            adr.append(addressList.get(0).getFeatureName());

            address.setText(adr);

            Log.i(LOG_TAG,addressList.get(0).toString());
        }
        //네트워크나 기타 I/O 문제 처리
        catch (IOException e) {
            e.printStackTrace();
        }
        //잘못된 위도 or 경도 값일 경우 처리
        catch (IllegalArgumentException e)
        {
            Log.e(LOG_TAG, "잘못된 위도 경도 전달받음.");

        }


        //라디오버튼 클릭리스너 등록 (쓰레기통 종류 선택)
        kind_group.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        //쓰레기통 등록 버튼 클릭리스너 등록 (쓰레기통 정보 저장)
        save.setOnClickListener(buttonClickListener);


    }

    //라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.custom_trash_general){
                customTrash.setKind("GENERAL");
            }
            else if(i == R.id.custom_trash_recycle){
                customTrash.setKind("RECYCLE");
            }
            else if(i == R.id.custom_trash_smoking){
                customTrash.setKind("SMOKING");
            }
        }
    };


    Button.OnClickListener buttonClickListener = new View.OnClickListener()
    {
        //쓰레기통 등록 버튼 클릭시
        @Override
        public void onClick(View view) {
            {
                //쓰레기통 종류를 골랐을 경우
                if(general.isChecked()||recycle.isChecked()||smoking.isChecked())
                {
                    //회원정보 POST
                    Call<String> call_userinfo = retrofitAPI.createCustomTrash(customTrash);
                    call_userinfo.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            //통신 실패
                            if (!response.isSuccessful()) {
                                Log.e(LOG_TAG, String.valueOf(response.code()));
                                return;
                            }

//                            //통신 성공시 MainMenu 로 이동
//                            String result = response.body();
//                            Log.i(LOG_TAG, result);
//                            Intent intent = new Intent(SignUp.this, MainMenu.class);
//                            intent.putExtra("userInfo", userInfo);
//                            startActivity(intent);
//                            finish();

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            //통신 실패
                            Log.e(LOG_TAG, t.getLocalizedMessage());
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"쓰레기통 종류를 선택해주세요.",Toast.LENGTH_SHORT);
                }


            }
        }
    };




}