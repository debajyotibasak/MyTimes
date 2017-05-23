package com.example.droiddebo.mytimes.Network;

import com.example.droiddebo.mytimes.Model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("articles")
    Call<ArticleResponse> getCall(@Query("source") String source, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);
}
