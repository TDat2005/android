package com.example.myapplication10;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private static final String PREF_NAME = "OrderHistory";
    private static final String KEY_ORDERS = "orders";

    private static OrderManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private List<Order> orderList;

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

    // ================== LẤY TOÀN BỘ ĐƠN HÀNG ==================
    public List<Order> getAllOrders() {
        if (orderList == null) {
            loadOrders();
        }
        return orderList;
    }

    // ================== THÊM MỚI MỘT ĐƠN HÀNG ==================
    public void addOrder(Order order) {
        if (orderList == null) {
            orderList = new ArrayList<>();
        }
        orderList.add(0, order); // Thêm đơn mới lên đầu danh sách
        saveOrders();
    }

    // ================== XÓA ĐƠN HÀNG THEO ID ==================
    public void removeOrder(String orderId) {
        if (orderId == null || orderList == null) return;
        boolean removed = orderList.removeIf(order -> orderId.equals(order.getOrderId()));
        if (removed) {
            saveOrders();
        }
    }

    // ================== TÌM ĐƠN HÀNG THEO ID ==================
    public Order getOrderById(String orderId) {
        if (orderId == null || orderList == null) return null;
        for (Order order : orderList) {
            if (orderId.equals(order.getOrderId())) return order;
        }
        return null;
    }

    // ================== LƯU DỮ LIỆU ==================
    private void saveOrders() {
        String json = gson.toJson(orderList);
        sharedPreferences.edit().putString(KEY_ORDERS, json).apply();
    }

    // ================== TẢI DỮ LIỆU ==================
    private void loadOrders() {
        String json = sharedPreferences.getString(KEY_ORDERS, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<Order>>() {}.getType();
            orderList = gson.fromJson(json, type);
        } else {
            orderList = new ArrayList<>();
        }
    }
}
