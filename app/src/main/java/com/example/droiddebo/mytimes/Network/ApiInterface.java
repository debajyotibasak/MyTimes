package com.example.droiddebo.mytimes.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("articles")
    Call<JSONResponse> getJSON(@Query("source") String source, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);
}
