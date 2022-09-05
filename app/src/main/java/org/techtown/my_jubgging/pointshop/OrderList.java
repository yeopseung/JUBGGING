package org.techtown.my_jubgging.pointshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderList extends AppCompatActivity {
    private static final String LOG_TAG = "OrderList";

    private Retrofit retrofit = RetrofitClient.getInstance();
    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        UserInfo userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");

        Call<HashMap<String, ArrayList<Order>>> call = retrofitAPI.getOrderList(userInfo.getUserId());
        call.enqueue(new Callback<HashMap<String, ArrayList<Order>>>() {
            @Override
            public void onResponse(Call<HashMap<String, ArrayList<Order>>> call, Response<HashMap<String, ArrayList<Order>>> response) {
                View callOutBalloon;

                //통신 실패
                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG, String.valueOf(response.code()));
                    return;
                }

                //RecyclerView 객체 지정
                RecyclerView recyclerView = findViewById(R.id.order_list_recycler);
                //그리드형식의 리사이클러뷰 (리사이클러뷰를 2개로 분할)
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                //통신 성공시 커스텀마커 (커스텀 쓰레기통) 추가
                HashMap<String, ArrayList<Order>> result = response.body();
                ArrayList<Order> orderArrayList = result.get("Results");
                Log.i(LOG_TAG,String.valueOf(orderArrayList.size()));

                for(Order order:orderArrayList)
                {
                    Log.i(LOG_TAG,String.valueOf(order.getOrderDate()));
                }

                //RecyclerView 어뎁터 지정
                OrderAdapter adapter = new OrderAdapter(orderArrayList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<HashMap<String, ArrayList<Order>>> call, Throwable t) {
                //통신 실패
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });
    }
}