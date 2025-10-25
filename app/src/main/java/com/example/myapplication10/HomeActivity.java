package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication10.databinding.ActivityHomeBinding;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "ProductOffline";

    private ActivityHomeBinding binding;
    private final List<Product> allProductsList = new ArrayList<>();
    private final List<Product> displayedProductList = new ArrayList<>();
    private ProductAdapter productAdapter;

    // --- NEW: dùng SQLite thay API ---
    private ProductManager productManager;

    private String currentCategory = "All";
    private String currentSearchQuery = "";
    private String currentSortOrder = "DEFAULT";
    private List<MaterialButton> categoryButtons = new ArrayList<>();

    private SliderAdapter sliderAdapter;
    private final Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d(TAG, "HomeActivity onCreate (OFFLINE/SQLite).");

        // NEW
        productManager = new ProductManager(this);

        productAdapter = new ProductAdapter(this, displayedProductList);
        binding.rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvProducts.setAdapter(productAdapter);

        // Dữ liệu từ DB (không gọi API)
        seedIfEmpty();     // chèn mẫu nếu DB trống
        reloadFromDb();    // nạp tất cả sản phẩm vào allProductsList
        setupCategoryButtons();
        currentCategory = "All";
        updateCategoryButtonsUI(binding.btnCatAll);
        filterAndDisplayProducts();

        setupNavigation();
        setupSearchListener();
        setupFilterButton();
        setupImageSlider();

        // OPTIONAL: nếu bạn có FAB để thêm sản phẩm
        // (bỏ comment nếu layout có @id/fabAdd)
        /*
        binding.fabAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, ProductFormActivity.class);
            startActivityForResult(i, 1001);
        });
        */

        // OPTIONAL: nếu ProductAdapter đã có callbacks để sửa/xoá
        /*
        productAdapter.setOnItemClickListener(p -> {
            Intent i = new Intent(this, ProductFormActivity.class);
            i.putExtra(ProductFormActivity.EXTRA_PRODUCT_ID, p.getId());
            startActivityForResult(i, 1002);
        });

        productAdapter.setOnItemLongClickListener(p -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Xoá sản phẩm")
                    .setMessage("Bạn có chắc muốn xoá \"" + p.getName() + "\"?")
                    .setPositiveButton("Xoá", (d, w) -> {
                        productManager.delete(p.getId());
                        reloadFromDb();
                        filterAndDisplayProducts();
                    })
                    .setNegativeButton("Huỷ", null).show();
        });
        */
    }

    // ---- NEW: nạp dữ liệu từ SQLite thay vì gọi API ----
    private void reloadFromDb() {
        allProductsList.clear();
        allProductsList.addAll(productManager.getAll());
        Log.d(TAG, "Loaded from DB: " + allProductsList.size() + " products.");
        Collections.shuffle(allProductsList);
    }

    // ---- NEW: seed vài sản phẩm nếu DB trống (tuỳ chọn) ----
    private void seedIfEmpty() {
        if (productManager.getAll().isEmpty()) {
            productManager.insert("iPhone 15", 999.0, "iphone15", "Flagship 2024", "Dien Thoai");
            productManager.insert("Galaxy S24", 899.0, "galaxy_s24", "Android cao cấp", "Dien Thoai");
            productManager.insert("MacBook Air M2", 1199.0, "macbook_air_m2", "Mỏng nhẹ, pin trâu", "May Tinh");
            productManager.insert("ThinkPad X1", 1399.0, "thinkpad_x1", "Bền, gõ sướng", "May Tinh");
            productManager.insert("AirPods Pro 2", 249.0, "airpods_pro_2", "Chống ồn chủ động", "TW");
            productManager.insert("Logitech MX Master 3S", 119.0, "mx_master_3s", "Chuột productivity", "TW");
        }
    }



    // ==================== UI/Slider giữ nguyên ====================
    private void setupImageSlider() {
        List<Integer> sliderImages = new ArrayList<>();
        sliderImages.add(R.drawable.sale);
        sliderImages.add(R.drawable.promo_2);
        sliderImages.add(R.drawable.promo_3);
        sliderAdapter = new SliderAdapter(sliderImages, binding.viewPagerSlider);
        binding.viewPagerSlider.setAdapter(sliderAdapter);
        binding.indicator.setViewPager(binding.viewPagerSlider);
        sliderRunnable = () -> {
            int currentItem = binding.viewPagerSlider.getCurrentItem();
            int nextItem = currentItem + 1;
            if (nextItem >= sliderAdapter.getItemCount()) nextItem = 0;
            binding.viewPagerSlider.setCurrentItem(nextItem, true);
        };
        binding.viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }
    @Override protected void onPause() { super.onPause(); sliderHandler.removeCallbacks(sliderRunnable); }
    @Override protected void onResume() { super.onResume(); sliderHandler.postDelayed(sliderRunnable, 3000); }

    private void setupFilterButton() {
        binding.btnFilter.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, binding.btnFilter);
            popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_sort_low_to_high) {
                    currentSortOrder = "LOW_TO_HIGH";
                } else if (itemId == R.id.action_sort_high_to_low) {
                    currentSortOrder = "HIGH_TO_LOW";
                } else if (itemId == R.id.action_sort_default) {
                    currentSortOrder = "DEFAULT";
                }
                filterAndDisplayProducts();
                return true;
            });
            popupMenu.show();
        });
    }

    private void setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_favorites) {
                startActivity(new Intent(HomeActivity.this, FavoritesActivity.class));
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void setupCategoryButtons() {
        categoryButtons = new ArrayList<>();
        categoryButtons.add(binding.btnCatAll);
        categoryButtons.add(binding.btnCatCoffee); // dùng lại view cũ nhưng gán danh mục mới
        categoryButtons.add(binding.btnCatBeer);
        categoryButtons.add(binding.btnCatWine);

        // Map sang danh mục điện tử bạn đang dùng trong DB
        binding.btnCatAll.setOnClickListener(v -> handleCategoryClick(binding.btnCatAll, "All"));
        binding.btnCatCoffee.setOnClickListener(v -> handleCategoryClick(binding.btnCatCoffee, "Dien Thoai"));
        binding.btnCatBeer.setOnClickListener(v -> handleCategoryClick(binding.btnCatBeer, "May Tinh"));
        binding.btnCatWine.setOnClickListener(v -> handleCategoryClick(binding.btnCatWine, "TW")); // phụ kiện/thiết bị khác
    }

    private void handleCategoryClick(MaterialButton clickedButton, String category) {
        if (category.equals(currentCategory)) return;
        currentCategory = category;
        updateCategoryButtonsUI(clickedButton);
        filterAndDisplayProducts();
    }

    private void updateCategoryButtonsUI(MaterialButton selectedButton) {
        for (MaterialButton button : categoryButtons) {
            if (button == selectedButton) {
                button.setTextColor(ContextCompat.getColor(this, R.color.white));
            } else {
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
                button.setTextColor(ContextCompat.getColor(this, R.color.black));
            }
        }
    }

    private void setupSearchListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                currentSearchQuery = s.toString().toLowerCase().trim();
                filterAndDisplayProducts();
            }
        });
    }

    private void filterAndDisplayProducts() {
        displayedProductList.clear();

        // 1) Lọc theo danh mục
        List<Product> categoryFilteredList = new ArrayList<>();
        if (currentCategory.equals("All")) {
            categoryFilteredList.addAll(allProductsList);
        } else {
            for (Product product : allProductsList) {
                if (product.getCategory() != null &&
                        product.getCategory().equalsIgnoreCase(currentCategory)) {
                    categoryFilteredList.add(product);
                }
            }
        }

        // 2) Tìm kiếm theo tên
        if (currentSearchQuery.isEmpty()) {
            displayedProductList.addAll(categoryFilteredList);
        } else {
            for (Product product : categoryFilteredList) {
                if (product.getTitle() != null &&
                        product.getTitle().toLowerCase().contains(currentSearchQuery)) {
                    displayedProductList.add(product);
                }
            }
        }

        // 3) Sắp xếp
        if ("LOW_TO_HIGH".equals(currentSortOrder)) {
            Collections.sort(displayedProductList, Comparator.comparingDouble(Product::getPrice));
        } else if ("HIGH_TO_LOW".equals(currentSortOrder)) {
            Collections.sort(displayedProductList, Comparator.comparingDouble(Product::getPrice).reversed());
        }

        productAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Hiển thị " + displayedProductList.size() + " sản phẩm", Toast.LENGTH_SHORT).show();
    }

    // OPTIONAL: nếu dùng form thêm/sửa thì nhận kết quả và reload
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            reloadFromDb();
            filterAndDisplayProducts();
        }
    }
}
