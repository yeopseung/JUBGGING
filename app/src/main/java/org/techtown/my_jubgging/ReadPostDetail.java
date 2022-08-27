package org.techtown.my_jubgging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;
import org.w3c.dom.Text;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReadPostDetail extends Activity {
    Context context;

    ImageButton backBtn;
    ImageButton settingBtn;

    TextView recruitingTxt;
    TextView userNameTxt;
    TextView regionTxt;
    TextView timeTxt;

    TextView titleTxt;
    TextView genderTxt;
    TextView placeTxt;

    TextView content;

    Button participateBtn;


    private RetrofitAPI retrofitApi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_together_post);

        context = getApplicationContext();

        Intent intent = getIntent();
        long boardId = intent.getLongExtra("boardId", 0L);

        boardId = 50L;
        setButtons();
        buttonsOnClickSet();

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitApi = retrofit.create(RetrofitAPI.class);

        Call<Map<String, Object>> call = retrofitApi.getPostDetail(boardId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                //setValue(response.body());
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });
    }

    private void setButtons() {
        backBtn = findViewById(R.id.together_post_back_button);
        settingBtn = findViewById(R.id.together_post_setting_button);

        recruitingTxt = findViewById(R.id.together_post_recruiting_text);
        userNameTxt = findViewById(R.id.together_post_username_text);
        regionTxt = findViewById(R.id.together_post_region_text);
        timeTxt = findViewById(R.id.together_post_posting_time_text);

        titleTxt = findViewById(R.id.together_post_title_text);
        genderTxt = findViewById(R.id.together_post_gender_text);
        placeTxt = findViewById(R.id.together_post_place_text);

        content = findViewById(R.id.together_post_content_text);

        participateBtn = findViewById(R.id.together_post_participate_button);
    }

    private void buttonsOnClickSet() {
        /* 뒤로가기 버튼 */
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                startActivity(intent);
            }
        });

        //< FIXME Setting Btn
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        participateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void setValue(Map<String, Object> data) {
        /*
        recruitingTxt.setText("모집중"); //< FIXME
        userNameTxt.setText(data.get("nickName").toString());
        regionTxt.setText(data.get("address").toString());
        //timeTxt.setText(readPost.modifiedTime)

        titleTxt.setText(data.get("title").toString());
        //genderTxt.setText(readPost.)
        placeTxt.setText(data.get("place").toString());

        content.setText(data.get("content").toString());
        */
    }

    private void customToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}