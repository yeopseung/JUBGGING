package org.techtown.my_jubgging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.techtown.my_jubgging.home.HomeFragment;
import org.techtown.my_jubgging.ranking.RankingFragment;
import org.techtown.my_jubgging.together.TogetherFragment;
import org.techtown.my_jubgging.pointshop.PointShopFragment;
import org.techtown.my_jubgging.trashmap.TrashMapFragment;

public class MainMenu extends AppCompatActivity {

    // FrameLayout 에 각 메뉴의 Fragment 를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 5개의 메뉴에 들어갈 Fragment 들
    private RankingFragment rankingFragment = new RankingFragment();
    private TogetherFragment togetherFragment = new TogetherFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private TrashMapFragment trashMapFragment = new TrashMapFragment();
    private PointShopFragment pointShopFragment = new PointShopFragment();

    // 내 프로필 이동 버튼
    private ImageButton button;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Intent 에서 UserInfo 가져오기
        userInfo = (UserInfo)getIntent().getSerializableExtra("userInfo");

        //첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_frame_layout, homeFragment).commitAllowingStateLoss();


        //BottomNavigationView 선언 및 Listener 등록
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottom_navi_view);
        bottomNavigationView.setOnItemSelectedListener(new MainMenu.ItemSelectedListener());
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Button 등록
        button = findViewById(R.id.main_my_profile_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this,MyProfile.class);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);
            }
        });

        Glide.with(this).load(userInfo.profileURL).apply(new RequestOptions().circleCrop()).into(button);
    }


    //BottomNavigationView Item 선택 시
    //해당 Fragment 로 화면 전환
    class ItemSelectedListener  implements NavigationBarView.OnItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.ranking:
                    //main_frame_layout 의 fragment 를 ranking 으로 변경
                    transaction.replace(R.id.main_frame_layout, rankingFragment).commitAllowingStateLoss();
                    break;

                case R.id.together:
                    //main_frame_layout 의 fragment 를 together 으로 변경
                    transaction.replace(R.id.main_frame_layout, togetherFragment).commitAllowingStateLoss();
                    break;

                case R.id.home:
                    //main_frame_layout 의 fragment 를 home 으로 변경
                    transaction.replace(R.id.main_frame_layout, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.trash_map:
                    //main_frame_layout 의 fragment 를 trash map 으로 변경
                    transaction.replace(R.id.main_frame_layout, trashMapFragment).commitAllowingStateLoss();
                    break;
                case R.id.point_shop:
                    //main_frame_layout 의 fragment 를 point shop 으로 변경
                    transaction.replace(R.id.main_frame_layout, pointShopFragment).commitAllowingStateLoss();
                    break;
            }

            return true;
        }
    }
}