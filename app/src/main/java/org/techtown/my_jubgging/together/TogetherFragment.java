package org.techtown.my_jubgging.together;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.RegionPickerActivity;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TogetherFragment extends Fragment {
    private RetrofitAPI retrofitApi;
    private RecyclerView recyclerView;

    private Context context;

    public static final int RESULT_OK = -1;

    TextView regionMain;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_together, container, false);

        context = getActivity();
        ImageButton Button = rootView.findViewById(R.id.together_add_button);
        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, NewpageActivity.class);
                startActivity(intent);
            }
        });

        regionMain = rootView.findViewById(R.id.together_board_region_main);
        regionMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegionPickerActivity.class);
                intent.putExtra("targetNum", 1);
                mStartForResult.launch(intent);
            }
        });

        recyclerView = (RecyclerView)rootView.findViewById(R.id.together_board_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        Retrofit retrofit = RetrofitClient.getInstance();

        retrofitApi = retrofit.create(RetrofitAPI.class);
        getPost("상도동");

        return rootView;
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    int regionNum = data.getIntExtra("regionCnt", 0);

                    String get = "";

                    for (int i = 0; i < regionNum; ++i) {
                        get = data.getStringExtra("region1");
                        regionMain.setText(get);
                        regionMain.setBackgroundResource(R.drawable.rounded_rectangle);
                    }
                    for (int i = regionNum; i < 1; ++i) {
                        regionMain.setText("+");
                        regionMain.setBackgroundResource(R.drawable.rounded_rectangle_gray);
                    }

                    if (regionNum > 0)
                        getPost(get);
                }
            }
    );

    private void getPost(String regionName) {
        Call<Map<String, List<RegionPost>>> call = retrofitApi.getPosts(regionName);

        call.enqueue(new Callback<Map<String, List<RegionPost>>>() {
            @Override
            public void onResponse(Call<Map<String, List<RegionPost>>> call, Response<Map<String, List<RegionPost>>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }
                recyclerView.removeAllViews();

                Map<String, List<RegionPost>> data = response.body();
                List<RegionPost> realData = data.get("Results");
                ArrayList<RegionPost> reformData = new ArrayList<RegionPost>(realData);
                customToast(realData.size() + " ");

                recyclerView.setAdapter(new RecycleAdapter(reformData));

            }

            @Override
            public void onFailure(Call<Map<String, List<RegionPost>>> call, Throwable t){
                customToast("Fail : " + t.getMessage());
            }
        });
    }

    private void customToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}