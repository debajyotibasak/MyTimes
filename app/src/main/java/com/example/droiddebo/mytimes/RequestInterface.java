package com.example.droiddebo.mytimes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestInterface {
    @GET("articles")
    Call<JSONResponse> getJSON(@Query("source") String source, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);
}
