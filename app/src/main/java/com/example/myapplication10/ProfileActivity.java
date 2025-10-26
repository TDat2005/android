package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivityProfileBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        findViewById(R.id.cardOrderHistory).setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, InvoiceHistoryActivity.class);
            startActivity(i);
        });


        // Lấy thông tin từ SessionManager và hiển thị
        String email = sessionManager.getEmail();

        // Lấy phần trước dấu @ của email để làm username
        if (email != null && !email.isEmpty() && email.contains("@")) {
            binding.tvUsername.setText(email.split("@")[0]);
        } else {
            binding.tvUsername.setText("Guest User");
        }
        binding.tvEmail.setText(email);

        // Theo dõi đơn hàng (LinearLayout id: btn_order_history -> binding: btnOrderHistory)
        binding.btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // ĐỔI MẬT KHẨU (LinearLayout id: row_change_password -> binding: rowChangePassword)
        // Nhớ đã thêm mục này trong activity_profile.xml
        if (binding.rowChangePassword != null) {
            binding.rowChangePassword.setOnClickListener(v -> showChangePasswordSheet());
        }
        binding.btnOrderHistory.setOnClickListener(v -> {
            Intent i = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(i);
        });

        // Đăng xuất
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

    // ============= Đổi mật khẩu =============
    private void showChangePasswordSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.layout_change_password_sheet, null);
        dialog.setContentView(view);

        TextInputEditText etCurrent = view.findViewById(R.id.etCurrentPassword);
        TextInputEditText etNew = view.findViewById(R.id.etNewPassword);
        TextInputEditText etConfirm = view.findViewById(R.id.etConfirmPassword);
        MaterialButton btnSubmit = view.findViewById(R.id.btnSubmitChange);
        View tvForgot = view.findViewById(R.id.tvForgot);

        btnSubmit.setOnClickListener(v -> {
            String current = etCurrent.getText() != null ? etCurrent.getText().toString().trim() : "";
            String nw = etNew.getText() != null ? etNew.getText().toString().trim() : "";
            String cf = etConfirm.getText() != null ? etConfirm.getText().toString().trim() : "";

            if (current.isEmpty() || nw.isEmpty() || cf.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nw.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải từ 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!nw.equals(cf)) {
                Toast.makeText(this, "Xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            reauthAndUpdatePassword(current, nw, dialog);
        });

        // (Tuỳ chọn) Quên mật khẩu -> gửi email đặt lại
        if (tvForgot != null) {
            tvForgot.setOnClickListener(v -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && user.getEmail() != null) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(this, "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Gửi email thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show());
                } else {
                    Toast.makeText(this, "Bạn chưa đăng nhập.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        dialog.show();
    }

    private void reauthAndUpdatePassword(String currentPassword, String newPassword, BottomSheetDialog dialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || user.getEmail() == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reauthenticate with current password
        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), currentPassword))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update to new password
                        user.updatePassword(newPassword).addOnCompleteListener(upd -> {
                            if (upd.isSuccessful()) {
                                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                            } else {
                                String msg = upd.getException() != null ? upd.getException().getMessage() : "Lỗi không xác định";
                                Toast.makeText(this, "Đổi mật khẩu thất bại: " + msg, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        String msg = task.getException() != null ? task.getException().getMessage() : "Xác thực thất bại";
                        Toast.makeText(this, "Sai mật khẩu hiện tại hoặc cần đăng nhập lại: " + msg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
