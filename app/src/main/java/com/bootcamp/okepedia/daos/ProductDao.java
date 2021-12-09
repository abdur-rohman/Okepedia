package com.bootcamp.okepedia.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bootcamp.okepedia.entities.ProductEntity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id DESC")
    LiveData<List<ProductEntity>> getProducts();

    @Query("SELECT * FROM products WHERE name LIKE :query ORDER BY id DESC")
    LiveData<List<ProductEntity>> getProductsFilter(String query);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ProductEntity product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserts(List<ProductEntity> products);

    @Update()
    void update(ProductEntity product);

    @Delete()
    void delete(ProductEntity product);
}
