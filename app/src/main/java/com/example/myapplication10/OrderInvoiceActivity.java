package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderInvoiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_invoice);

        String orderId = getIntent().getStringExtra("ORDER_ID");
        double total = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0);
        String name = getIntent().getStringExtra("NAME");
        String phone = getIntent().getStringExtra("PHONE");
        String address = getIntent().getStringExtra("ADDRESS");

        ((TextView) findViewById(R.id.tvOrderId)).setText(orderId);
        ((TextView) findViewById(R.id.tvTotal)).setText(MoneyUtils.vnd(total));
        ((TextView) findViewById(R.id.tvName)).setText(name);
        ((TextView) findViewById(R.id.tvPhone)).setText(phone);
        ((TextView) findViewById(R.id.tvAddress)).setText(address);
        ((TextView) findViewById(R.id.tvDate)).setText(new SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault()).format(new Date()));

        MaterialButton btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

    }
}
