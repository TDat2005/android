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

        // --- Init ---
        sessionManager = new SessionManager(this);
        totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);

        // --- Hiển thị thông tin người dùng + số tiền ---
        binding.tvNameValue.setText(sessionManager.getUsername());
        binding.tvEmailValue.setText(sessionManager.getEmail());
        binding.tvAmountValue.setText(MoneyUtils.vnd(totalAmount));



        // --- Xác nhận đặt hàng ---
        binding.btnConfirmPurchase.setOnClickListener(v -> showConfirmationDialog());
    }

    // ========================= CHECKOUT =========================

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận Thanh toán")
                .setMessage("Bạn có chắc chắn muốn hoàn tất đơn hàng này không?")
                .setPositiveButton("Xác nhận", (dialog, which) -> processOrder())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void processOrder() {
        // 1) Tạo đơn hàng mới
        String orderId = UUID.randomUUID().toString().substring(0, 8);
        long orderDateTimestamp = System.currentTimeMillis();
        List<CartItem> orderedItems = new ArrayList<>(CartManager.getInstance().getCartItems());

        Order newOrder = new Order(orderId, orderDateTimestamp, totalAmount, orderedItems);
        OrderManager.getInstance(this).addOrder(newOrder);
        CartManager.getInstance().clearCart();

        // 2) Chuyển sang màn hóa đơn chi tiết
        Intent intent = new Intent(this, OrderInvoiceActivity.class);
        intent.putExtra("ORDER_ID", orderId);
        intent.putExtra("TOTAL_AMOUNT", totalAmount);

        // Lấy thông tin người dùng từ SessionManager (đỡ cần currentAddress)
        intent.putExtra("NAME", sessionManager.getUsername());
        intent.putExtra("PHONE", sessionManager.getEmail()); // có thể thay = số điện thoại nếu có
        intent.putExtra("ADDRESS", "Không có địa chỉ cụ thể"); // hoặc để trống nếu chưa có DB địa chỉ

        startActivity(intent);
        finish();
    }


}
