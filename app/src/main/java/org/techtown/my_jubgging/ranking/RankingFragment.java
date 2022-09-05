package org.techtown.my_jubgging.ranking;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;
import org.techtown.my_jubgging.together.RegionPost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RankingFragment extends Fragment {
    private RetrofitAPI retrofitAPI;
    private RecyclerView recyclerView;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_ranking, container, false);

        context = getActivity();

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        recyclerView = rootView.findViewById(R.id.ranking_adapter_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        getAndSetRank();

        return rootView;
    }

    private void getAndSetRank() {
        Call<Map<String, List<RankInfo>>> call = retrofitAPI.getRankList();

        call.enqueue(new Callback<Map<String, List<RankInfo>>>() {
            @Override
            public void onResponse(Call<Map<String, List<RankInfo>>> call, Response<Map<String, List<RankInfo>>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }
                recyclerView.removeAllViews();

                Map<String, List<RankInfo>> data = response.body();
                List<RankInfo> realData = data.get("Results");
                ArrayList<RankInfo> reformData = new ArrayList<RankInfo>(realData);

                recyclerView.setAdapter(new RankRecycleAdapter(reformData));
            }

            @Override
            public void onFailure(Call<Map<String, List<RankInfo>>> call, Throwable t) {
                customToast("Fail : " + t.getMessage());
            }
        });
    }

    private void customToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}