package com.example.myapplication10;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "shop.db";
    public static final int DB_VERSION = 1;

    public static final String TBL_PRODUCTS = "products";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "image";
    public static final String COL_DESC = "description";
    public static final String COL_CATEGORY = "category";

    public ProductDbHelper(Context ctx) { super(ctx, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TBL_PRODUCTS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_TITLE + " TEXT NOT NULL, " +
                        COL_PRICE + " REAL NOT NULL DEFAULT 0, " +
                        COL_IMAGE + " TEXT, " +
                        COL_DESC + " TEXT, " +
                        COL_CATEGORY + " TEXT)"
        );
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PRODUCTS);
        onCreate(db);
    }
}

