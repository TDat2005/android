package com.example.myapplication10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.VH> {

    public interface OnInvoiceClickListener {
        void onInvoiceClick(Order order);
    }

    private List<Order> data;
    private final OnInvoiceClickListener listener;

    public InvoiceAdapter(List<Order> data, OnInvoiceClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Order o = data.get(pos);
        h.tvId.setText("Mã hóa đơn: " + o.getOrderId());
        h.tvTotal.setText(MoneyUtils.vnd(o.getTotalAmount()));
        h.tvDate.setText("Ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(o.getDate()));
        h.itemView.setOnClickListener(v -> listener.onInvoiceClick(o));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvId, tvDate, tvTotal;
        VH(View v) {
            super(v);
            tvId = v.findViewById(R.id.tvInvoiceId);
            tvDate = v.findViewById(R.id.tvInvoiceDate);
            tvTotal = v.findViewById(R.id.tvInvoiceTotal);
        }
    }
    public void submitList(List<Order> newList) {
        this.data = newList;
        notifyDataSetChanged();
    }

}
