package org.techtown.my_jubgging.pointshop;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.my_jubgging.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private static final String LOG_TAG = "ItemViewHolder";

    private ImageView itemURL;
    private TextView name;
    private TextView price;
    private ImageButton detail;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        itemURL = itemView.findViewById(R.id.point_shop_item);
        name = itemView.findViewById(R.id.point_shop_name);
        price = itemView.findViewById(R.id.point_shop_price);
        detail = itemView.findViewById(R.id.point_shop_detail_button);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG_TAG,"아이템 디테일 버튼 클릭");
            }
        });
    }

    public void setItem(Item item, Context context)
    {
        Glide.with(context).load(item.getItemURL()).into(itemURL);
        name.setText(item.getName());
        price.setText(item.getPrice());
    }
}