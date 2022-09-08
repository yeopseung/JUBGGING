package org.techtown.my_jubgging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.usermgmt.response.model.User;

import org.techtown.my_jubgging.ranking.RankInfo;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JubggingResultActivity extends AppCompatActivity {
    Context context;
    UserInfo userInfo;
    RetrofitAPI retrofitAPI;

    LinearLayout intagramLayout;

    long runTime;
    int walkingNum;
    Double calorie;
    Double kilometer;
    boolean isPhotoInserted = false;

    /* */
    TextView kmTxt;
    TextView timeTxt;
    TextView calorieTxt;

    ImageButton addPhotoBtn;
    ImageButton shareStoryBtn;

    Button exitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jubgging_result);

        context = getApplicationContext();
        userInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        getViewById();
        setValue();
        setOnClick();
    }
    private void getViewById() {
        intagramLayout = findViewById(R.id.jubgging_result_instagram_layout);

        kmTxt = findViewById(R.id.jubgging_result_kilometer_text);
        timeTxt = findViewById(R.id.jubgging_result_time_text);
        calorieTxt = findViewById(R.id.jubgging_result_calorie_text);

        addPhotoBtn = findViewById(R.id.jubgging_result_add_photo_button);
        shareStoryBtn = findViewById(R.id.jubgging_result_share_story_button);

        exitBtn = findViewById(R.id.jubgging_result_exit_button);
    }

    private void setValue() {
        Intent intent = getIntent();

        walkingNum = intent.getIntExtra("step", 0);
        runTime = (long)intent.getLongExtra("runtime", 0L);

        calorie = (double)walkingNum * 0.04;
        kilometer = (double)walkingNum * 0.00071429;

        kmTxt.setText(String.format("%.1f", kilometer));
        calorieTxt.setText(String.format("%.1f", calorie));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(runTime);
        if (runTime < 60 * 60 * 1000)
            date.setHours(0);

        timeTxt.setText(timeFormat.format(date));
    }

    private void setOnClick() {
        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        shareStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AAA", "log");
                Uri stickerAssetUri = Uri.parse(addPhotoBtn.getResources().toString());
                String sourceApplication = "org.techtown.my_jubgging";

                Intent intent = new Intent("com.instagram.share.ADD_TO_STORY");
                intent.putExtra("source_application", sourceApplication);

                intent.setType("image/*");
                intent.putExtra("interactive_asset_uri", stickerAssetUri);

                context.grantUriPermission(
                        "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (getPackageManager().resolveActivity(intent, 0) != null) {
                    startActivity(intent);
                }
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainMenu.class);
                intent.putExtra("userInfo",userInfo); //< FIXME
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            addPhotoBtn.setImageBitmap(imageBitmap);
            addPhotoBtn.setScaleType(ImageView.ScaleType.FIT_XY);

            isPhotoInserted = true;
            intagramLayout.setVisibility(View.VISIBLE);

            savePoint();
        }
    }

    private void savePoint() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", Long.parseLong(userInfo.userId));
        data.put("walkingNum", walkingNum);
        Call<Map<String, Integer>> call = retrofitAPI.addPoint(data);

        call.enqueue(new Callback<Map<String, Integer>>() {
             @Override
             public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                 if (!response.isSuccessful()) {
                     customToast("Code : " + response.code() + response.message() + response.errorBody());
                     return;
                 }

                 customToast("포인트 적립 성공!" + response.body().get("nowPoint"));
             }

             @Override
             public void onFailure(Call<Map<String, Integer>> call, Throwable t) {

             }
         });
    }

    private void customToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
