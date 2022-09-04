package org.techtown.my_jubgging;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RecycleAdapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<RegionPost> dataList = null;

    public RecycleAdapter(ArrayList<RegionPost> dataList){
        this.dataList = dataList;
    }

    Context context;

    /* */
    int recruitingBoxColor;

    long nowMS;
    long dateMS;

    Integer attendingNum;
    Integer peopleNum;

    Calendar todayDate;
    Calendar targetDate;
    Calendar modifiedDate;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.adapter_together_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        attendingNum = dataList.get(position).getNowAttendingNum();
        peopleNum = dataList.get(position).getPeopleNum();

        setTimeSetting(position);

        viewHolder.recruitingTxt.setText(setRecruiting());
        viewHolder.recruitingTxt.setBackgroundColor(recruitingBoxColor);

        long boardId = dataList.get(position).getBoardId();

        viewHolder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadPostDetail.class);
                intent.putExtra("boardId", boardId);
                context.startActivity(intent);
            }
        });

        viewHolder.titleTxt.setText(dataList.get(position).getTitle());
        viewHolder.contentTxt.setText(dataList.get(position).getContent());

        viewHolder.startTimeTxt.setText(setDate());
        viewHolder.peopleNumTxt.setText(attendingNum + "/" + peopleNum);

        viewHolder.uploadTimeTxt.setText(setModifiedTime());

        String tmp = dataList.get(position).getRegion1();
        if (!(tmp.equals("+") || tmp.equals("")))
            viewHolder.region1Txt.setText(tmp);
        else
            viewHolder.region1Txt.setVisibility(View.INVISIBLE);

        tmp = dataList.get(position).getRegion2();
        if (!(tmp.equals("+") || tmp.equals("")))
            viewHolder.region2Txt.setText(tmp);
        else
            viewHolder.region2Txt.setVisibility(View.INVISIBLE);

        tmp = dataList.get(position).getRegion3();
        if (!(tmp.equals("+") || tmp.equals("")))
            viewHolder.region3Txt.setText(tmp);
        else
            viewHolder.region3Txt.setVisibility(View.INVISIBLE);
    }

    private void setTimeSetting(int position) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date modifiedTime = null;
        try { modifiedTime = dateFormat.parse(dataList.get(position).getModifiedTime()); }
        catch (Exception e) { }

        Date date = null;
        try { date = dateFormat.parse(dataList.get(position).getAppointmentTime()); }
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

    private String setRecruiting() {
        if ((attendingNum == peopleNum) || (nowMS >= dateMS)) {
            recruitingBoxColor = context.getResources().getColor(R.color.light_gray);
            return "모집 완료";
        } else {
            recruitingBoxColor = context.getResources().getColor(R.color.main_color_4);
            return "모집중";
        }
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
            return ((targetDate.get(Calendar.MONTH) + 1) + "월 " + targetDate.get(Calendar.DATE) + "일");

        return (targetDate.get(Calendar.YEAR) + "년 " + (targetDate.get(Calendar.MONTH) + 1) + "월 " + targetDate.get(Calendar.DATE) +"일");
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }
}
