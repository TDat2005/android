package com.example.myapplication10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private final ProductDbHelper dbHelper;
    public ProductManager(Context ctx) { dbHelper = new ProductDbHelper(ctx); }

    // INSERT
    public long insert(String title, double price, String image, String desc, String category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ProductDbHelper.COL_TITLE, title);
        cv.put(ProductDbHelper.COL_PRICE, price);
        cv.put(ProductDbHelper.COL_IMAGE, image);
        cv.put(ProductDbHelper.COL_DESC, desc);
        cv.put(ProductDbHelper.COL_CATEGORY, category);
        return db.insert(ProductDbHelper.TBL_PRODUCTS, null, cv);
    }

    // UPDATE theo id
    public int update(long id, String title, double price, String image, String desc, String category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ProductDbHelper.COL_TITLE, title);
        cv.put(ProductDbHelper.COL_PRICE, price);
        cv.put(ProductDbHelper.COL_IMAGE, image);
        cv.put(ProductDbHelper.COL_DESC, desc);
        cv.put(ProductDbHelper.COL_CATEGORY, category);
        return db.update(ProductDbHelper.TBL_PRODUCTS, cv,
                ProductDbHelper.COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(ProductDbHelper.TBL_PRODUCTS,
                ProductDbHelper.COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Product getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor c = db.query(ProductDbHelper.TBL_PRODUCTS, null,
                ProductDbHelper.COL_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null)) {
            if (c.moveToFirst()) return cursorToProduct(c);
        }
        return null;
    }

    public List<Product> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> out = new ArrayList<>();
        try (Cursor c = db.query(ProductDbHelper.TBL_PRODUCTS, null,
                null, null, null, null, ProductDbHelper.COL_ID + " DESC")) {
            while (c.moveToNext()) out.add(cursorToProduct(c));
        }
        return out;
    }

    private Product cursorToProduct(Cursor c) {
        int id = c.getInt(c.getColumnIndexOrThrow(ProductDbHelper.COL_ID));
        String title = c.getString(c.getColumnIndexOrThrow(ProductDbHelper.COL_TITLE));
        double price = c.getDouble(c.getColumnIndexOrThrow(ProductDbHelper.COL_PRICE));
        String image = c.getString(c.getColumnIndexOrThrow(ProductDbHelper.COL_IMAGE));
        String desc = c.getString(c.getColumnIndexOrThrow(ProductDbHelper.COL_DESC));
        String category = c.getString(c.getColumnIndexOrThrow(ProductDbHelper.COL_CATEGORY));
        double rating = 4.5; // DB chưa lưu rating → gán mặc định
        return new Product(id, title, price, desc, category, image, rating);
    }
}
