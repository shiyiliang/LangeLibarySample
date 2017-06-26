package com.example.smallhttp.http;

import android.text.TextUtils;
import android.view.TextureView;

import java.lang.reflect.Field;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-26
 * Desc  :
 */

public class ServiceFactory {

    public ServiceFactory() {
    }

    private static class Single {
        private static ServiceFactory factory = new ServiceFactory();
    }

    public ServiceFactory getInstance() {
        return Single.factory;
    }

    public <T> T createService(Class<T> clazz) throws IllegalAccessException {
        String baseUrl = null;
        try {
            Field f = clazz.getField("BASE_URL");
            baseUrl = (String) f.get(clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException(clazz.getSimpleName() + ": BASE_URL can't be null.");
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpClicentFactory.createDefault())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(clazz);
    }
}
