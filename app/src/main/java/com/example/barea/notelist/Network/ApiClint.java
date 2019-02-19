package com.example.barea.notelist.Network;

import android.content.Context;
import android.icu.util.TimeUnit;
import android.text.TextUtils;
import android.util.Base64;

import com.example.barea.notelist.Utils.Const;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.barea.notelist.Utils.PrefUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;


public class ApiClint {
    private static OkHttpClient okHttpClient;
    private static  Retrofit retrofit = null;

    public static Retrofit getClient(Context context){

        if (okHttpClient == null){

            initOkhttp(context);
        }

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;

    }

    private static void initOkhttp(final  Context context){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request orginal = chain.request();
                Request.Builder requestbuilder = orginal.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");
                if (!TextUtils.isEmpty(PrefUtils.getApiKey(context))) {

                 requestbuilder.addHeader("Authorization", PrefUtils.getApiKey(context));

                }
                Request request = requestbuilder.build();
                return chain.proceed(request);
            }
        });
        okHttpClient = httpClient.build();
    }
}
