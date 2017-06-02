package com.example.droiddebo.mytimes.network;

import com.example.droiddebo.mytimes.model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
** Uses the URL Endpoint and other queries to complete the call.
**/
public interface ApiInterface {
    @GET("articles")
    Call<ArticleResponse>
    getCall(@Query("source") String source,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey);
}
