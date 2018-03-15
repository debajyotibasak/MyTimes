package com.news.droiddebo.mytimes.network;

import com.news.droiddebo.mytimes.model.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
** Main Retrofit Client which parses the Base URL and uses
** OkHttp custom client to build the request.
**/
public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(OkHttpClient.Builder httpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)                                  //<-- Base URL is loaded
                .addConverterFactory(GsonConverterFactory.create()) //<-- Uses GSON convertor to convert the JSON to JAVA objects
                .client(httpClient.build())                         //<-- Builds the request with OkHttp
                .build();
        }
        return retrofit;
    }
}
