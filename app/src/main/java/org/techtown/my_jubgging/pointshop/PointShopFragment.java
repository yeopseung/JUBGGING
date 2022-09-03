package org.techtown.my_jubgging.pointshop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.bumptech.glide.Glide;

import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PointShopFragment extends Fragment {
    private static final String LOG_TAG = "PointShopFragment";

    private Retrofit retrofit = RetrofitClient.getInstance();
    private RetrofitAPI retrofitAPI = RetrofitClient.getApiService();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_point_shop, container, false);

        //RecyclerView 객체 지정
        RecyclerView recyclerView = rootView.findViewById(R.id.point_shop_item_recycler);
        //그리드형식의 리사이클러뷰 (리사이클러뷰를 2개로 분할)
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);



        Call<HashMap<String, ArrayList<Item>>> call = retrofitAPI.getCustomTrashUser();
        call.enqueue(new Callback<HashMap<String, ArrayList<Item>>>() {
            @Override
            public void onResponse(Call<HashMap<String, ArrayList<Item>>> call, Response<HashMap<String, ArrayList<Item>>> response) {
                View callOutBalloon;

                //통신 실패
                if (!response.isSuccessful()) {
                    Log.e(LOG_TAG, String.valueOf(response.code()));
                    return;
                }

                //통신 성공시 커스텀마커 (커스텀 쓰레기통) 추가
                HashMap<String, ArrayList<Item>> result = response.body();

                //RecyclerView 어뎁터 지정
                ItemAdapter adapter = new ItemAdapter(result.get("Results"));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<HashMap<String, ArrayList<Item>>> call, Throwable t) {
                //통신 실패
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });



        return rootView;
    }
}