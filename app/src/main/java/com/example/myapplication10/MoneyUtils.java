package com.example.myapplication10;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtils {
    private static final Locale VN = new Locale("vi", "VN");
    private static final NumberFormat CURRENCY;

    static {
        CURRENCY = NumberFormat.getCurrencyInstance(VN);
        // Không lấy phần thập phân, dùng ký hiệu ₫
        if (CURRENCY instanceof DecimalFormat) {
            DecimalFormat df = (DecimalFormat) CURRENCY;
            df.setMaximumFractionDigits(0);
            df.setMinimumFractionDigits(0);
            DecimalFormatSymbols s = df.getDecimalFormatSymbols();
            s.setCurrencySymbol("₫"); // hoặc "VNĐ" nếu m thích
            df.setDecimalFormatSymbols(s);
        }
    }

    private MoneyUtils() {}

    public static String vnd(double amount) {
        return CURRENCY.format(amount);
    }
}
