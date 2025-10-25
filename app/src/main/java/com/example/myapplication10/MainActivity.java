package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager; // Khai báo SessionManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(this);

        // **LOGIC QUAN TRỌNG NHẤT LÀ Ở ĐÂY**
        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (sessionManager.isLoggedIn()) {
            // Nếu đã đăng nhập, chuyển thẳng đến HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Đóng MainActivity lại để không quay lại được
            return; // Dừng việc thực thi các code còn lại trong onCreate
        }

        // Nếu chưa đăng nhập, thì mới hiển thị layout của MainActivity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Gán sự kiện cho các nút như bình thường
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}