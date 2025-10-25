package com.example.myapplication10;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private static FavoritesManager instance;
    private List<Product> favoriteProducts;

    private FavoritesManager() {
        favoriteProducts = new ArrayList<>();
    }

    public static synchronized FavoritesManager getInstance() {
        if (instance == null) {
            instance = new FavoritesManager();
        }
        return instance;
    }

    public void addProduct(Product product) {
        if (!isFavorite(product.getId())) {
            favoriteProducts.add(product);
        }
    }

    public void removeProduct(Product product) {
        favoriteProducts.removeIf(p -> p.getId() == product.getId());
    }

    public boolean isFavorite(int productId) {
        for (Product p : favoriteProducts) {
            if (p.getId() == productId) {
                return true;
            }
        }
        return false;
    }

    public List<Product> getFavoriteProducts() {
        return favoriteProducts;
    }
}