package org.techtown.my_jubgging.pointshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.techtown.my_jubgging.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private ImageView itemURL;
        private TextView name;
        private TextView count;
        private TextView price;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.my_profile_order_date);
            itemURL = itemView.findViewById(R.id.my_profile_order_itemURL);
            name = itemView.findViewById(R.id.my_profile_order_name);
            price = itemView.findViewById(R.id.my_profile_order_price);
            count = itemView.findViewById(R.id.my_profile_order_count);
        }
    }

    private ArrayList<Order> orderArrayList;
    private Context context;

    //생성자에서 아이템 리스트를 전달 받음
    public OrderAdapter(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }


    //아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.adapter_order_recycler, parent, false) ;
        OrderViewHolder itemViewHolder = new OrderViewHolder(view);

        return itemViewHolder;
    }

    //해당 position 의 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder,int position) {
        //itemArrayList 에 있는 아이템 사진, 이름, 가격
        Order order = orderArrayList.get(position);
        //주문 날짜 설정
        holder.date.setText(order.getOrderDate());
        //아이템 사진 설정
        Glide.with(context).load(order.getItemURL()).into(holder.itemURL);
        //아이템 이름 설정
        holder.name.setText(order.getName());
        //아이템 가격 설정
        holder.price.setText(order.getOrderPrice());

    }


    //전체 데이터 개수 리턴
    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}
