package org.techtown.my_jubgging.pointshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.techtown.my_jubgging.R;
import org.techtown.my_jubgging.UserInfo;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemURL;
        private TextView name;
        private TextView price;
        private ImageButton detail;

        public ItemViewHolder(@NonNull View itemView, UserInfo userInfo) {
            super(itemView);

            itemURL = itemView.findViewById(R.id.point_shop_item);
            name = itemView.findViewById(R.id.point_shop_name);
            price = itemView.findViewById(R.id.point_shop_price);
            detail = itemView.findViewById(R.id.point_shop_detail_button);
        }
    }

    private ArrayList<Item> itemArrayList;
    private Context context;
    private UserInfo userInfo;

    //생성자에서 아이템 리스트를 전달 받음
    public ItemAdapter(ArrayList<Item> itemArrayList, UserInfo userInfo) {
        this.itemArrayList = itemArrayList;
        this.userInfo = userInfo;
    }


    //아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.adapter_pointshop_recycler, parent, false) ;
        ItemViewHolder itemViewHolder = new ItemViewHolder(view,userInfo);

        return itemViewHolder;
    }

    //해당 position 의 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //itemArrayList 에 있는 아이템 사진, 이름, 가격
        Item item = itemArrayList.get(position);
        //아이템 사진 설정
        Glide.with(context).load(item.getItemURL()).into(holder.itemURL);
        //아이템 이름 설정
        holder.name.setText(item.getName());
        //아이템 가격 설정
        holder.price.setText(item.getPrice());

        //해당 아이템 클릭시
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ItemDetail.class);
                //itemId, userId 전달
                intent.putExtra("userId",userInfo.getUserId());
                intent.putExtra("itemId",itemArrayList.get(position).getItemId());

                //아이템의 상세정보를 띄워주는 인텐트 실행
                view.getContext().startActivity(intent);
            }
        });
    }


    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }
}
