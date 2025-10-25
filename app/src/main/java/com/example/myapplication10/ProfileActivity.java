package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        // Lấy thông tin từ SessionManager và hiển thị lên các view mới
        String email = sessionManager.getEmail();

        // Lấy phần trước dấu @ của email để làm username
        if (email != null && !email.isEmpty() && email.contains("@")) {
            binding.tvUsername.setText(email.split("@")[0]);
        } else {
            binding.tvUsername.setText("Guest User");
        }
        binding.tvEmail.setText(email);


        // Bắt sự kiện click vào nút Theo dõi đơn hàng
        binding.btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Bắt sự kiện click vào nút Đăng xuất
        binding.btnLogout.setOnClickListener(v -> {
            // Đăng xuất khỏi Firebase
            FirebaseAuth.getInstance().signOut();
            // Xóa session đã lưu
            sessionManager.clearSession();
            // Chuyển về màn hình Login và xóa các màn hình cũ
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}