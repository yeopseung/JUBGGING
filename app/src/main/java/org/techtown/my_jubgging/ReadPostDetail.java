package org.techtown.my_jubgging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    /* */
    Context context;

    ImageButton backBtn;
    ImageButton settingBtn;

    TextView recruitingTxt;

    ImageView profileImg;
    TextView userNameTxt;
    TextView regionTxt;
    TextView modifiedDateTxt;

    TextView titleTxt;
    TextView genderTxt;
    TextView placeTxt;
    TextView targetTimeTxt;

    TextView content;
    TextView partyNumText;

    Button participateBtn;

    LinearLayout profileLayouts;

    /* */
    int recruitingBoxColor;

    long nowMS;
    long dateMS;

    Integer attendingNum;
    Integer peopleNum;

    Calendar todayDate;
    Calendar targetDate;
    Calendar modifiedDate;

    private RetrofitAPI retrofitApi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_together_post);

        context = getApplicationContext();

        Intent intent = getIntent();
        long boardId = intent.getLongExtra("boardId", 0L);
customToast(boardId + " ");

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

        profileImg = findViewById(R.id.together_post_profile_image);
        userNameTxt = findViewById(R.id.together_post_username_text);
        regionTxt = findViewById(R.id.together_post_region_text);
        modifiedDateTxt = findViewById(R.id.together_post_modified_time_text);

        titleTxt = findViewById(R.id.together_post_title_text);
        genderTxt = findViewById(R.id.together_post_gender_text);
        placeTxt = findViewById(R.id.together_post_place_text);
        targetTimeTxt = findViewById(R.id.together_post_target_time_text);

        content = findViewById(R.id.together_post_content_text);
        partyNumText = findViewById(R.id.together_post_party_num_text);

        participateBtn = findViewById(R.id.together_post_participate_button);

        profileLayouts = findViewById(R.id.together_post_profiles_layout);
    }

    private void buttonsOnClickSet() {
        /* 뒤로가기 버튼 */
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
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
        attendingNum =((Double)data.get("nowAttendingNum")).intValue();
        peopleNum = ((Double)data.get("peopleNum")).intValue();

        setTimeSetting(data);

        recruitingTxt.setText(setRecruiting());
        recruitingTxt.setBackgroundColor(recruitingBoxColor);

        Glide.with(this).load(((List<String>)data.get("attendingPeopleProfileURL")).get(0)).apply(new RequestOptions().circleCrop()).into(profileImg);

        userNameTxt.setText(data.get("nickName").toString());
        regionTxt.setText(data.get("address").toString());

        modifiedDateTxt.setText(setModifiedTime());

        titleTxt.setText(data.get("title").toString());
        genderTxt.setText(genderSetting(data.get("possibleGender")));
        placeTxt.setText(data.get("place").toString());
        targetTimeTxt.setText(setDate());

        content.setText(data.get("content").toString());

        partyNumText.setText("참여 " + attendingNum + " / " + peopleNum);

        profileLayouts.removeAllViews();

        List<String> URLS = (List<String>)data.get("attendingPeopleProfileURL");
        for (int i = 0; i < attendingNum; ++i) {
            ImageView imgView = makeNewImageView(URLS.get(i));
            profileLayouts.addView(imgView);
        }
    }

    private String setRecruiting() {
        if ((attendingNum == peopleNum) || (nowMS >= dateMS)) {
            recruitingBoxColor = context.getResources().getColor(R.color.light_gray);
            return "모집 완료";
        } else {
            recruitingBoxColor = context.getResources().getColor(R.color.main_color_4);
            return "모집중";
        }
    }

    private void setTimeSetting(Map<String, Object> data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date modifiedTime = null;
        try { modifiedTime = dateFormat.parse(data.get("modifiedTime").toString()); }
        catch (Exception e) { }

        Date date = null;
        try { date = dateFormat.parse(data.get("appointmentTime").toString()); }
        catch (Exception e) { }
        dateMS = date.getTime();

        nowMS = System.currentTimeMillis();
        Date today = new Date(nowMS);

        modifiedDate = Calendar.getInstance();
        modifiedDate.setTime(modifiedTime);

        targetDate = Calendar.getInstance();
        targetDate.setTime(date);

        todayDate = Calendar.getInstance();
        todayDate.setTime(today);
    }

    private String setModifiedTime() {
        if (todayDate.get(Calendar.YEAR) != modifiedDate.get(Calendar.YEAR))
            return ((todayDate.get(Calendar.YEAR) - modifiedDate.get(Calendar.YEAR)) + "년 전");

        if (todayDate.get(Calendar.MONTH) != modifiedDate.get(Calendar.MONTH))
            return (((todayDate.get(Calendar.MONTH) - modifiedDate.get(Calendar.MONTH)) % 12) + "개월 전");

        if (todayDate.get(Calendar.DAY_OF_MONTH) != modifiedDate.get(Calendar.DAY_OF_MONTH))
            return (((todayDate.get(Calendar.DAY_OF_MONTH) - modifiedDate.get(Calendar.DAY_OF_MONTH)) % todayDate.getActualMaximum(Calendar.DAY_OF_MONTH)) + "일 전");

        if (todayDate.get(Calendar.HOUR_OF_DAY) != modifiedDate.get(Calendar.HOUR_OF_DAY))
            return (((todayDate.get(Calendar.HOUR_OF_DAY) - modifiedDate.get(Calendar.HOUR_OF_DAY)) % 24) + "시간 전");

        if (todayDate.get(Calendar.MINUTE) != modifiedDate.get(Calendar.MINUTE))
            return (((todayDate.get(Calendar.MINUTE) - modifiedDate.get(Calendar.MINUTE)) % 60) + "분 전");

        return "방금 전";
    }

    private String setDate() {
        if (todayDate.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR))
            return (targetDate.get(Calendar.MONTH) + "월 " + targetDate.get(Calendar.DATE) + "일");

        return (targetDate.get(Calendar.YEAR) + "년 " + targetDate.get(Calendar.MONTH) + "월 " + targetDate.get(Calendar.DATE) +"일");
    }

    private String genderSetting(Object input) {
        HashMap<String, String> genderTranslate = new HashMap<String, String>();
        genderTranslate.put("Female", "여자만");
        genderTranslate.put("Male", "남자만");
        genderTranslate.put("All", "아무나");

        return genderTranslate.get(input);
    }

    private ImageView makeNewImageView(String URL) {
        ImageView imgView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(138, 138);
        params.setMargins(0, 0, 40, 0);
        params.gravity = Gravity.CENTER;

        imgView.setLayoutParams(params);

        Glide.with(this).load(URL).apply(new RequestOptions().circleCrop()).into(imgView);

        return imgView;
    }

    private void customToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}