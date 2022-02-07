package com.webnotics.swipee.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SwipeeApiClient {

    /*create static retrofit instance with null*/
    private static Retrofit swipeeRetrofit = null;
    /*create static  instance with null*/
    private static RestService swipeeService = null;

    /*create singleton retrofit client*/
    public static Retrofit getswipeeRetrofitInstance() {
        if (swipeeRetrofit == null) {

            swipeeRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiUrls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return swipeeRetrofit;
    }

    /*create singleton  object*/
    public static RestService swipeeServiceInstance() {
        if (swipeeService == null) {
            swipeeService = getswipeeRetrofitInstance().create(RestService.class);
        }
        return swipeeService;
    }

    public static Gson gson =
            new GsonBuilder()
                    .setLenient()
                    .create();

    //Records network calls logs in logcat
    private static final HttpLoggingInterceptor httpLoggingInterceptor =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    /*Create Httpclient to send and get data over network*/
    public static OkHttpClient okHttpClient
            = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(httpLoggingInterceptor)
            .build();


}
