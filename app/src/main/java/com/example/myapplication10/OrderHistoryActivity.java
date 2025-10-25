package com.example.myapplication10;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication10.databinding.ActivityOrderHistoryBinding;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ActivityOrderHistoryBinding binding;
    private OrderHistoryAdapter adapter;
    private List<Order> orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thiết lập RecyclerView một lần
        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    // =================== THÊM onResume ĐỂ TỰ ĐỘNG CẬP NHẬT ===================
    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại danh sách đơn hàng mỗi khi màn hình này được hiển thị
        loadAndDisplayOrders();
    }

    private void loadAndDisplayOrders() {
        orderHistory = OrderManager.getInstance(this).getOrderList();

        if (orderHistory == null || orderHistory.isEmpty()) {
            binding.rvOrderHistory.setVisibility(View.GONE);
            binding.tvNoOrders.setVisibility(View.VISIBLE);
        } else {
            binding.rvOrderHistory.setVisibility(View.VISIBLE);
            binding.tvNoOrders.setVisibility(View.GONE);

            adapter = new OrderHistoryAdapter(this, orderHistory);
            binding.rvOrderHistory.setAdapter(adapter);
        }
    }
    // =======================================================================
}