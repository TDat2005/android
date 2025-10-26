package com.example.myapplication10;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;        // Mã đơn hàng
    private long orderDate;        // Thời gian đặt hàng (timestamp)
    private double totalPrice;     // Tổng tiền
    private List<CartItem> items;  // Danh sách sản phẩm trong đơn

    // Constructor
    public Order(String orderId, long orderDate, double totalPrice, List<CartItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    // ====== Getters ======
    public String getOrderId() {
        return orderId;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<CartItem> getItems() {
        return items;
    }

    // ====== Các alias để tương thích với adapter ======
    // Dành cho code cũ (getTotalAmount, getDate)
    public double getTotalAmount() {
        return totalPrice;
    }

    public long getDate() {
        return orderDate;
    }
}
