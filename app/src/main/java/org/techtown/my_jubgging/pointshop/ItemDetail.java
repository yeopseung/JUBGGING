package org.techtown.my_jubgging.pointshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ItemDetail extends AppCompatActivity {
    private static final String LOG_TAG = "ItemDetail";

    private Retrofit retrofit = RetrofitClient.getInstance();
    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();


    private ImageView itemURL;
    private TextView name;
    private TextView information;
    private TextView price;
    private TextView stock;

    private Button order_open;
    private ImageButton order_close;
    private LinearLayout order_page;
    private ImageButton order_plus;
    private ImageButton order_minus;
    private TextView order_count;
    private Button order_finish;

    private long itemId;
    private int count = 1;
    private String userId;

    boolean isPageOpen=false;
    private Animation translateUpAnim;
    private Animation translateDownAnim;

    private Item result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        //전달받은 itemId, userId
        itemId = getIntent().getLongExtra("itemId",0);
        userId = getIntent().getStringExtra("userId");
        Log.i(LOG_TAG,itemId+" "+userId);


        //아이템 내용 등록
        itemURL = findViewById(R.id.item_detail_itemURL);
        name = findViewById(R.id.item_detail_name);
        information = findViewById(R.id.item_detail_information);
        price = findViewById(R.id.item_detail_price);
        stock = findViewById(R.id.item_detail_stock);

        //주문페이지 등록
        order_open = findViewById(R.id.item_detail_order_page_open);
        order_page = findViewById(R.id.item_detail_order_page);
        order_close = findViewById(R.id.item_detail_order_page_close);
        order_plus = findViewById(R.id.item_detail_order_page_plus);
        order_minus = findViewById(R.id.item_detail_order_page_minus);
        order_count = findViewById(R.id.item_detail_order_page_count);
        order_finish = findViewById(R.id.item_detail_order_end);

        //주문페이지 띄우는 애니메이션 등록
        translateUpAnim = AnimationUtils.loadAnimation(this,R.anim.translate_up);
        translateDownAnim = AnimationUtils.loadAnimation(this,R.anim.translate_down);
        SlidingAnimationListener animListener = new SlidingAnimationListener();
        translateUpAnim.setAnimationListener(animListener);
        translateDownAnim.setAnimationListener(animListener);

        //주문페이지 버튼 클릭리스너 등록
        ItemDetail.ButtonClickListener buttonClickListener = new ButtonClickListener();
        order_open.setOnClickListener(buttonClickListener);
        order_close.setOnClickListener(buttonClickListener);
        order_plus.setOnClickListener(buttonClickListener);
        order_minus.setOnClickListener(buttonClickListener);
        order_finish.setOnClickListener(buttonClickListener);


        //상품의 상세내용을 받아옴
        Call<Item> call = retrofitAPI.getItem(itemId);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                View callOutBalloon;

                //통신 실패
                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG, String.valueOf(response.code()));
                    return;
                }

                //통신 성공시 커스텀마커 (커스텀 쓰레기통) 추가
                result = response.body();

                //이미지 , 상품명, 설명, 가격, 재고 설정
                Glide.with(getApplicationContext()).load(result.getItemURL()).into(itemURL);
                name.setText(result.getName());
                information.setText(result.getInformation());
                price.setText(result.getPrice());
                stock.setText(result.getStock());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                //통신 실패
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });

    }

    //구매하기 버튼 슬라이딩 애니메이션 (열고 닫는 기능)
    class SlidingAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen){
                order_page.setVisibility(View.INVISIBLE);
                isPageOpen = false;
            }
            else{
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    class ButtonClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.item_detail_order_page_open:
                    //상품 주문 페이지 오픈
                    if(isPageOpen){
                        order_page.startAnimation(translateUpAnim);
                    }
                    else {
                        order_page.setVisibility(View.VISIBLE);
                        order_page.startAnimation(translateDownAnim);
                    }
                    break;
                case R.id.item_detail_order_page_close:
                    //상품 주문 페이지 닫음
                    if(isPageOpen){
                        order_page.startAnimation(translateUpAnim);
                    }
                    else
                    {
                        order_page.setVisibility(View.INVISIBLE);
                        order_page.startAnimation(translateDownAnim);
                    }
                    break;

                case R.id.item_detail_order_page_plus:
                    //상품 개수 추가 +1
                    if(count+1 > Integer.parseInt(result.getStock()))
                    {
                        Toast.makeText(getApplicationContext(),"주문가능 수량 초과.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        count++;
                        order_count.setText(String.valueOf(count));
                    }
                    break;

                case R.id.item_detail_order_page_minus:
                    //상품 개수 추가 -1
                    if(count-1 <= 0)
                    {
                        Toast.makeText(getApplicationContext(),"주문 최소수량은 1개입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        count--;
                        order_count.setText(String.valueOf(count));
                    }
                    break;

                case R.id.item_detail_order_end:
                    //상품 주문 완료
                    Call<String> call = retrofitAPI.createOrder(new Order(userId,itemId,count));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            View callOutBalloon;

                            //통신 실패
                            if (!response.isSuccessful()) {
                                Log.e(LOG_TAG, response.toString());
                                if(response.code()==400)
                                {
                                    Toast.makeText(getApplicationContext(),"포인트가 부족합니다.",Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }

                            //통신 성공시 커스텀마커 (커스텀 쓰레기통) 추가
                            String result_create = response.body();
                            Toast.makeText(getApplicationContext(),"주문이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                            result.setStock(String.valueOf(Long.parseLong(result.getStock()) - count));
                            stock.setText("재고: "+ result.getStock());

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            //통신 실패
                            Log.e(LOG_TAG, t.getLocalizedMessage());
                        }
                    });

                    break;
            }
        }
    }

}