package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceHistoryActivity extends AppCompatActivity implements InvoiceAdapter.OnInvoiceClickListener {

    private RecyclerView recyclerView;
    private InvoiceAdapter adapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_history);

        recyclerView = findViewById(R.id.recyclerInvoices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders(); // tự refresh khi quay lại
    }

    private void loadOrders() {
        orderList = OrderManager.getInstance(this).getAllOrders();
        if (adapter == null) {
            adapter = new InvoiceAdapter(orderList, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.submitList(orderList); // hoặc adapter.setData(orderList) tuỳ bạn đặt tên
        }
    }

    @Override
    public void onInvoiceClick(Order order) {
        Intent intent = new Intent(this, OrderInvoiceActivity.class);
        intent.putExtra("ORDER_ID", order.getOrderId());
        intent.putExtra("TOTAL_AMOUNT", order.getTotalPrice()); // <-- dùng đúng getter mới (double)
        intent.putExtra("NAME", new SessionManager(this).getUsername());
        // Tạm dùng email làm “phone” nếu chưa có số điện thoại trong session:
        intent.putExtra("PHONE", new SessionManager(this).getEmail());
        intent.putExtra("ADDRESS", "Địa chỉ mặc định");
        startActivity(intent);
    }
}
