package com.example.myapplication10;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritesManager {
    private static FavoritesManager instance;

    private static final String PREF = "favorites_prefs";
    private static final String KEY_IDS = "fav_ids";
    private static final String KEY_ITEMS = "fav_items";

    private final SharedPreferences sp;
    private final Gson gson = new Gson();

    private FavoritesManager(Context ctx) {
        sp = ctx.getApplicationContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static synchronized FavoritesManager getInstance(Context ctx) {
        if (instance == null) instance = new FavoritesManager(ctx);
        return instance;
    }

    // ====== APIs đơn giản ======
    public boolean isFavorite(long productId) {
        return getIdSet().contains(String.valueOf(productId));
    }

    public void add(Product p) {
        Set<String> ids = getIdSet();
        ids.add(String.valueOf(p.getId()));
        sp.edit().putStringSet(KEY_IDS, ids).apply();

        List<Product> items = getItems();
        // tránh trùng
        boolean exists = false;
        for (Product it : items) if (it.getId() == p.getId()) { exists = true; break; }
        if (!exists) items.add(p);
        sp.edit().putString(KEY_ITEMS, gson.toJson(items)).apply();
    }

    public void remove(long productId) {
        Set<String> ids = getIdSet();
        ids.remove(String.valueOf(productId));
        sp.edit().putStringSet(KEY_IDS, ids).apply();

        List<Product> items = getItems();
        items.removeIf(it -> it.getId() == productId);
        sp.edit().putString(KEY_ITEMS, gson.toJson(items)).apply();
    }

    public List<Product> getFavorites() {
        return getItems();
    }

    // ====== Helpers ======
    private Set<String> getIdSet() {
        Set<String> def = new HashSet<>();
        return new HashSet<>(sp.getStringSet(KEY_IDS, def));
    }

    private List<Product> getItems() {
        String json = sp.getString(KEY_ITEMS, null);
        if (json == null) return new ArrayList<>();
        Type t = new TypeToken<ArrayList<Product>>(){}.getType();
        List<Product> list = gson.fromJson(json, t);
        return list != null ? list : new ArrayList<>();
    }
}
