package org.techtown.my_jubgging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
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
    TextView partyNumText;

    Button participateBtn;

    LinearLayout profileLayouts;

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

                setValue(response.body());
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
        partyNumText = findViewById(R.id.together_post_party_num_text);

        participateBtn = findViewById(R.id.together_post_participate_button);

        profileLayouts = findViewById(R.id.together_post_profiles_layout);
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
        Integer attendingNum =((Double)data.get("nowAttendingNum")).intValue();
        Integer peopleNum = ((Double)data.get("peopleNum")).intValue();

        HashMap<String, String> genderTranslate = new HashMap<String, String>();
        genderTranslate.put("Female", "여자만");
        genderTranslate.put("Male", "남자만");
        genderTranslate.put("All", "아무나");

        recruitingTxt.setText("모집중"); //< FIXME
        userNameTxt.setText(data.get("nickName").toString());
        regionTxt.setText(data.get("address").toString());
        timeTxt.setText(data.get("modifiedTime").toString());

        titleTxt.setText(data.get("title").toString());
        genderTxt.setText(genderTranslate.get(data.get("possibleGender")));
        placeTxt.setText(data.get("place").toString());

        content.setText(data.get("content").toString());

        partyNumText.setText("참여 " + attendingNum + " / " + peopleNum);

        profileLayouts.removeAllViews();
        List<String> URLS = (List<String>)data.get("attendingPeopleProfileURL");
        for (int i = 0; i < attendingNum; ++i) {
            ImageView imgView = makeNewImageView(URLS.get(i));
            profileLayouts.addView(imgView);
        }
    }

    private ImageView makeNewImageView(String URL) {
        ImageView imgView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(138, 138);
        params.setMargins(0, 0, 40, 0);
        imgView.setLayoutParams(params);

        Glide.with(this).load(URL).apply(new RequestOptions().circleCrop()).into(imgView);

        return imgView;
    }

    private void customToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}