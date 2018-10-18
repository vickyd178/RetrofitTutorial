package com.vickyd178.retrofittutorial.api;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String AUTH = "Basic "+Base64.encodeToString(("vickyd:454060").getBytes(),Base64.NO_WRAP);
    private static final String BASE_URL ="http://192.168.0.102/MyApi/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){

        //Authentication for api calls to prevent intrusion

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("Authorization",AUTH)
                                .method(original.method(),original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient) //Authentication for api calls to detect valid api calls
                .build();
    }

    public static synchronized RetrofitClient getmInstance(){
        if (mInstance==null){
            mInstance = new RetrofitClient();

        }
        return mInstance;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
