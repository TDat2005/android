package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivityProductDetailBinding;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT = "PRODUCT_OBJECT";

    private ActivityProductDetailBinding binding;
    private Product product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Object obj = getIntent() != null ? getIntent().getSerializableExtra(EXTRA_PRODUCT) : null;
        if (obj instanceof Product) {
            product = (Product) obj;
        }
        if (product == null) {
            finish();
            return;
        }

        // Texts
        binding.tvTitleDetail.setText(product.getTitle());
        binding.tvCategoryDetail.setText(product.getCategory());
        binding.tvPriceDetail.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
        binding.tvDescDetail.setText(product.getDescription());

        // Image from drawable name (e.g., "iphone15")
        loadImageFromDrawableName(product.getImage());

        binding.btnAddToCartDetail.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product);
            startActivity(new Intent(this, CartActivity.class));
        });
    }

    private void loadImageFromDrawableName(String name) {
        if (name == null || name.trim().isEmpty()) {
            binding.ivProductImageDetail.setImageResource(R.drawable.placeholder);
            return;
        }
        if (name.startsWith("http") || name.startsWith("content://") || name.startsWith("file://")) {
            // offline mode: ignore external sources
            binding.ivProductImageDetail.setImageResource(R.drawable.placeholder);
            return;
        }
        int resId = getResources().getIdentifier(name, "drawable", getPackageName());
        binding.ivProductImageDetail.setImageResource(resId != 0 ? resId : R.drawable.placeholder);
    }
}
