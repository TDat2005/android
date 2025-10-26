package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication10.databinding.ActivityCartBinding;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartManager.CartUpdateListener {

    private ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    // địa chỉ đã chọn từ AddressListActivity
    private long selectedAddressId = -1L;
    private String selectedAddressText = null;

    // launcher mở màn danh sách địa chỉ
    private final ActivityResultLauncher<Intent> pickAddressLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedAddressId = result.getData().getLongExtra(AddressListActivity.EXTRA_SELECTED_ID, -1L);
                    selectedAddressText = result.getData().getStringExtra(AddressListActivity.EXTRA_SELECTED_TEXT);
                    binding.tvAddress.setText(
                            selectedAddressText != null ? selectedAddressText : getString(R.string.label_no_address)
                    );
                }
            });

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

        // nếu chưa có địa chỉ, hiện nhãn mặc định
        binding.tvAddress.setText(getString(R.string.label_no_address));

        updatePriceDetails();
        addSwipeToDelete();

        // Mở màn chọn địa chỉ
        binding.layoutAddress.setOnClickListener(v -> {
            Intent i = new Intent(this, AddressListActivity.class);
            pickAddressLauncher.launch(i);
        });

        // Pay now -> sang PurchaseActivity kèm địa chỉ đã chọn
        binding.btnPayNow.setOnClickListener(v -> {
            double totalAmount = CartManager.getInstance().calculateGrandTotal();
            if (totalAmount <= 0) {
                Toast.makeText(this, "Giỏ hàng đang trống.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(CartActivity.this, PurchaseActivity.class);
            intent.putExtra("TOTAL_AMOUNT", totalAmount);
            intent.putExtra("ADDRESS_ID", selectedAddressId);
            intent.putExtra("ADDRESS_TEXT", selectedAddressText);
            startActivity(intent);
        });
    }

    @Override
    public void onCartUpdated() {
        if (cartAdapter != null) cartAdapter.notifyDataSetChanged();
        updatePriceDetails();
    }

    private void updatePriceDetails() {
        double totalAmount = CartManager.getInstance().calculateGrandTotal();
        String totalAmountStr = MoneyUtils.vnd(totalAmount); // ➜ VND
        binding.tvGrandTotal.setText(totalAmountStr);
        binding.btnPayNow.setText("Đặt đơn hàng (" + totalAmountStr + ")");
    }

    private void addSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder vh, @NonNull RecyclerView.ViewHolder t) { return false; }
            @Override public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int dir) {
                int position = vh.getAdapterPosition();
                CartItem itemToDelete = cartItems.get(position);
                CartManager.getInstance().removeItem(itemToDelete);
                // cập nhật UI ngay
                cartAdapter.notifyItemRemoved(position);
                updatePriceDetails();
            }
        }).attachToRecyclerView(binding.rvCartItems);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CartManager.getInstance().setListener(null);
    }
}
