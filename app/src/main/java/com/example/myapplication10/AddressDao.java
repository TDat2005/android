package com.example.myapplication10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddressDao {

    private final ProductDbHelper dbHelper;

    public AddressDao(Context context) {
        dbHelper = new ProductDbHelper(context);
    }

    // ====================== INSERT ======================
    /** Thêm địa chỉ. Nếu makeDefault = true -> clear mặc định cũ và set mặc định cho dòng mới. */
    public long insert(Address a, boolean makeDefault) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (makeDefault) {
            ContentValues cvReset = new ContentValues();
            cvReset.put("is_default", 0);
            db.update("addresses", cvReset, null, null);
        }

        long now = System.currentTimeMillis();

        ContentValues cv = new ContentValues();
        cv.put("name", a.getName());
        cv.put("phone", a.getPhone());
        cv.put("line1", a.getLine1());
        cv.put("line2", a.getLine2());
        cv.put("ward", a.getWard());
        cv.put("district", a.getDistrict());
        cv.put("city", a.getCity());
        cv.put("zip", a.getZip());
        cv.put("is_default", (makeDefault || a.isDefault()) ? 1 : 0);
        cv.put("created_at", now);
        cv.put("updated_at", now);

        long id = db.insert("addresses", null, cv);
        db.close();
        return id;
    }

    // ====================== UPDATE ======================
    /** Cập nhật địa chỉ. Nếu makeDefault = true -> set mặc định cho id hiện tại. */
    public int update(Address a, boolean makeDefault) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (makeDefault) {
            ContentValues reset = new ContentValues();
            reset.put("is_default", 0);
            db.update("addresses", reset, null, null);
        }

        ContentValues cv = new ContentValues();
        cv.put("name", a.getName());
        cv.put("phone", a.getPhone());
        cv.put("line1", a.getLine1());
        cv.put("line2", a.getLine2());
        cv.put("ward", a.getWard());
        cv.put("district", a.getDistrict());
        cv.put("city", a.getCity());
        cv.put("zip", a.getZip());
        cv.put("is_default", (makeDefault || a.isDefault()) ? 1 : 0);
        cv.put("updated_at", System.currentTimeMillis());

        int rows = db.update("addresses", cv, "id=?", new String[]{String.valueOf(a.getId())});
        db.close();
        return rows;
    }

    // ====================== DELETE ======================
    /** Xoá địa chỉ theo id. Trả về số dòng bị xoá (0/1). */
    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("addresses", "id=?", new String[]{ String.valueOf(id) });
        db.close();
        return rows;
    }

    // ====================== GET DEFAULT ======================
    public Address getDefault() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM addresses WHERE is_default = 1 LIMIT 1", null);

        Address a = null;
        if (c.moveToFirst()) a = readAddress(c);

        c.close();
        db.close();
        return a;
    }

    // ====================== GET ALL ======================
    public List<Address> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Address> list = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM addresses ORDER BY updated_at DESC", null);
        while (c.moveToNext()) list.add(readAddress(c));

        c.close();
        db.close();
        return list;
    }

    // ====================== SET DEFAULT ======================
    /** Đặt mặc định cho 1 id (clear hết rồi set id này). */
    public void setDefault(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("UPDATE addresses SET is_default = 0");
            db.execSQL("UPDATE addresses SET is_default = 1, updated_at = ? WHERE id = ?",
                    new Object[]{System.currentTimeMillis(), id});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // ====================== READ HELPER ======================
    private Address readAddress(Cursor c) {
        Address a = new Address();
        a.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        a.setName(c.getString(c.getColumnIndexOrThrow("name")));
        a.setPhone(c.getString(c.getColumnIndexOrThrow("phone")));
        a.setLine1(c.getString(c.getColumnIndexOrThrow("line1")));
        a.setLine2(c.getString(c.getColumnIndexOrThrow("line2")));
        a.setWard(c.getString(c.getColumnIndexOrThrow("ward")));
        a.setDistrict(c.getString(c.getColumnIndexOrThrow("district")));
        a.setCity(c.getString(c.getColumnIndexOrThrow("city")));
        a.setZip(c.getString(c.getColumnIndexOrThrow("zip")));
        a.setDefault(c.getInt(c.getColumnIndexOrThrow("is_default")) == 1);
        a.setCreatedAt(c.getLong(c.getColumnIndexOrThrow("created_at")));
        a.setUpdatedAt(c.getLong(c.getColumnIndexOrThrow("updated_at")));
        return a;
    }
}
