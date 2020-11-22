package com.news.droiddebo.mytimes.network.interceptors;

import androidx.annotation.NonNull;

import com.news.droiddebo.mytimes.util.UtilityMethods;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor to cache data and maintain it for four weeks.
 * <p>
 * If the device is offline, stale (at most four weeks old)
 * response is fetched from the cache.
 */
public class OfflineResponseCacheInterceptor implements Interceptor {
    @NotNull
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
