package com.example.myapplication10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private Context context;
    private List<Product> favoriteProducts;

    public FavoritesAdapter(Context context, List<Product> favoriteProducts) {
        this.context = context;
        this.favoriteProducts = favoriteProducts;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Product product = favoriteProducts.get(position);

        holder.tvName.setText(product.getTitle());
        // Chuẩn hóa định dạng tiền tệ sang Đô la Mỹ ($) cho đồng bộ
        holder.tvPrice.setText(String.format(Locale.US, "$%.2f", product.getPrice()));

        Glide.with(context).load(product.getImage()).into(holder.ivImage);

        // =================== PHẦN ĐƯỢC SỬA ===================
        // Sự kiện click vào nút "Add to Cart"
        holder.btnAdd.setOnClickListener(v -> {
            // 1. Thêm sản phẩm vào giỏ hàng
            CartManager.getInstance().addProduct(product);
            Toast.makeText(context, "Đã thêm " + product.getTitle() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();

            // 2. Chuyển ngay đến màn hình Giỏ hàng
            Intent intent = new Intent(context, CartActivity.class);
            context.startActivity(intent);
        });
        // ======================================================

        // =================== SỬA LỖI TIỀM ẨN ===================
        // Sự kiện click vào cả item để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            // Gửi cả đối tượng Product, không gửi ID nữa để tránh lỗi
            intent.putExtra("PRODUCT_OBJECT", product);
            context.startActivity(intent);
        });
        // ======================================================
    }

    @Override
    public int getItemCount() {
        return favoriteProducts.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivImage;
        TextView tvName, tvPrice;
        MaterialButton btnAdd;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_favorite_image);
            tvName = itemView.findViewById(R.id.tv_favorite_name);
            tvPrice = itemView.findViewById(R.id.tv_favorite_price);
            btnAdd = itemView.findViewById(R.id.btn_add_favorite_to_cart);
        }
    }
}