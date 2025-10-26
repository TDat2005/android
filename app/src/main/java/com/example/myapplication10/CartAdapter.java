package com.example.myapplication10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Product product = cartItem.getProduct();

        holder.tvName.setText(product.getTitle());

        // Giá theo VND
        holder.tvPrice.setText(MoneyUtils.vnd(product.getPrice()));

        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(context)
                .load(product.getImage())
                .into(holder.ivImage);

        holder.btnPlus.setOnClickListener(v -> {
            CartManager.getInstance().increaseQuantity(cartItem);
            notifyItemChanged(holder.getAdapterPosition()); // cập nhật lại item
        });

        holder.btnMinus.setOnClickListener(v -> {
            CartManager.getInstance().decreaseQuantity(cartItem);
            notifyItemChanged(holder.getAdapterPosition()); // cập nhật lại item
        });
    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivImage;
        TextView tvName, tvPrice, tvQuantity;
        MaterialButton btnPlus, btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_cart_item_image);
            tvName = itemView.findViewById(R.id.tv_cart_item_name);
            tvPrice = itemView.findViewById(R.id.tv_cart_item_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }
}