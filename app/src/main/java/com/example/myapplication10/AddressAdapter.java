package com.example.myapplication10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.VH> {

    public interface OnSelectListener { void onSelect(Address a); }
    public interface OnLongDeleteListener { void onDelete(Address a, int position); }

    private final List<Address> data;
    private long selectedId = -1L;
    private final OnSelectListener listener;
    private OnLongDeleteListener longDeleteListener;

    public AddressAdapter(@NonNull List<Address> data, long selectedId, @NonNull OnSelectListener l) {
        this.data = data;
        this.selectedId = selectedId;
        this.listener = l;
    }

    public void setSelectedId(long id) {
        this.selectedId = id;
        notifyDataSetChanged();
    }
    public void setOnLongDeleteListener(OnLongDeleteListener l) { this.longDeleteListener = l; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Address a = data.get(position);

        h.tvName.setText("Họ và Tên:  " + safe(a.getName()));
        h.tvPhone.setText("Điện thoại: " + safe(a.getPhone()));
        h.tvAddr.setText("Địa chỉ:     " + buildDisplay(a));

        h.radio.setOnCheckedChangeListener(null);
        h.radio.setChecked(a.getId() == selectedId);

        View.OnClickListener select = v -> {
            selectedId = a.getId();
            notifyDataSetChanged();
            listener.onSelect(a);
        };
        h.itemView.setOnClickListener(select);
        h.radio.setOnClickListener(select);

        h.itemView.setOnLongClickListener(v -> {
            if (longDeleteListener != null) {
                longDeleteListener.onDelete(a, h.getAdapterPosition());
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() { return data == null ? 0 : data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvAddr;
        RadioButton radio;
        VH(@NonNull View v) {
            super(v);
            tvName  = v.findViewById(R.id.tvName);
            tvPhone = v.findViewById(R.id.tvPhone);
            tvAddr  = v.findViewById(R.id.tvAddr);
            radio   = v.findViewById(R.id.radio);
        }
    }

    private static String buildDisplay(Address a) {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty(a.getLine1()))    sb.append(a.getLine1());
        if (!isEmpty(a.getWard()))     sb.append(", ").append(a.getWard());
        if (!isEmpty(a.getDistrict())) sb.append(", ").append(a.getDistrict());
        if (!isEmpty(a.getCity()))     sb.append(", ").append(a.getCity());
        return sb.toString();
    }

    private static boolean isEmpty(String s){ return s == null || s.trim().isEmpty(); }
    private static String safe(String s){ return s == null ? "" : s; }
}
