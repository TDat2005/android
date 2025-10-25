package com.example.myapplication10;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication10.databinding.ActivityFavoritesBinding;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityFavoritesBinding binding;
    private FavoritesAdapter adapter;
    private List<Product> favoriteProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy danh sách sản phẩm từ FavoritesManager
        favoriteProducts = FavoritesManager.getInstance().getFavoriteProducts();

        // Thiết lập RecyclerView
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter(this, favoriteProducts);
        binding.rvFavorites.setAdapter(adapter);

        // Gọi hàm kiểm tra trạng thái hiển thị
        checkEmptyState();
    }

    // Hàm này được gọi mỗi khi người dùng quay lại màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách phòng trường hợp có thay đổi
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        // Kiểm tra lại trạng thái hiển thị
        checkEmptyState();
    }

    // =================== HÀM MỚI ĐỂ KIỂM TRA VÀ CẬP NHẬT GIAO DIỆN ===================
    private void checkEmptyState() {
        if (favoriteProducts.isEmpty()) {
            // Nếu danh sách rỗng, ẩn RecyclerView và hiện thông báo
            binding.rvFavorites.setVisibility(View.GONE);
            binding.tvNoFavorites.setVisibility(View.VISIBLE);
        } else {
            // Nếu danh sách có sản phẩm, hiện RecyclerView và ẩn thông báo
            binding.rvFavorites.setVisibility(View.VISIBLE);
            binding.tvNoFavorites.setVisibility(View.GONE);
        }
    }
    // =================================================================================
}