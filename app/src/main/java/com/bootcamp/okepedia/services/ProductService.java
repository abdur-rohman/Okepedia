package com.bootcamp.okepedia.services;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProductService {
    @Multipart
    @POST("v1/product")
    Call<HashMap<String, Object>> postProduct(
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("stock") RequestBody stock,
            @Part("category_id") RequestBody categoryId,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("v1/product")
    Call<HashMap<String, Object>> postProduct(
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("stock") RequestBody stock,
            @Part("category_id") RequestBody categoryId
    );

    @Multipart
    @POST("v1/product")
    Call<HashMap<String, Object>> postProduct(
            @Part MultipartBody.Part image
    );
}
