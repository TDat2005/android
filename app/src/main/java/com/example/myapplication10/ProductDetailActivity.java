package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication10.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT = "PRODUCT_OBJECT";

    private ActivityProductDetailBinding binding;
    private Product product;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Object obj = getIntent() != null ? getIntent().getSerializableExtra(EXTRA_PRODUCT) : null;
        if (obj instanceof Product) product = (Product) obj;
        if (product == null) { finish(); return; }

        // Fill UI
        binding.tvTitleDetail.setText(product.getTitle());
        binding.tvCategoryDetail.setText(product.getCategory());
        binding.tvPriceDetail.setText(MoneyUtils.vnd(product.getPrice()));
        binding.tvDescDetail.setText(product.getDescription());
        loadImageFromDrawableName(product.getImage());

        // Favorite state
        isFavorite = FavoritesManager.getInstance(this).isFavorite(product.getId());
        refreshFavIcon();

        // Toggle favorite
        binding.btnFavoriteDetail.setOnClickListener(v -> {
            if (isFavorite) {
                FavoritesManager.getInstance(this).remove(product.getId());
                isFavorite = false;
                Toast.makeText(this, "Đã bỏ khỏi Yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                FavoritesManager.getInstance(this).add(product);
                isFavorite = true;
                Toast.makeText(this, "Đã thêm vào Yêu thích", Toast.LENGTH_SHORT).show();
            }
            refreshFavIcon();
        });

        // Add to cart
        binding.btnAddToCartDetail.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product);
            Toast.makeText(this, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CartActivity.class));
        });
    }

    private void refreshFavIcon() {
        binding.btnFavoriteDetail.setIconResource(
                isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border
        );
    }

    private void loadImageFromDrawableName(String name) {
        if (name == null || name.trim().isEmpty()) {
            binding.ivProductImageDetail.setImageResource(R.drawable.placeholder);
            return;
        }
        if (name.startsWith("http") || name.startsWith("content://") || name.startsWith("file://")) {
            binding.ivProductImageDetail.setImageResource(R.drawable.placeholder);
            return;
        }
        int resId = getResources().getIdentifier(name, "drawable", getPackageName());
        binding.ivProductImageDetail.setImageResource(resId != 0 ? resId : R.drawable.placeholder);
    }
}
