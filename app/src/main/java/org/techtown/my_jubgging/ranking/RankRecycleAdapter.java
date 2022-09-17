package org.techtown.my_jubgging.ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.together.ViewHolder;

import java.util.ArrayList;

public class RankRecycleAdapter extends RecyclerView.Adapter<RankViewHolder> {
    private ArrayList<RankInfo> dataList = null;
    Context context;

    public RankRecycleAdapter(ArrayList<RankInfo> dataList) { this.dataList = dataList; }

    /* For TypeCast */
    Integer rank;
    Integer walkingNum;

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.adapter_ranking_recycler, parent, false);

        RankViewHolder rankViewHolder = new RankViewHolder(view);

        return rankViewHolder;
    }

    @Override
    public void onBindViewHolder(RankViewHolder viewHolder, int position)
    {
        rank = dataList.get(position).getRank();
        walkingNum = dataList.get(position).getWalkingNum();

        viewHolder.rankNum.setText(rank.toString() + "위");
        viewHolder.walkingCnt.setText("걸음 수 : " + walkingNum.toString());
        viewHolder.nickName.setText(dataList.get(position).getUserNickName());

        if (RankingFragment.userId == dataList.get(position).userId)
            viewHolder.bodyLayout.setBackgroundResource(R.drawable.outline_rectangle_bold);
        else
            viewHolder.bodyLayout.setBackgroundResource(R.drawable.outline_rectangle);

        Glide.with(context).load(dataList.get(position).getProfileURL()).apply(new RequestOptions().circleCrop()).into(viewHolder.profileImg);
    }

    @Override
    public int getItemCount() { return dataList.size(); }
}
