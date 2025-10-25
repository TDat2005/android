package com.example.myapplication10;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static OrderManager instance;
    private List<Order> orderList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static final String PREF_NAME = "OrderHistory";
    private static final String KEY_ORDERS = "orders";

    private OrderManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadOrders();
    }

    public static synchronized OrderManager getInstance(Context context) {
        if (instance == null) {
            instance = new OrderManager(context.getApplicationContext());
        }
        return instance;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void addOrder(Order order) {
        orderList.add(0, order);
        saveOrders();
    }

    // =================== HÀM MỚI ĐỂ XÓA ĐƠN HÀNG ===================
    public void removeOrder(String orderId) {
        if (orderId == null || orderList == null) {
            return; // Kiểm tra an toàn
        }
        // Sử dụng removeIf để xóa đơn hàng có ID trùng khớp
        boolean removed = orderList.removeIf(order -> orderId.equals(order.getOrderId()));
        if (removed) {
            saveOrders(); // Nếu xóa thành công, lưu lại danh sách mới
        }
    }
    // ===============================================================

    private void saveOrders() {
        String jsonOrders = gson.toJson(orderList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ORDERS, jsonOrders);
        editor.apply();
    }

    private void loadOrders() {
        String jsonOrders = sharedPreferences.getString(KEY_ORDERS, null);
        if (jsonOrders != null) {
            Type type = new TypeToken<ArrayList<Order>>() {}.getType();
            orderList = gson.fromJson(jsonOrders, type);
        } else {
            orderList = new ArrayList<>();
        }
    }
}