package com.bootcamp.okepedia.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bootcamp.okepedia.R;
import com.bootcamp.okepedia.adapters.ProductsAdapter;
import com.bootcamp.okepedia.databinding.ActivityMainBinding;
import com.bootcamp.okepedia.entities.ProductEntity;
import com.bootcamp.okepedia.viewmodels.ProductViewModel;

public class MainActivity extends AppCompatActivity {

    public final static String DATA_PRODUCT = "data.product";

    private static final int REQUEST_PERMISSIONS_CODE = 111;

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, permissions[0]) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[1]) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_PERMISSIONS_CODE);
        }

        ProductViewModel viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        ProductsAdapter adapter = new ProductsAdapter();
        adapter.setOnProductListener(new ProductsAdapter.OnProductListener() {
            @Override
            public void onEdit(ProductEntity product) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(DATA_PRODUCT, product);

                startActivity(new Intent(
                        MainActivity.this,
                        ProductActivity.class
                ).putExtras(bundle));
            }

            @Override
            public void onDelete(ProductEntity product) {
                viewModel.delete(product);
            }
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.rvProducts.setLayoutManager(layoutManager);
        binding.rvProducts.setAdapter(adapter);

        viewModel.getProducts().observe(this, adapter::setProducts);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                viewModel.filter(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        binding.setViewModel(viewModel);
    }

    public void newProduct(View view) {
        startActivity(new Intent(this, ProductActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (grantResults.length != permissions.length) {
                requestPermissions(permissions, REQUEST_PERMISSIONS_CODE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}