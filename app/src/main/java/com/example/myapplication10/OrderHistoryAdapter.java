package com.example.myapplication10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // =================== PHẦN SỬA LỖI HIỂN THỊ NGÀY ===================
        // Định dạng lại ngày tháng từ long sang String
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(new Date(order.getOrderDate()));

        holder.tvOrderId.setText(order.getOrderId());
        holder.tvOrderDate.setText(formattedDate); // Sử dụng ngày đã được định dạng
        holder.tvTotalAmount.setText(MoneyUtils.vnd(order.getTotalPrice()));
        // =================================================================

        // =================== THÊM CHỨC NĂNG XEM CHI TIẾT ===================
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            // Gửi cả đối tượng Order qua Intent
            intent.putExtra("ORDER_OBJECT", order);
            context.startActivity(intent);
        });
        // =================================================================
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvTotalAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvTotalAmount = itemView.findViewById(R.id.tv_total_amount);
        }
    }
    public void update(List<Order> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }

}