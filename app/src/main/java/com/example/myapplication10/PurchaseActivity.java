package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivityPurchaseBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PurchaseActivity extends AppCompatActivity {

    private ActivityPurchaseBinding binding;
    private SessionManager sessionManager;
    private double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);

        // Hiển thị thông tin
        String username = sessionManager.getUsername();
        String email = sessionManager.getEmail(); // Lấy email thật từ SessionManager
        binding.tvNameValue.setText(username);
        binding.tvEmailValue.setText(email); // Hiển thị email thật

        binding.tvAmountValue.setText(String.format(Locale.US, "$%.2f", totalAmount));

        // Xử lý nút xác nhận
        binding.btnConfirmPurchase.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận Thanh toán")
                .setMessage("Bạn có chắc chắn muốn hoàn tất đơn hàng này không?")
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    processOrder();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // =================== HÀM ĐƯỢC SỬA LỖI ===================
    private void processOrder() {
        // 1. Tạo một đơn hàng mới
        String orderId = UUID.randomUUID().toString().substring(0, 8);
        // Lấy thời gian hiện tại dưới dạng long (miliseconds)
        long orderDateTimestamp = System.currentTimeMillis();
        // Lấy danh sách sản phẩm từ giỏ hàng
        List<CartItem> orderedItems = new ArrayList<>(CartManager.getInstance().getCartItems());

        // Tạo đối tượng Order với đúng kiểu dữ liệu
        Order newOrder = new Order(orderId, orderDateTimestamp, totalAmount, orderedItems);

        // 2. Thêm đơn hàng vào lịch sử
        OrderManager.getInstance(this).addOrder(newOrder);

        // 3. Xóa giỏ hàng hiện tại
        CartManager.getInstance().clearCart();

        // 4. Hiển thị thông báo thành công và quay về Home
        Toast.makeText(this, "Thanh toán thành công! Đơn hàng của bạn đang được xử lý.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    // ==========================================================
}