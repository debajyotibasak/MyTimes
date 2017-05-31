package com.example.droiddebo.mytimes.network.interceptors;

import android.support.annotation.NonNull;

import com.example.droiddebo.mytimes.util.UtilityMethods;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor to cache data and maintain it for four weeks.
 *
 * If the device is offline, stale (at most four weeks old)
 * response is fetched from the cache.
 */

public class OfflineResponseCacheInterceptor implements Interceptor{
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (!UtilityMethods.isNetworkAvailable()) {
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 2419200)
                    .build();
        }
        return chain.proceed(request);
    }
}
