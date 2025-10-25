package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Bắt sự kiện click cho nút Sign Up
        binding.btnSignUp.setOnClickListener(v -> {
            registerUser();
        });

        // =================== THÊM SỰ KIỆN CLICK MỚI ===================
        // Bắt sự kiện click cho TextView "Login" để quay lại màn hình đăng nhập
        binding.tvGoToLogin.setOnClickListener(v -> {
            // Tạo Intent để mở LoginActivity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            // Đóng màn hình hiện tại
            finish();
        });
        // ============================================================
    }

    private void registerUser() {
        // =================== SỬA LẠI CÁCH LẤY TEXT ===================
        // Lấy text từ các TextInputEditText mới
        String email = binding.etEmailSignup.getText().toString().trim();
        String password = binding.etPasswordSignup.getText().toString().trim();
        String confirmPassword = binding.etConfirmPasswordSignup.getText().toString().trim();
        // ============================================================

        // Kiểm tra dữ liệu đầu vào (giữ nguyên)
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm của Firebase để tạo người dùng (giữ nguyên)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        Log.d("FIREBASE_AUTH", "createUserWithEmail:success");
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();

                        // Chuyển người dùng đến màn hình Đăng nhập
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Đóng màn hình đăng ký

                    } else {
                        // Đăng ký thất bại
                        Log.w("FIREBASE_AUTH", "createUserWithEmail:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this, "Email này đã được đăng ký.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}