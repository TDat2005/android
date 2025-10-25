package com.example.myapplication10;// Trong file Order.java
import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private long orderDate;
    private double totalPrice;
    private List<CartItem> items;

    // Constructor mới
    public Order(String orderId, long orderDate, double totalPrice, List<CartItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    // Getters
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
}