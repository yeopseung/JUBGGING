package org.techtown.my_jubgging.together;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.my_jubgging.MyProfile;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.RegionPickerActivity;
import org.techtown.my_jubgging.UserInfo;
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
    public static UserInfo userInfo;

    private Context context;
    public static TogetherFragment mContext;

    public static final int RESULT_OK = -1;

    ImageButton profileImgBtn;
    LinearLayout targetRegionLayout;
    TextView regionTxt;
    ImageButton addNewPageBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_together, container, false);

        context = getActivity();
        mContext = this;
        userInfo = (UserInfo)getActivity().getIntent().getSerializableExtra("userInfo");

        setViewById(rootView);
        setOnClick();

        Glide.with(this).load(userInfo.profileURL).apply(new RequestOptions().circleCrop()).into(profileImgBtn);

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitApi = retrofit.create(RetrofitAPI.class);

        //regionTxt.setText(userInfo.getDong());
        //getPost(userInfo.getDong());
        getPost("상도동");
        regionTxt.setText("상도동");

        return rootView;
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    int regionNum = data.getIntExtra("regionCnt", 0);

                    customToast(regionNum + " ");
                    if (regionNum > 0) {
                        String get = data.getStringExtra("region1");
                        regionTxt.setText(get);
                        getPost(get);
                    }
                }
            }
    );

    private void setViewById(ViewGroup rootView) {
        profileImgBtn = rootView.findViewById(R.id.together_board_my_profile_button);
        addNewPageBtn = rootView.findViewById(R.id.together_add_button);

        targetRegionLayout = rootView.findViewById(R.id.together_board_target_region_layout);
        regionTxt = rootView.findViewById(R.id.together_board_target_region_text);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.together_board_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    private void setOnClick() {
        profileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyProfile.class);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);
            }
        });

        addNewPageBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, NewpageActivity.class);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);
            }
        });

        targetRegionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegionPickerActivity.class);
                intent.putExtra("userInfo",userInfo);
                intent.putExtra("targetNum", 1);

                intent.putExtra("region1", regionTxt.getText().toString());
                intent.putExtra("region2", "+");
                intent.putExtra("region3", "+");

                intent.putExtra("regionCnt", 1);

                mStartForResult.launch(intent);
            }
        });
    }

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