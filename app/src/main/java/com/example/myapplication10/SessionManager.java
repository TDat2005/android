package com.example.myapplication10;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Key cho Firebase Auth
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_EMAIL = "email";

    // Key cho chức năng "Remember Me"
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_REMEMBERED_EMAIL = "remembered_email";
    private static final String KEY_REMEMBERED_PASSWORD = "remembered_password";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // --- Quản lý phiên đăng nhập ---
    public void saveAuthToken(String token, String email) {
        editor.putString(KEY_AUTH_TOKEN, token);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "user@example.com");
    }

    // =================== PHƯƠNG THỨC GÂY LỖI ĐÃ ĐƯỢC THÊM LẠI ===================
    // Phương thức này dùng để lấy tên người dùng (chính là email)
    public String getUsername() {
        return sharedPreferences.getString(KEY_EMAIL, "Guest");
    }
    // ========================================================================

    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }

    public void clearSession() {
        editor.remove(KEY_AUTH_TOKEN);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }

    // --- Quản lý chức năng "Remember Me" ---
    public void saveRememberMe(boolean remember, String email, String password) {
        editor.putBoolean(KEY_REMEMBER_ME, remember);
        if (remember) {
            editor.putString(KEY_REMEMBERED_EMAIL, email);
            editor.putString(KEY_REMEMBERED_PASSWORD, password);
        } else {
            editor.remove(KEY_REMEMBERED_EMAIL);
            editor.remove(KEY_REMEMBERED_PASSWORD);
        }
        editor.apply();
    }

    public boolean isRememberMeChecked() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }

    public String getRememberedEmail() {
        return sharedPreferences.getString(KEY_REMEMBERED_EMAIL, "");
    }

    public String getRememberedPassword() {
        return sharedPreferences.getString(KEY_REMEMBERED_PASSWORD, "");
    }
}