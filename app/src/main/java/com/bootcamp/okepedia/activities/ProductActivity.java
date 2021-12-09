package com.bootcamp.okepedia.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bootcamp.okepedia.R;
import com.bootcamp.okepedia.databinding.ActivityProductBinding;
import com.bootcamp.okepedia.entities.ProductEntity;
import com.bootcamp.okepedia.utils.FileUtil;
import com.bootcamp.okepedia.viewmodels.ProductViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 111;

    private ProductViewModel viewModel;
    private ActivityProductBinding binding;
    private File photoFile;
    private ProductEntity product;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);

        Bundle bundle = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("NEW PRODUCT");
        }

        viewModel.setPhoto("");

        if (bundle != null) {
            product = bundle.getParcelable(MainActivity.DATA_PRODUCT);

            if (product != null) {
                getSupportActionBar().setTitle("UPDATE " + product.getName().toUpperCase());

                viewModel.setPhoto(product.getPhoto());
                viewModel.setProduct(product);

                binding.btnAdd.setText("UPDATE PRODUCT");

                isUpdate = true;
            }
        }

        if (product == null)
            product = new ProductEntity(0, 0, "", "");

        viewModel.getProduct().observe(this, product -> {
            binding.etName.setText(product.getName());
            binding.etPrice.setText(product.getPrice() + "");
            binding.etQuantity.setText(product.getQuantity() + "");
        });

        viewModel.getPhoto().observe(this, s -> {
            if (!TextUtils.isEmpty(s))
                binding.ivPhoto.setImageBitmap(BitmapFactory.decodeFile(s));
            else
                binding.ivPhoto.setImageResource(R.drawable.download);
        });

        binding.cvPhoto.setOnClickListener(view -> dispatchTakePictureIntent());
        binding.btnAdd.setOnClickListener(view -> {
            String name = binding.etName.getText() != null ?
                    binding.etName.getText().toString().trim() : "";
            String photo = photoFile != null ? photoFile.getAbsolutePath() : "";

            long price = 0, quantity = 0;

            try {
                price = Long.parseLong(
                        Objects.requireNonNull(binding.etPrice.getText()).toString().trim()
                );

                quantity = Long.parseLong(
                        Objects.requireNonNull(binding.etQuantity.getText()).toString().trim()
                );
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setPhoto(photo);

            if (name.equals("") || price == 0 || quantity == 0 || photo.equals("")) {
                if (name.equals("")) showMessage("Product name can't be empty!");
                else if (price == 0) showMessage("Price can't be zero!");
                else if (quantity == 0) showMessage("Quantity can't be zero!");
                else showMessage("Photo can't be empty!");
            } else {
                if (isUpdate) viewModel.update(product);
                else {
                    viewModel.insert(product);

                    FileUtil.compressImage(photoFile);

                    viewModel.post(photoFile, product.getName() + ".png", product.getName(), price + "", quantity + "", "1");
                }

//                finish();
            }
        });

        binding.setViewModel(viewModel);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            if (photoFile != null && photoFile.delete())
                Log.e("DELETE", photoFile.getAbsolutePath());

            photoFile = createImageFile();

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat(
                    "yyyyMMdd_HHmmss",
                    Locale.getDefault()
            ).format(new Date());

            return File.createTempFile(
                    "JPEG_" + timeStamp + "_",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.setPhoto(photoFile.getAbsolutePath());
        }
    }
}