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

        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndDisplayOrders();
    }

    private void loadAndDisplayOrders() {
        orderHistory = OrderManager.getInstance(this).getAllOrders();

        if (orderHistory == null || orderHistory.isEmpty()) {
            binding.rvOrderHistory.setVisibility(View.GONE);
            binding.tvNoOrders.setVisibility(View.VISIBLE);
        } else {
            binding.rvOrderHistory.setVisibility(View.VISIBLE);
            binding.tvNoOrders.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new OrderHistoryAdapter(this, orderHistory);
                binding.rvOrderHistory.setAdapter(adapter);
            } else {
                adapter.update(orderHistory);   // thêm hàm update(list) trong adapter nếu chưa có
            }
        }
    }
}
