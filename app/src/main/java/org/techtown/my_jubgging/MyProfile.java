package org.techtown.my_jubgging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class MyProfile extends AppCompatActivity {
    private ImageView profile;
    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        UserInfo userInfo = (UserInfo)getIntent().getSerializableExtra("userInfo");

        profile = findViewById(R.id.my_profile_profile);
        logout = findViewById(R.id.my_profile_logout);

        Glide.with(this).load(userInfo.profileURL).into(profile);

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
    }
}