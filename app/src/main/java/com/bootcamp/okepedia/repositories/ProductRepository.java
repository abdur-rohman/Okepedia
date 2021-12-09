package com.bootcamp.okepedia.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bootcamp.okepedia.daos.ProductDao;
import com.bootcamp.okepedia.database.LocalDatabase;
import com.bootcamp.okepedia.entities.ProductEntity;
import com.bootcamp.okepedia.services.ProductService;
import com.bootcamp.okepedia.utils.ClientUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<ProductEntity>> products;
    private ProductService productService;

    public ProductRepository(Application application) {
        LocalDatabase db = LocalDatabase.getDatabase(application);

        productDao = db.productDao();

        products = productDao.getProducts();

        productService = ClientUtil.service(ProductService.class, "https://rest-api-meser.herokuapp.com/api/");
    }

    public void post(File file, String image, String name, String price, String stock, String categoryId) {
        RequestBody files = RequestBody.create(file, MediaType.parse("image/*"));
        RequestBody productName = RequestBody.create(name, MultipartBody.FORM);
        RequestBody productPrice = RequestBody.create(price, MultipartBody.FORM);
        RequestBody productStock = RequestBody.create(stock, MultipartBody.FORM);
        RequestBody productCategory = RequestBody.create(categoryId, MultipartBody.FORM);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("image", image, files);

        productService.postProduct(productName, productPrice, productStock, productCategory, photo).enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                System.out.println(new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<List<ProductEntity>> getProducts() {
        return products;
    }

    public void insert(ProductEntity product) {
        LocalDatabase.databaseWriteExecutor.execute(() -> productDao.insert(product));
    }

    public void update(ProductEntity product) {
        LocalDatabase.databaseWriteExecutor.execute(() -> productDao.update(product));
    }

    public void delete(ProductEntity product) {
        LocalDatabase.databaseWriteExecutor.execute(() -> productDao.delete(product));
    }

    public LiveData<List<ProductEntity>> filter(String query) {
        return productDao.getProductsFilter(query);
    }
}
