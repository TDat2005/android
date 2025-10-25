package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication10.databinding.ActivityCartBinding;

import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartManager.CartUpdateListener {

    private ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CartManager.getInstance().setListener(this);
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems);
        binding.rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCartItems.setAdapter(cartAdapter);

        updatePriceDetails();
        addSwipeToDelete();

        binding.btnPayNow.setOnClickListener(v -> {
            double totalAmount = CartManager.getInstance().calculateGrandTotal();
            Intent intent = new Intent(CartActivity.this, PurchaseActivity.class);
            intent.putExtra("TOTAL_AMOUNT", totalAmount);
            startActivity(intent);
        });
    }

    @Override
    public void onCartUpdated() {
        if (cartAdapter != null) {
            cartAdapter.notifyDataSetChanged();
        }
        updatePriceDetails();
    }

    private void updatePriceDetails() {
        double totalAmount = CartManager.getInstance().calculateGrandTotal();

        // =================== PHẦN ĐƯỢC SỬA ===================
        String totalAmountStr = String.format(Locale.US, "$%.2f", totalAmount);
        // =====================================================

        binding.tvGrandTotal.setText(totalAmountStr);
        binding.btnPayNow.setText("Thanh Tóan Ngay (" + totalAmountStr + ")");
    }

    private void addSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartItem itemToDelete = cartItems.get(position);
                CartManager.getInstance().removeItem(itemToDelete);
            }
        }).attachToRecyclerView(binding.rvCartItems);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CartManager.getInstance().setListener(null);
    }
}