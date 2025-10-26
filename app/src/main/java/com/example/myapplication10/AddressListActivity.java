package com.example.myapplication10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressListActivity extends AppCompatActivity {
    public static final String EXTRA_SELECTED_ID   = "selected_address_id";
    public static final String EXTRA_SELECTED_TEXT = "selected_address_text";

    private AddressDao dao;
    private AddressAdapter adapter;
    private final List<Address> data = new ArrayList<>();
    private long selectedId = -1L;

    private RecyclerView rcv;

    // ====== DATA DEMO MIỀN BẮC (Province -> District -> Ward) ======
    private final String[] cities = new String[]{
            "Hà Nội", "Hải Phòng", "Quảng Ninh", "Bắc Ninh",
            "Hải Dương", "Thái Nguyên", "Nam Định", "Ninh Bình"
    };
    private final Map<String, String[]> cityToDistricts = new HashMap<>();
    private final Map<String, String[]> districtToWards = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        seedNorthData();

        dao = new AddressDao(this);

        Address def = dao.getDefault();
        if (def != null) selectedId = def.getId();

        rcv = findViewById(R.id.rcv);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        data.clear();
        data.addAll(dao.getAll());

        adapter = new AddressAdapter(data, selectedId, a -> {
            // chọn mặc định
            dao.setDefault(a.getId());
            selectedId = a.getId();
            adapter.setSelectedId(selectedId);

            Intent out = new Intent();
            out.putExtra(EXTRA_SELECTED_ID, a.getId());
            out.putExtra(EXTRA_SELECTED_TEXT, a.getName() + " • " + a.getPhone() + "\n" + buildDisplay(a));
            setResult(RESULT_OK, out);
            finish(); // nếu muốn đóng ngay sau khi chọn
        });

        // Giữ lâu để xoá
        adapter.setOnLongDeleteListener((a, pos) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa địa chỉ")
                    .setMessage("Xóa địa chỉ của " + a.getName() + "?")
                    .setPositiveButton("Xóa", (d, w) -> {
                        dao.delete(a.getId());
                        data.remove(pos);
                        adapter.notifyItemRemoved(pos);
                        if (selectedId == a.getId()) selectedId = -1L;
                        Toast.makeText(this, "Đã xóa.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        rcv.setAdapter(adapter);

        findViewById(R.id.btnAdd).setOnClickListener(v -> openAddSheet());
    }

    private void openAddSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.sheet_add_address, findViewById(android.R.id.content), false);

        // Inputs
        TextInputEditText edtName  = view.findViewById(R.id.edtName);
        TextInputEditText edtPhone = view.findViewById(R.id.edtPhone);
        TextInputEditText edtLine1 = view.findViewById(R.id.edtLine1);

        // Dropdowns
        AutoCompleteTextView actCity     = view.findViewById(R.id.edtCity);
        AutoCompleteTextView actDistrict = view.findViewById(R.id.edtDistrict);
        AutoCompleteTextView actWard     = view.findViewById(R.id.edtWard);

        // ====== Bind adapters ======
        ArrayAdapter<String> cityAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        actCity.setAdapter(cityAdapter);

        actCity.setOnItemClickListener((parent, v1, position, id) -> {
            String city = (String) parent.getItemAtPosition(position);
            String[] districts = cityToDistricts.get(city);
            if (districts == null) districts = new String[]{};

            actDistrict.setText("");
            actWard.setText("");
            actDistrict.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, districts));
        });

        actDistrict.setOnItemClickListener((parent, v12, position, id) -> {
            String district = (String) parent.getItemAtPosition(position);
            String[] wards = districtToWards.get(district);
            if (wards == null) wards = new String[]{};

            actWard.setText("");
            actWard.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, wards));
        });

        // Buttons
        view.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            String name  = txt(edtName);
            String phone = txt(edtPhone);
            String line1 = txt(edtLine1);

            if (name.isEmpty() || phone.isEmpty() || line1.isEmpty()) {
                Toast.makeText(this, "Nhập đủ Họ tên / Điện thoại / Số nhà-đường", Toast.LENGTH_SHORT).show();
                return;
            }

            Address a = new Address();
            a.setName(name);
            a.setPhone(phone);
            a.setLine1(line1);
            a.setCity(actCity.getText().toString().trim());
            a.setDistrict(actDistrict.getText().toString().trim());
            a.setWard(actWard.getText().toString().trim());
            a.setDefault(true);

            long id = dao.insert(a, true); // reset default và set cho dòng này
            a.setId(id);
            dao.setDefault(id);

            // Cập nhật UI: thêm lên đầu list + chọn radio
            data.add(0, a);
            adapter.notifyItemInserted(0);
            rcv.scrollToPosition(0);
            selectedId = id;
            adapter.setSelectedId(selectedId);

            // Trả về selection (nếu màn hình này dùng để chọn địa chỉ)
            Intent out = new Intent();
            out.putExtra(EXTRA_SELECTED_ID, id);
            out.putExtra(EXTRA_SELECTED_TEXT, name + " • " + phone + "\n" + buildDisplay(a));
            setResult(RESULT_OK, out);

            dialog.dismiss();
            // finish(); // bật nếu muốn đóng sau khi thêm
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private String buildDisplay(Address a) {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty(a.getLine1()))    sb.append(a.getLine1());
        if (!isEmpty(a.getWard()))     sb.append(", ").append(a.getWard());
        if (!isEmpty(a.getDistrict())) sb.append(", ").append(a.getDistrict());
        if (!isEmpty(a.getCity()))     sb.append(", ").append(a.getCity());
        return sb.toString();
    }

    private String txt(TextInputEditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
    private boolean isEmpty(String s){ return s == null || s.trim().isEmpty(); }

    private void seedNorthData() {
        cityToDistricts.put("Hà Nội", new String[]{"Ba Đình", "Hoàn Kiếm", "Cầu Giấy", "Thanh Xuân"});
        cityToDistricts.put("Hải Phòng", new String[]{"Hồng Bàng", "Lê Chân", "Ngô Quyền"});
        cityToDistricts.put("Quảng Ninh", new String[]{"Hạ Long", "Cẩm Phả"});
        cityToDistricts.put("Bắc Ninh", new String[]{"TP. Bắc Ninh", "Từ Sơn"});
        cityToDistricts.put("Hải Dương", new String[]{"TP. Hải Dương", "Chí Linh"});
        cityToDistricts.put("Thái Nguyên", new String[]{"TP. Thái Nguyên", "Sông Công"});
        cityToDistricts.put("Nam Định", new String[]{"TP. Nam Định", "Mỹ Lộc"});
        cityToDistricts.put("Ninh Bình", new String[]{"TP. Ninh Bình", "Tam Điệp"});

        districtToWards.put("Ba Đình",   new String[]{"Điện Biên", "Kim Mã", "Ngọc Hà"});
        districtToWards.put("Hoàn Kiếm", new String[]{"Hàng Trống", "Hàng Bông", "Hàng Bạc"});
        districtToWards.put("Cầu Giấy",  new String[]{"Dịch Vọng", "Dịch Vọng Hậu", "Quan Hoa"});
        districtToWards.put("Thanh Xuân",new String[]{"Thanh Xuân Bắc", "Thanh Xuân Nam", "Hạ Đình"});

        districtToWards.put("Hồng Bàng", new String[]{"Hạ Lý", "Hoàng Văn Thụ"});
        districtToWards.put("Lê Chân",   new String[]{"An Biên", "Hàng Kênh"});
        districtToWards.put("Ngô Quyền", new String[]{"Máy Chai", "Vạn Mỹ"});

        districtToWards.put("Hạ Long",   new String[]{"Bạch Đằng", "Hòn Gai"});
        districtToWards.put("Cẩm Phả",   new String[]{"Cẩm Trung", "Cẩm Sơn"});

        districtToWards.put("TP. Bắc Ninh", new String[]{"Ninh Xá", "Tiền An"});
        districtToWards.put("Từ Sơn",       new String[]{"Đồng Kỵ", "Đình Bảng"});
    }
}
