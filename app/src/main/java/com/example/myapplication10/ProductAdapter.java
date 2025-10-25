package com.example.myapplication10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getTitle());
        holder.tvProductDescription.setText(product.getCategory());
        String formattedPrice = String.format(Locale.US, "$%.2f", product.getPrice());
        holder.tvProductPrice.setText(formattedPrice);

        // ----- LOAD ẢNH TỪ drawable THEO TÊN -----
        String imgName = product.getImage(); // ví dụ "iphone15"
        ImageView iv = holder.ivProductImage;

        if (imgName != null && !imgName.trim().isEmpty()) {
            int resId = iv.getResources()
                    .getIdentifier(imgName, "drawable", iv.getContext().getPackageName());
            if (resId != 0) {
                iv.setImageResource(resId);
            } else {
                iv.setImageResource(R.drawable.placeholder); // fallback nếu sai tên
            }
        } else {
            iv.setImageResource(R.drawable.placeholder);     // fallback nếu trống
        }

        // Click item -> vào chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("PRODUCT_OBJECT", product);
            context.startActivity(intent);
        });

        // Nút "+"
        holder.btnAdd.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product);
            Toast.makeText(context, "Đã thêm " + product.getTitle() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, CartActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductDescription, tvProductPrice;
        MaterialButton btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Map ĐÚNG với id trong item_product.xml
            ivProductImage     = itemView.findViewById(R.id.iv_product_image);
            tvProductName      = itemView.findViewById(R.id.tv_product_name);
            tvProductDescription = itemView.findViewById(R.id.tv_product_description);
            tvProductPrice     = itemView.findViewById(R.id.tv_product_price);
            btnAdd             = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
