package com.example.myapplication10;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "shop.db";
    public static final int DB_VERSION = 10;

    public static final String TBL_PRODUCTS = "products";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "image";
    public static final String COL_DESC = "description";
    public static final String COL_CATEGORY = "category";
    public static final String TBL_ADDRESS = "addresses";
    private static final String CREATE_TBL_ADDRESS =
            "CREATE TABLE IF NOT EXISTS " + TBL_ADDRESS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "phone TEXT NOT NULL," +
                    "line1 TEXT NOT NULL," +
                    "is_default INTEGER NOT NULL DEFAULT 0)";
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
        db.execSQL(CREATE_TBL_ADDRESS);
        db.execSQL("CREATE TABLE IF NOT EXISTS addresses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "phone TEXT, " +
                "line1 TEXT, " +
                "line2 TEXT, " +
                "ward TEXT, " +
                "district TEXT, " +
                "city TEXT, " +
                "zip TEXT, " +
                "is_default INTEGER DEFAULT 0, " +
                "created_at INTEGER, " +
                "updated_at INTEGER)");

    }


    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PRODUCTS);
        onCreate(db);
    }
}

