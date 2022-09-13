package org.techtown.my_jubgging.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.my_jubgging.JubggingActivity;
import org.techtown.my_jubgging.MainMenu;
import org.techtown.my_jubgging.MyProfile;
import org.techtown.my_jubgging.PloggingInfo;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.ReadPostDetail;
import org.techtown.my_jubgging.UserInfo;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private RetrofitAPI retrofitApi;
    UserInfo userInfo;

    Context context;
    ViewGroup rootView;

    static private int goalWalkingNum = 10000;
    long userId = 0L; //< FIXME
    String targetDate;

    int textColor;

    /* */
    ImageButton profileImgBtn;

    TextView calorieTxt;
    TextView accumulatedTimeTxt;
    TextView kmTxt;

    LinearLayout circleLayout;
    TextView yearTxt;
    TextView dateTxt;
    TextView warkingCntTxt;

    View percentBar[];
    ImageView percentCircle[];
    Space leftSpace;
    Space rightSpace;

    LinearLayout reservedTogetherLayout;
    TextView startPloggingTxt;

    /* */
    int year;
    int month;
    int date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);

        context = getActivity();

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitApi = retrofit.create(RetrofitAPI.class);

        Intent data = getActivity().getIntent();

        if (userInfo == null)
            userInfo = (UserInfo)data.getSerializableExtra("userInfo");

        userId = Long.parseLong(userInfo.userId);

        textColor = ContextCompat.getColor(context, R.color.text_color);

        setViewById();
        setOnClick();

        Calendar today = Calendar.getInstance();
        getPloggingInfo(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DATE));
        getReservedPloggingList();

        return rootView;
        //
    }

    private void setViewById() {
        profileImgBtn = rootView.findViewById(R.id.main_my_profile_button);

        calorieTxt = rootView.findViewById(R.id.home_calorie_text);
        accumulatedTimeTxt = rootView.findViewById(R.id.home_accumulated_time_text);
        kmTxt = rootView.findViewById(R.id.home_moving_km_text);

        circleLayout = rootView.findViewById(R.id.home_circle_layout);
        yearTxt = rootView.findViewById(R.id.home_in_circle_year_Text);
        dateTxt = rootView.findViewById(R.id.home_in_circle_date_Text);
        warkingCntTxt = rootView.findViewById(R.id.home_in_circle_warking_count_text);

        percentBar = new View[6];
        percentCircle = new ImageView[6];

        percentBar[0] = rootView.findViewById(R.id.home_percent_bar1);
        percentBar[1] = rootView.findViewById(R.id.home_percent_bar2);
        percentBar[2] = rootView.findViewById(R.id.home_percent_bar3);
        percentBar[3] = rootView.findViewById(R.id.home_percent_bar4);
        percentBar[4] = rootView.findViewById(R.id.home_percent_bar5);
        percentBar[5] = rootView.findViewById(R.id.home_percent_bar6);

        percentCircle[0] = rootView.findViewById(R.id.home_percent_circle1);
        percentCircle[1] = rootView.findViewById(R.id.home_percent_circle2);
        percentCircle[2] = rootView.findViewById(R.id.home_percent_circle3);
        percentCircle[3] = rootView.findViewById(R.id.home_percent_circle4);
        percentCircle[4] = rootView.findViewById(R.id.home_percent_circle5);
        percentCircle[5] = rootView.findViewById(R.id.home_percent_circle6);

        leftSpace = rootView.findViewById(R.id.home_percent_left_space);
        rightSpace = rootView.findViewById(R.id.home_percent_right_space);

        reservedTogetherLayout = rootView.findViewById(R.id.home_reserved_plogging_layout);
        startPloggingTxt = rootView.findViewById(R.id.home_start_plogging_text);
    }

    private void setOnClick() {
        circleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
                //savePloggingInfo();
                //customToast("save clicked");
            }
        });

        startPloggingTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JubggingActivity.class);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);
            }
        });

        profileImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyProfile.class);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);
            }
        });
    }

    private void drawPercentBar(int walkingNum) {
        int idx = (int)((double)walkingNum / (double)goalWalkingNum * (double)6);

        int green = rootView.getResources().getColor(R.color.main_color_2);
        int gray = rootView.getResources().getColor(R.color.gray);

        if (idx >= 6)
            idx = 6;

        for (int i = 0; i < idx; ++i) {
            percentCircle[i].setImageResource(R.drawable.ic_baseline_circle_green_24);
            percentBar[i].setBackgroundColor(green);
        }
        for (int i = idx; i < 6; ++i) {
            percentCircle[i].setImageResource(R.drawable.ic_baseline_circle_gray_24);
            percentBar[i].setBackgroundColor(gray);
        }

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.weight = (float)idx;
        leftSpace.setLayoutParams(params1);

        params2.weight = (float)(6 - idx);
        rightSpace.setLayoutParams(params2);
    }

    private void showDate() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                year = y;
                month = m + 1;
                date = d;

                // 년도 출력 여부
                if (year == calendar.get(Calendar.YEAR))
                    yearTxt.setVisibility(View.INVISIBLE);
                else {
                    yearTxt.setText(year + "년");
                    yearTxt.setVisibility(View.VISIBLE);
                }

                getPloggingInfo(year, month, date);
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();

        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(textColor);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(textColor);
    }

    private void getPloggingInfo(int year, int month, int date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTxt.setText(month + "월 " + date + "일");

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, date);
        Date target = cal.getTime();
        targetDate = dateFormat.format(target);

        Call<Map<String, Object>> call = retrofitApi.getPloggingInfo(userId, targetDate);

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                Map<String, Object> data = response.body();
                setValue(data);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                customToast("Fail : " + t.getMessage());
            }
        });
    }

    private void getReservedPloggingList() {
        Call<Map<String, List<Object>>> call = retrofitApi.getReservedPloggingList(userId);

        call.enqueue(new Callback<Map<String, List<Object>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Object>>> call, Response<Map<String, List<Object>>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                Map<String, List<Object>> huskData = response.body();
                List<Object> realData = huskData.get("Results");

                customToast(realData.size() + " ddd");

                reservedTogetherLayout.removeAllViews();
                for (int i = 0; i < realData.size(); ++i) {
                    Map<String, Object> data = (Map<String, Object>) realData.get(i);
                    Long boardId = ((Double)(data.get("boardId"))).longValue();
                    String dateStr = (String)data.get("localDateTime");
                    String place = (String)data.get("place");
                    String today = (String)data.get("today");

                    boolean isToady = (today.equals("Y"))? true : false;

                    LinearLayout newLine = makeNewLine(boardId, dateStr, place, isToady);

                    reservedTogetherLayout.addView(newLine);
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<Object>>> call, Throwable t) {

            }
        });
    }

    LinearLayout makeNewLine(long boardId, String dateStr, String place, boolean isToday) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setPadding(40, 40, 40, 40);
        linearLayout.setBackground(getResources().getDrawable(R.drawable.outline_rectangle));

        LinearLayout.LayoutParams paramsMW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsMW.topMargin = 15;
        paramsMW.bottomMargin = 15;
        linearLayout.setLayoutParams(paramsMW);

        // Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(dateStr));
        }catch (Exception e) { }

        TextView date = new TextView(context);
        date.setGravity(Gravity.CENTER_VERTICAL);
        date.setText((cal.get(Calendar.MONTH) + 1)+ "/" + cal.get(Calendar.DATE));
        date.setTextColor(textColor);
        date.setTypeface(date.getTypeface(), Typeface.BOLD);
        date.setTextSize(24);

        LinearLayout.LayoutParams params181W = new LinearLayout.LayoutParams(196, ViewGroup.LayoutParams.WRAP_CONTENT);
        date.setLayoutParams(params181W);

        linearLayout.addView(date);

        // Bar
        View bar = new View(context);
        bar.setBackground(getResources().getDrawable(R.color.text_color_light));

        LinearLayout.LayoutParams params4M = new LinearLayout.LayoutParams(4, ViewGroup.LayoutParams.MATCH_PARENT);
        params4M.leftMargin = 15;
        params4M.rightMargin = 15;
        bar.setLayoutParams(params4M);

        linearLayout.addView(bar);

        // Place
        TextView placeTxt = new TextView(context);
        placeTxt.setGravity(Gravity.CENTER_VERTICAL);
        placeTxt.setText(place);
        placeTxt.setTextColor(textColor);
        placeTxt.setTypeface(placeTxt.getTypeface(), Typeface.BOLD);
        placeTxt.setTextSize(18);

        LinearLayout.LayoutParams paramsWM = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        placeTxt.setLayoutParams(paramsWM);

        linearLayout.addView(placeTxt);

        // Today
        if (isToday) {
            TextView todayTxt = new TextView(context);
            todayTxt.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle));
            todayTxt.setGravity(Gravity.CENTER);
            todayTxt.setPadding(24, 0, 24, 0);

            todayTxt.setText("오늘");
            todayTxt.setTextColor(textColor);
            todayTxt.setTypeface(todayTxt.getTypeface(), Typeface.BOLD);
            todayTxt.setTextSize(36);

            LinearLayout.LayoutParams paramsWW = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            todayTxt.setLayoutParams(paramsWW);

            linearLayout.addView(todayTxt);
        }

        // Space
        Space space = new Space(context);

        LinearLayout.LayoutParams params0M = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params0M.weight = 1;
        space.setLayoutParams(params0M);

        linearLayout.addView(space);

        // ">"
        ImageView image = new ImageView(context);
        image.setImageResource(R.drawable.ic_baseline_chevron_right_24);

        LinearLayout.LayoutParams params3232 = new LinearLayout.LayoutParams(81, 81);
        params3232.gravity = Gravity.CENTER_VERTICAL;
        image.setLayoutParams(params3232);

        linearLayout.addView(image);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadPostDetail.class);
                intent.putExtra("boardId", boardId);
                intent.putExtra("userInfo",userInfo);
                context.startActivity(intent);
            }
        });

        return linearLayout;
    }


    private void savePloggingInfo() {
        //< FIXME
        int walkingNum = 123;
        String walkingTime = "01:23:45";

        PloggingInfo info = new PloggingInfo();
        info.userId = userId;
        info.walkingNum = walkingNum;
        info.walkingTime = walkingTime;

        Call<Map<String, Long>> call = retrofitApi.savePloggingInfo(info);

        call.enqueue(new Callback<Map<String, Long>>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                if (!response.isSuccessful()) {
                    customToast("Code : " + response.code() + response.message() + response.errorBody());
                    return;
                }

                Map<String, Long> data = response.body();
                customToast(data.get("userId").toString());
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {

            }
        });

    }

    private void setValue(Map<String, Object> data) {
        Integer calorie = ((Double)data.get("calorie")).intValue();
        Integer walkingNum = ((Double)data.get("walkingNum")).intValue();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("HH'H' mm'M'");
        Date walkingTime = null;
        try { walkingTime = dateFormat.parse(data.get("walkingTime").toString()); }
        catch (Exception e) { walkingTime = new Date(0); }

        String walkingTimeStr = newFormat.format(walkingTime);

        calorieTxt.setText(calorie.toString());
        warkingCntTxt.setText(walkingNum.toString());
        accumulatedTimeTxt.setText(walkingTimeStr);
        kmTxt.setText(String.format("%.1f" , data.get("kilometer")));

        drawPercentBar(walkingNum);

        if (userInfo != null)
            Glide.with(this).load(userInfo.profileURL).apply(new RequestOptions().circleCrop()).into(profileImgBtn);
    }

    private void customToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
