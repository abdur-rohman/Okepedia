package com.bootcamp.okepedia.viewmodels;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.bootcamp.okepedia.entities.ProductEntity;
import com.bootcamp.okepedia.repositories.ProductRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepository repository;
    private LiveData<List<ProductEntity>> products;
    private MutableLiveData<String> photo = new MutableLiveData<>();
    private MutableLiveData<String> query = new MutableLiveData<>("%");
    private MutableLiveData<ProductEntity> product = new MutableLiveData<>();

    public ProductViewModel(@NonNull Application application) {
        super(application);

        repository = new ProductRepository(application);

        products = Transformations.switchMap(query, input -> repository.filter(input));
    }

    public void insert(ProductEntity product) {
        repository.insert(product);
    }

    public void post(File file, String image, String name, String price, String stock, String categoryId) {
        repository.post(file, image, name, price, stock, categoryId);
    }

    public void update(ProductEntity product) {
        repository.update(product);
    }

    public void delete(ProductEntity product) {
        if (new File(product.getPhoto()).delete())
            System.out.println("DELETED: " + product.getPhoto());

        repository.delete(product);
    }

    public LiveData<List<ProductEntity>> getProducts() {
        return products;
    }

    public LiveData<String> getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo.setValue(photo);
    }

    public void filter(String s) {
        String query = TextUtils.isEmpty(s) ? "%" : "%" + s + "%";
        this.query.postValue(query);
    }

    public LiveData<ProductEntity> getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product.setValue(product);
    }
}
