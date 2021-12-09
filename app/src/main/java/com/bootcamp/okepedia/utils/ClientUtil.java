package com.bootcamp.okepedia.utils;

import android.util.Log;

import com.bootcamp.okepedia.BuildConfig;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtil {
    public static <T> T service(Class<T> clazz, String baseUrl) {
        OkHttpClient.Builder okHttp = new OkHttpClient().newBuilder();
        okHttp.retryOnConnectionFailure(true);
        okHttp.connectTimeout(60, TimeUnit.SECONDS);
        okHttp.writeTimeout(60, TimeUnit.SECONDS);
        okHttp.readTimeout(60, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(s -> Log.e("API-LOG", s));
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        if (BuildConfig.DEBUG) okHttp.addInterceptor(interceptor);

        okHttp.addInterceptor(chain -> {
            Request request = chain.request();
            Request newReq = request.newBuilder()
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Encoding", "identity")
                    .addHeader("Connection", "close")
                    .addHeader("Transfer-Encoding", "chunked")
                    .build();
            return chain.proceed(newReq);
        });

        OkHttpClient client = okHttp.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().setLenient().create())
                )
                .build();

        return retrofit.create(clazz);
    }
}
