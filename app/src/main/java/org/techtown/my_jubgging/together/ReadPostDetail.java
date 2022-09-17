package org.techtown.my_jubgging.together;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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

import org.techtown.my_jubgging.MainMenu;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;
import org.techtown.my_jubgging.together.NewpageActivity;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReadPostDetail extends Activity {
    private RetrofitAPI retrofitApi;

    /* View Reference */
    Context context;
    UserInfo userInfo;

    LinearLayout profileLayout;
    LinearLayout openChatLayout;

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

    TextView openChatLinkTxt;
    Button copyLinkBtn;

    /* */
    int state; // 0 : 모집 중 | 1 : 참여 완료 | 2 : 모집 완료

    long userId;
    long boardId;
    long postOwnerId;
    boolean isParticipated;
    boolean isRecruiting;

    int recruitingBoxColor;
    String targetGENDER;

    long nowMS;
    long dateMS;

    Integer attendingNum;
    Integer peopleNum;

    Calendar todayDate;
    Calendar targetDate;
    Calendar modifiedDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_together_post);

        context = getApplicationContext();
        userInfo = (UserInfo)getIntent().getSerializableExtra("userInfo");
        userId = Long.parseLong(userInfo.userId);

        Intent intent = getIntent();
        boardId = intent.getLongExtra("boardId", 0L);
        //customToast(boardId + " ");

        setViewById();
        buttonsOnClickSet();

        retrofitApi = RetrofitClient.getApiService();

        getPost();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPost();
    }

    private void setViewById() {
        profileLayout = findViewById(R.id.together_post_profiles_layout);
        openChatLayout = findViewById(R.id.together_post_open_chat_layout);

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

        openChatLinkTxt = findViewById(R.id.together_post_open_chat_link_text);
        copyLinkBtn = findViewById(R.id.together_post_copy_link_button);
    }

    private void buttonsOnClickSet() {
        /* 뒤로가기 버튼 */
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingBtn();
            }
        });

        participateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isParticipated) {
                    participateBtn.setEnabled(false);
                    isParticipated = true;
                    customToast("함께 줍깅에 참여했어요!");
                    addParticipant();
                }
                else {
                    if (userId == postOwnerId) {
                        customToast("게시물 작성자는 참여 취소할 수 없어요!");
                    }
                    else {
                        participateBtn.setEnabled(false);
                        isParticipated = false;
                        customToast("참여에 취소했어요!");
                        deleteParticipant();
                    }
                }
            }
        });

        copyLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("LINK", openChatLinkTxt.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                customToast("링크가 복사되었어요!");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MainMenu.class);
        intent.putExtra("userInfo",userInfo);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void onSettingBtn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (userId == postOwnerId) {
            builder.setItems(R.array.post_setting_owner_array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        amendPost();
                    }
                    else {
                        deletePost();
                    }
                }
            })
                    .create()
                    .show();
        }
        else {
            builder.setItems(R.array.post_setting_array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    customToast("해당 게시물이 신고 되었습니다!");
                }
            })
                    .create()
                    .show();
        }
    }

    private void amendPost() {
        Intent intent = new Intent(context, NewpageActivity.class);

        intent.putExtra("userInfo", userInfo);

        intent.putExtra("isAmend", true);
        intent.putExtra("boardId", boardId);

        intent.putExtra("title", titleTxt.getText().toString());
        intent.putExtra("content", content.getText().toString());
        intent.putExtra("peopleNum", peopleNum);
        intent.putExtra("lowerBound", attendingNum);

        intent.putExtra("place", placeTxt.getText().toString());
        intent.putExtra("link", openChatLinkTxt.getText().toString());

        startActivity(intent);
    }

    private void deletePost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말 해당 게시물을 삭제하실 건가요?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Map<String, Long>> call = retrofitApi.deletePost(boardId);

                        call.enqueue(new Callback<Map<String, Long>>() {
                            @Override
                            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                                if (!response.isSuccessful()) {
                                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                                    return;
                                }

                                customToast("게시물을 삭제했어요!");
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                                customToast("게시물 삭제에 실패했어요...");
                            }
                        });
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    private void getPost() {
        Call<Map<String, Object>> call = retrofitApi.getPostDetail(boardId);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                setValue(response.body());
                if (isPossibleGender(response.body().get("possibleGender").toString().toUpperCase()))
                    isParticipate();
                else {
                    setParticipateBtnActivate(false);
                    participateBtn.setText("참여불가");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                customToast("게시물 불러오기를 실패했어요...");
            }
        });
    }

    private boolean isPossibleGender(String targetGender) {
        targetGENDER = new String(targetGender).toUpperCase();
        if (targetGender.equals("ALL"))
            return true;

        if (targetGender.equals(userInfo.gender))
            return true;

        return false;
    }

    private void isParticipate() {
        Call<Map<String, String>> call = retrofitApi.isParticipating(userId, boardId);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                Map<String, String> data = response.body();
                String realData = (String)data.get("attending");

                if(realData.equals("Y")) {
                    openChatLayout.setVisibility(View.VISIBLE);
                    isParticipated = true;

                    setParticipateBtnActivate(true);
                    participateBtn.setText("참여취소");
                }
                else {
                    openChatLayout.setVisibility(View.INVISIBLE);
                    isParticipated = false;

                    if (isRecruiting) {
                        setParticipateBtnActivate(true);
                        participateBtn.setText("참여하기");
                    }
                    else {
                        setParticipateBtnActivate(false);
                        participateBtn.setText("모집완료");
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {

            }
        });
    }

    private void addParticipant() {
        Call<Map<String, Long>> call = retrofitApi.addParticipant(boardId, userId);

        call.enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (!response.isSuccessful()) {
                    participateBtn.setEnabled(true);
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                getPost();
                participateBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                participateBtn.setEnabled(true);
            }
        });
    }

    private void deleteParticipant() {
        Call<Map<String, Long>> call = retrofitApi.deleteParticipant(boardId, userId);

        call.enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (!response.isSuccessful()) {
                    participateBtn.setEnabled(true);
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                getPost();
                participateBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {
                participateBtn.setEnabled(true);
            }
        });
    }

    private void setParticipateBtnActivate(boolean activate) {
        if (activate) {
            participateBtn.setEnabled(true);
            participateBtn.setBackgroundResource(R.drawable.rounded_rectangle);
        }
        else {
            participateBtn.setEnabled(false);
            participateBtn.setBackgroundResource(R.drawable.rounded_rectangle_gray);
        }
    }

    private void setValue(Map<String, Object> data) {
        postOwnerId = ((Double)data.get("userId")).longValue();

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

        profileLayout.removeAllViews();

        List<String> URLS = (List<String>)data.get("attendingPeopleProfileURL");
        for (int i = 0; i < attendingNum; ++i) {
            ImageView imgView = makeNewImageView(URLS.get(i));
            profileLayout.addView(imgView);
        }

        openChatLinkTxt.setText(data.get("kakaoChatAddress").toString());
    }

    private String setRecruiting() {
        // 모집 인원이 충족되거나 줍깅 날짜가 지난 경우 모집 완료
        if ((attendingNum >= peopleNum) || (nowMS >= dateMS)) {
            isRecruiting = false;
            recruitingBoxColor = context.getResources().getColor(R.color.light_gray);
            return "모집 완료";
        }

        else {
            isRecruiting = true;
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
        int minute = targetDate.get(Calendar.MINUTE);
        String min = (minute != 0)? minute + "분" : "";

        if (todayDate.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR))
            return ((targetDate.get(Calendar.MONTH) + 1) + "월 " + targetDate.get(Calendar.DATE) + "일 " +
                    targetDate.get(Calendar.HOUR_OF_DAY) + "시 " + min);

        return (targetDate.get(Calendar.YEAR) + "년 " + (targetDate.get(Calendar.MONTH) + 1) + "월 " + targetDate.get(Calendar.DATE) +"일" +
                targetDate.get(Calendar.HOUR_OF_DAY) + "시 " + min);
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
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}