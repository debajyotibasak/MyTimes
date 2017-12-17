package com.news.droiddebo.mytimes.network;

import com.news.droiddebo.mytimes.model.ArticleResponse;
import com.news.droiddebo.mytimes.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
** Uses the URL Endpoint and other queries to complete the call.
**/
public interface ApiInterface {

    //Old Endpoint.
    @GET("articles")
    Call<ArticleResponse> getCall(@Query("source") String source,
                                  @Query("sortBy") String sortBy,
                                  @Query("apiKey") String apiKey);

    //New Endpoint to fetch headlines.
    @GET("top-headlines")
    Call<NewsResponse> getHeadlines(@Query("sources") String sources,
                                    @Query("apiKey") String apiKey);

    //New Endpoint to fetch search results.
    @GET("everything")
    Call<NewsResponse> getSearchResults(@Query("q") String query,
                                           @Query("sortBy") String sortBy,
                                           @Query("language") String language,
                                           @Query("apiKey") String apiKey);

}
