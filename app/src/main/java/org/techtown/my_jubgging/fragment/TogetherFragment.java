package org.techtown.my_jubgging.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import org.techtown.my_jubgging.NewpageActivity;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.ReadPost;
import org.techtown.my_jubgging.ReadPostDetail;
import org.techtown.my_jubgging.RegionPost;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TogetherFragment extends Fragment {
    private RetrofitAPI retrofitApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_together, container, false);

        ImageButton Button = rootView.findViewById(R.id.together_add_button);
        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewpageActivity.class);
                startActivity(intent);
            }
        });

        ImageButton Button2 = rootView.findViewById(R.id.together_post_read);
        Button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReadPostDetail.class);
                intent.putExtra("boardId", 189L);
                startActivity(intent);
            }
        });

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = RetrofitClient.getInstance();

        retrofitApi = retrofit.create(RetrofitAPI.class);
        Call<Map<String, List<RegionPost>>> call = retrofitApi.getPosts("상도동");

        call.enqueue(new Callback<Map<String, List<RegionPost>>>() {
            @Override
            public void onResponse(Call<Map<String, List<RegionPost>>> call, Response<Map<String, List<RegionPost>>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                Map<String, List<RegionPost>> data = response.body();
                List<RegionPost> realData = data.get("Results");
                customToast(realData.size() + " ");
                //for (int i = 0; i < realData.size(); ++i) {
                //    customToast(((ReadPost)(realData.get(i))).getTitle());
                //}

            }

            @Override
            public void onFailure(Call<Map<String, List<RegionPost>>> call, Throwable t){
                customToast("Fail : " + t.getMessage());
            }

        });

        return rootView;
    }


    private void customToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
}