package org.techtown.my_jubgging.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.techtown.my_jubgging.PloggingInfo;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.RegionPost;
import org.techtown.my_jubgging.retrofit.RetrofitAPI;
import org.techtown.my_jubgging.retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;
    private RetrofitAPI retrofitApi;

    Context context;
    ViewGroup rootView;

    static private int goalWalkingNum = 10000;
    long userId = 19L; //< FIXME
    String targetDate;

    /* */
    TextView calorieTxt;
    TextView accumulatedTimeTxt;
    TextView kmTxt;

    LinearLayout circleLayout;
    TextView yearTxt;
    TextView dateTxt;
    TextView warkingCntTxt;

    View percentBar[];
    ImageView percentCircle[];

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

        setViewById();
        setOnClick();

        Calendar today = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = today.getTime();

        targetDate = dateFormat.format(date);
        getPloggingInfo(targetDate);

        drawPercentBar(7295);

        return rootView;
    }

    private void setViewById() {
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
    }

    private void setOnClick() {
        circleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //savePloggingInfo();
                customToast("save clicked");
            }
        });
    }

    private void drawPercentBar(int walkingNum) {
        int idx = (int)((double)walkingNum / (double)goalWalkingNum * (double)6);
        Log.d("ASD", idx + " ");

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
    }

    /*
    private void showDate() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                year = y;
                month = m + 1;
                date = d;


                if (y != calendar.get(Calendar.YEAR))
                    dateBtn.setText(year + "년 " + month + "월 " + date + "일");
                else
                    dateBtn.setText(month + "월 " + date + "일");

                isDateSet = true;


            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        //int textColor = ContextCompat.getColor(getApplicationContext(), R.color.text_color);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(textColor);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(textColor);
    }
*/

    private void getPloggingInfo(String date) {
        /*
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("userId", userId);
        query.put("date", dateStr);

         */
        Call<Map<String, Object>> call = retrofitApi.getPloggingInfo(userId, date);

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

    private void savePloggingInfo() {
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
        calorieTxt.setText(data.get("calorie").toString());
        warkingCntTxt.setText(data.get("walkingNum").toString());
        accumulatedTimeTxt.setText(data.get("walkingTime").toString());
        kmTxt.setText(data.get("kilometer").toString());
    }

    private void customToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}