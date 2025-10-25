package com.example.myapplication10;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class ProductFormActivity extends AppCompatActivity {
    public static final String EXTRA_PRODUCT_ID = "product_id";

    private EditText edtName, edtPrice, edtImage, edtDesc, edtCategory;
    private MaterialButton btnSave;
    private ProductManager productManager;
    private Product editing; // null = thêm mới

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        productManager = new ProductManager(this);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtImage = findViewById(R.id.edtImage);
        edtDesc = findViewById(R.id.edtDesc);
        edtCategory = findViewById(R.id.edtCategory);
        btnSave = findViewById(R.id.btnSave);

        long id = getIntent().getLongExtra(EXTRA_PRODUCT_ID, -1);
        if (id != -1) {
            editing = productManager.getById(id);
            if (editing != null) {
                edtName.setText(editing.getTitle());
                edtPrice.setText(String.valueOf(editing.getPrice()));
                edtImage.setText(editing.getImage());
                edtDesc.setText(editing.getDescription());
                edtCategory.setText(editing.getCategory());
                setTitle("Sửa sản phẩm");
            }
        } else setTitle("Thêm sản phẩm");

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {
        String title = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String image = edtImage.getText().toString().trim();
        String desc = edtDesc.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();

        if (title.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Nhập tên và giá", Toast.LENGTH_SHORT).show();
            return;
        }
        double price = Double.parseDouble(priceStr);

        if (editing == null) {
            productManager.insert(title, price, image, desc, category);
        } else {
            productManager.update(editing.getId(), title, price, image, desc, category);
        }
        setResult(RESULT_OK);
        finish();
    }
}

