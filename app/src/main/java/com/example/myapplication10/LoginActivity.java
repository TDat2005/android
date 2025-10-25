package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager sessionManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();

        // Xử lý chức năng "Remember me"
        loadRememberedCredentials();

        binding.btnLogin.setOnClickListener(v -> {
            // Lấy dữ liệu từ TextInputEditText bên trong TextInputLayout
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ email và password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu trạng thái "Remember me" trước khi đăng nhập
            sessionManager.saveRememberMe(binding.cbRememberMe.isChecked(), email, password);

            performFirebaseLogin(email, password);
        });

        // Xử lý sự kiện click cho "Forgot password?" (chỉ hiển thị Toast)
        binding.tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng này sẽ được phát triển sau!", Toast.LENGTH_SHORT).show();
        });
    }

    // Hàm để tải lại thông tin nếu người dùng đã chọn "Remember me"
    private void loadRememberedCredentials() {
        if (sessionManager.isRememberMeChecked()) {
            binding.etEmail.setText(sessionManager.getRememberedEmail());
            binding.etPassword.setText(sessionManager.getRememberedPassword());
            binding.cbRememberMe.setChecked(true);
        }
    }

    private void performFirebaseLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("FIREBASE_AUTH", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Lưu phiên đăng nhập
                        sessionManager.saveAuthToken(user.getUid(), user.getEmail());

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển đến màn hình Home
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.w("FIREBASE_AUTH", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}