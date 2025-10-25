package com.example.myapplication10;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication10.databinding.ActivityOrderDetailBinding;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentOrder = (Order) getIntent().getSerializableExtra("ORDER_OBJECT");

        if (currentOrder == null) {
            Toast.makeText(this, "Không thể tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupToolbar();
        displayOrderDetails();
        setupCancellationWithDelay();
    }

    private void setupToolbar() {
        binding.toolbarOrderDetail.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void displayOrderDetails() {
        List<CartItem> items = currentOrder.getItems();
        if (items != null && !items.isEmpty()) {
            binding.rvOrderProducts.setVisibility(View.VISIBLE);
            OrderDetailAdapter adapter = new OrderDetailAdapter(items, this);
            binding.rvOrderProducts.setLayoutManager(new LinearLayoutManager(this));
            binding.rvOrderProducts.setAdapter(adapter);
        } else {
            binding.rvOrderProducts.setVisibility(View.GONE);
        }
    }

    // =================== HÀM MỚI ĐỂ XỬ LÝ HỦY ĐƠN VỚI ĐỘ TRỄ 10 GIÂY ===================
    private void setupCancellationWithDelay() {
        binding.btnCancelOrder.setOnClickListener(v -> {
            // 1. Vô hiệu hóa nút ngay lập tức để người dùng không bấm nhiều lần
            binding.btnCancelOrder.setEnabled(false);
            binding.btnCancelOrder.setText("Đang xử lý hủy...");

            // 2. Hiển thị thông báo cho người dùng biết
            Toast.makeText(this, "Đang gửi yêu cầu hủy đơn hàng, vui lòng chờ...", Toast.LENGTH_SHORT).show();

            // 3. Tạo một Handler để thực hiện hành động sau 5giây
            new Handler(Looper.getMainLooper()).postDelayed(() -> {


                // 3.1. Gọi OrderManager để xóa đơn hàng khỏi bộ nhớ
                OrderManager.getInstance(this).removeOrder(currentOrder.getOrderId());

                // 3.2. Hiển thị thông báo thành công
                Toast.makeText(this, "Đã hủy đơn hàng thành công!", Toast.LENGTH_LONG).show();

                // 3.3. Đóng màn hình chi tiết để quay lại lịch sử
                finish();

            }, 5000); // 10000 milliseconds = 5 giây
        });
    }
    // =================================================================================
}