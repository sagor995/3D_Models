package com.appsdevsa.threedp;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    private static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String CACHE_CONTROL = "Cache-Control";
    //private static String API_TOKEN = getString(R.string.public_app_api_key);

    public static String API_TOKEN = "MRDieyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImVjMzhhOTA4Nzc4ODhlOTViZTNmYjczNzE1NmVkMWM2MmMwYTBhMjlmNDUwYmJjZmY5ZGNjOGY1MGExYTg4YWZjYjU0MTdlNTI5YzdhZGNkIn0.eyJhdWQiOiIzIiwianRpIjoiZWMzOGE5MDg3Nzg4OGU5NWJlM2ZiNzM3MTU2ZWQxYzYyYzBhMGEyOWY0NTBiYmNmZjlkY2M4ZjUwYTFhODhhZmNiNTQxN2U1MjljN2FkY2QiLCJpYXQiOjE1NDM0Nzg3NTcsIm5iZiI6MTU0MzQ3ODc1NywiZXhwIjoxNTc1MDE0NzU3LCJzdWIiOiIyIiwic2NvcGVzIjpbXX0.BTBFq-5QhNkskRpp_AK1zrGAz_gbzsawBWyuw3K2RQY7ck7a4JnMH7jX8csaG29eS8dmJSmVko_swEoCl8W1Ucc4Hiy25vMtN8n7eyeeesxGJcDgUtXZp6KZWCRIG_-pp098RZwNan2NZvgzx3DKzgXEIqTCvvAzAO6jQGhVlTe8qSW972meuLTrg2upG3VLRVtRdGLiXXfaoQBv_fdTafz_3jVlulxN7UY9B1TnsBFfywt9QcNgvYYBhRUQeP9aTyQmH_1h7itk4ETcX_o8ZvqNhU8MyJt4gr4eKUzPccGpWSsLFFNa0E-1MvxMZK7Vr0__l-843kzcJOQWAjxysh6aaVXkxs-MghvkBlppsnD3TA1fl-SCdlvixXRVpaHv7Qn0wtoNfxTIgpR2hPp54nGRl3qB2aefQp6JQbjiXqGbcO4RQEyAZbJR_n8HsIJhm3LwEs_QSEmNVbBMNHuGU2JW5_TpBjfcmzbxp-4kTH8Xh5Cnu0kCPlg4t0F2K94t_IDvu9i2VxjVMhCya60QwVxJYygNnn7YaeVuIcQ35IkQ1xzFCVuKzBJPoJnR-WT-AdmF0DsgMQ6hzis62ohRDJbl8hhYp4BAL84rEZdgjecyACz9qPe3zdb7oL99tDC2Qg932az2atv4lfV4rI5duYdiXOz4O6a6YBUTDt9Qm1010MRDI";

    private static final String BASE_URL = "http://mrdi.10minuteschool.com/api/";



    private static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(5, TimeUnit.MINUTES);
            clientBuilder.readTimeout(5, TimeUnit.MINUTES);
            clientBuilder.writeTimeout(5, TimeUnit.MINUTES);
            //clientBuilder.cache(getCache());
            clientBuilder.addInterceptor(getHttpLoggingInterceptor());
            clientBuilder.addNetworkInterceptor(provideHeaderInterceptor());
            OkHttpClient client = clientBuilder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }


    public static AssessmentService getAssessmentService(){
        if (retrofit == null){
            retrofit = getRetrofitClient();
        }
        return retrofit.create(AssessmentService.class);
    }


    /*private static Cache getCache() {
        Cache cache = null;
        try {
            File cacheDir = new File(MyApplication.getInstance().getCacheDir(), "http-cache");
            cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        } catch (Exception e) {

        }
        return cache;
    }*/
    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return httpLoggingInterceptor;
    }

    public static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();
                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }
    private static Interceptor provideHeaderInterceptor() {
        return new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Request request;

                request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();


                return chain.proceed(request);
            }
        };
    }
}
