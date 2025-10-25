package com.example.myapplication10;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    // Interface để thông báo cho Activity khi giỏ hàng thay đổi
    public interface CartUpdateListener {
        void onCartUpdated();
    }

    private static CartManager instance;
    private List<CartItem> cartItems;
    private CartUpdateListener listener; // Biến để lưu listener

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Đăng ký listener (thường là CartActivity)
    public void setListener(CartUpdateListener listener) {
        this.listener = listener;
    }

    public void addProduct(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                notifyCartUpdate(); // Thông báo thay đổi
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
        notifyCartUpdate(); // Thông báo thay đổi
    }

    // =================== CÁC HÀM MỚI ===================
    public void increaseQuantity(CartItem item) {
        item.setQuantity(item.getQuantity() + 1);
        notifyCartUpdate();
    }

    public void decreaseQuantity(CartItem item) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            // Nếu số lượng là 1, giảm nữa sẽ là xóa
            removeItem(item);
        }
        notifyCartUpdate();
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
        notifyCartUpdate();
    }

    public double calculateGrandTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }
    // =====================================================

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // Hàm private để gọi listener nếu nó tồn tại
    private void notifyCartUpdate() {
        if (listener != null) {
            listener.onCartUpdated();
        }
    }
    public void clearCart() {
        cartItems.clear();
        notifyCartUpdate(); // Thông báo cho các màn hình khác (nếu cần)
    }
}